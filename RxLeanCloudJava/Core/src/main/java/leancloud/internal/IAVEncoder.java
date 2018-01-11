package leancloud.internal;

public interface IAVEncoder {
    Object encode(Object value);

    boolean isValidType(Object value);
}
