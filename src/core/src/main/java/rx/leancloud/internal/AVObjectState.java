package rx.leancloud.internal;

import java.util.Date;
import java.util.Map;

public class AVObjectState {
    public String className;
    public String objectId;
    public Date createdAt;
    public Date updatedAt;
    public Map<String, Object> serverData;

    public AVObjectState mutatedClone() {
        AVObjectState state = new AVObjectState();

        state.className = this.className;
        state.objectId = this.objectId;
        state.createdAt = this.createdAt;
        state.updatedAt = this.updatedAt;
        state.serverData = this.serverData;
        return state;
    }

    public void apply(AVObjectState other) {
        this.className = other.className;
        this.objectId = other.objectId;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.serverData = other.serverData;
    }

    public void mergeInternalFields(AVObjectState other) {
        if (other.objectId != null) {
            this.objectId = other.objectId;
        }
        if (other.updatedAt != null) {
            this.updatedAt = other.updatedAt;
        }
        if (other.createdAt != null) {
            this.createdAt = other.createdAt;
        }
    }
}
