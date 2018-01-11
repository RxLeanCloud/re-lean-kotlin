package leancloud.core;

import io.reactivex.Observable;
import leancloud.internal.AVInternalPlugins;
import leancloud.internal.AVObjectController;
import leancloud.internal.AVObjectState;

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

    public void save() {
        this.state.serverData = this.localEstimatedData;
        AVObjectState serverState = getObjectController().save(this.state);
        this.handleSaved(serverState);
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
