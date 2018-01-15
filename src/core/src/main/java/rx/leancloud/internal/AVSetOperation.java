package rx.leancloud.internal;

public class AVSetOperation implements IAVFieldOperation {
    public Object value;

    public AVSetOperation(Object value) {
        this.value = value;
    }

    @Override
    public Object encode() {
        return AVInternalPlugins.getInstance().getAVEncoder().encode(this.value);
    }

    @Override
    public IAVFieldOperation mergeWithPrevious(IAVFieldOperation previous) {
        return this;
    }

    @Override
    public Object apply(Object oldValue, String key) {
        return this.value;
    }
}
