package rx.leancloud.internal;

import java.util.HashMap;

public class AVDeleteOperation implements IAVFieldOperation {
    private static AVDeleteOperation ourInstance = new AVDeleteOperation();

    public static AVDeleteOperation getInstance() {
        return ourInstance;
    }

    private final static Object DeleteToken = new Object();

    public static Object getDeleteToken() {
        return AVDeleteOperation.DeleteToken;
    }

    @Override
    public Object encode() {
        return new HashMap<String, Object>() {{
            put("__op", "Delete");
        }};
    }

    @Override
    public IAVFieldOperation mergeWithPrevious(IAVFieldOperation previous) {
        return this;
    }

    @Override
    public Object apply(Object oldValue, String key) {
        return AVDeleteOperation.getDeleteToken();
    }
}
