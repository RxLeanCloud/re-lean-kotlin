package rx.leancloud.core;

import io.reactivex.*;
import rx.leancloud.internal.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RxAVObject {

    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_UPDATED_AT = "updatedAt";

    static AVObjectController getObjectController() {
        return AVInternalPlugins.getInstance().getObjectController();
    }

    static AVObjectSubclassingController getSubclassingController() {
        return AVInternalPlugins.getInstance().getSubclassingController();
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

    public <T> T get(String key) {
        return this.get(null, key);
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
        this.isDirty = false;
    }

    protected void handleFetchResult(AVObjectState serverState) {
        this.state.apply(serverState);
        this.isDirty = false;
        this.rebuildLocalEstimatedData();
    }

    protected void rebuildLocalEstimatedData() {
        this.localEstimatedData = this.state.serverData;
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

    protected <T> T get(T defaultValue, String key) {
        T result;
        if (!this.localEstimatedData.containsKey(key)) return defaultValue;
        try {
            result = (T) this.localEstimatedData.get(key);
            return result;
        } catch (ClassCastException exc) {
        }
        return defaultValue;
    }

    public static RxAVObject create(String className) {
        return getSubclassingController().newInstance(className);
    }

    @SuppressWarnings("unchecked")
    public static <T extends RxAVObject> T create(Class<T> subclass) {
        return (T) create(getSubclassingController().getClassName(subclass));
    }

    public static RxAVObject createWithoutData(String className, String objectId) {
        RxAVObject avObject = RxAVObject.create(className);
        avObject.setObjectId(objectId);
        avObject.hasBeenFetched = false;
        return avObject;
    }

    static <T extends RxAVObject> T fromMap(Map<String, Object> metaMap, String defaultClassName, AVDecoder decoder) {
        String className = defaultClassName;
        if (metaMap.containsKey("className")) {
            className = (String) metaMap.get("className");
        }
        if (className == null)
            return null;
        String objectId = (String) metaMap.get(KEY_OBJECT_ID);
        T object = (T) RxAVObject.create(className);
        AVObjectState state = getObjectController().decode(metaMap);
        object.handleFetchResult(state);
        return object;
    }

    static void registerSubclass(Class<? extends RxAVObject> subclass) {
        RxAVObject.getSubclassingController().registerSubclass(subclass);
    }

    /* package */
    static void registerInternalSubclasses() {
        RxAVObject.registerSubclass(RxAVUser.class);
    }
}
