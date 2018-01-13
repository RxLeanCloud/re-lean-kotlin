package rx.leancloud.internal;

public interface IAVFieldOperation {
    Object encode();

    IAVFieldOperation mergeWithPrevious(IAVFieldOperation previous);

    Object apply(Object oldValue, String key);
}
