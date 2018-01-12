package rx.leancloud.core;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;
import rx.leancloud.internal.AVObjectState;
import rx.leancloud.internal.AVInternalPlugins;
import rx.leancloud.internal.AVObjectController;

import java.util.HashMap;
import java.util.Map;

public class RxAVObject {
    static AVObjectController getObjectController() {
        return AVInternalPlugins.getInstance().getObjectController();
    }

    private AVObjectState state = new AVObjectState();
    private Map<String, Object> localEstimatedData = new HashMap<>();

    public RxAVObject(String className) {
        this.state.className = className;
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
        this.localEstimatedData.put(key, value);
    }

    protected boolean saveInternal() throws RxAVException {
        this.state.serverData = this.localEstimatedData;
        AVObjectState serverState = getObjectController().save(this.state);
        if (serverState == null)
            return false;
        this.handleSaved(serverState);
        return true;
    }

    public Single<Boolean> rxSave() {
        return Single.create((source) -> {
            try {
                source.onSuccess(this.saveInternal());
            } catch (RxAVException e) {
                source.onError(e);
            }
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
        this.rxSave().observeOn(Schedulers.single()).subscribe(System.out::println, Throwable::printStackTrace);
    }



    protected void handleSaved(AVObjectState serverState) {
        this.state.mergeInternalFields(serverState);
    }

    public static RxAVObject createWithoutData(String className, String objectId) {
        RxAVObject avObject = new RxAVObject(className);
        avObject.setObjectId(objectId);
        return avObject;
    }
}
