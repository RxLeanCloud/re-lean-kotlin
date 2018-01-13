package rx.leancloud.core;

public class RxAVException extends Exception {
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public RxAVException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
