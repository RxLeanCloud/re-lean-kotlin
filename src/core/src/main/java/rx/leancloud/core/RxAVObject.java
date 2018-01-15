package rx.leancloud.core;

import io.reactivex.*;
import rx.leancloud.internal.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RxAVObject {

    static AVObjectController getObjectController() {
        return AVInternalPlugins.getInstance().getObjectController();
    }

    private AVObjectState state = new AVObjectState();
    private Map<String, Object> localEstimatedData = new HashMap<>();
    private boolean isDirty;
    private boolean hasBeenFetched;

    private LinkedList<Map<String, IAVFieldOperation>> operationSetQueue = new LinkedList<>();

    protected Map<String, IAVFieldOperation> getCurrentOperations() {
        synchronized (this) {
            return this.operationSetQueue.getLast();
        }
    }

    public RxAVObject(String className) {
        this.state.className = className;
        this.isDirty = true;
        this.operationSetQueue.addLast(new HashMap<>());
    }

    public String getObjectId() {
        return this.state.objectId;
    }

    protected void setObjectId(String objectId) {
        this.state.objectId = objectId;
    }

    public String getClassName() {
        return this.state.className;
    }

    public void set(String key, Object value) {
        if (value == null) {
            this.performOperation(key, AVDeleteOperation.getInstance());
        } else {
            boolean valid = getObjectController().getObjectEncoder().isValidType(value);
            if (valid) {
                this.performOperation(key, new AVSetOperation(value));
            }
        }
    }

    public Single<Boolean> saveRx() {
        return AVTaskQueue.once(() -> {
            try {
                return this.saveInternal();
            } catch (RxAVException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public void save() {
        try {
            this.saveInternal();
        } catch (RxAVException e) {
            e.printStackTrace();
        }
    }

    public void saveAsync() {
        AVTaskQueue.enqueue(this.saveRx());
    }

    protected boolean saveInternal() throws RxAVException {
        if (!this.isDirty)
            return true;
        this.state.serverData = this.localEstimatedData;
        AVObjectState serverState = getObjectController().save(this.state, this.getCurrentOperations());
        if (serverState == null)
            return false;
        this.handleSaved(serverState);
        return true;
    }

    protected void handleSaved(AVObjectState serverState) {
        this.state.mergeInternalFields(serverState);
    }

    protected void performOperation(String key, IAVFieldOperation operation) {
        Object oldValue = this.localEstimatedData.get(key);
        Object newValue = operation.apply(oldValue, key);

        if (newValue != AVDeleteOperation.getDeleteToken()) {
            this.localEstimatedData.put(key, newValue);
        } else {
            this.localEstimatedData.remove(key);
        }

        Map<String, IAVFieldOperation> currentOperations = this.getCurrentOperations();
        IAVFieldOperation oldOperation = currentOperations.get(key);
        IAVFieldOperation newOperation = operation.mergeWithPrevious(oldOperation);
        currentOperations.put(key, newOperation);

        this.isDirty = currentOperations.size() > 0;
    }

    public static RxAVObject createWithoutData(String className, String objectId) {
        RxAVObject avObject = new RxAVObject(className);
        avObject.setObjectId(objectId);
        avObject.hasBeenFetched = false;
        return avObject;
    }
}
