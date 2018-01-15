package rx.leancloud.internal;


import rx.leancloud.core.RxAVObject;

import java.util.*;

public class AVEncoder implements IAVEncoder {
    private static AVEncoder ourInstance = new AVEncoder();

    public static AVEncoder getInstance() {
        return ourInstance;
    }

    @Override
    public Object encode(Object value) {
        if (value instanceof RxAVObject) {
            return this.encodeAVObject((RxAVObject) value);
        } else if (value instanceof Date) {
            return this.encodeDate((Date) value);
        } else if (value instanceof IAVFieldOperation) {
            return ((IAVFieldOperation) value).encode();
        }
        return value;
    }

    private Map<String, Object> encodeDate(Date date) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("__type", "Date");
        result.put("iso", AVInternalPlugins.getInstance().getAVDateFormatter().format(date));
        return result;
    }


    private Map<String, Object> encodeAVObject(RxAVObject avObject) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("__type", "Pointer");
        result.put("getClassName", avObject.getClassName());
        result.put("getObjectId", avObject.getObjectId());
        return result;
    }

    @Override
    public boolean isValidType(Object value) {
        return value instanceof Date
                || value instanceof Number
                || value instanceof RxAVObject
                || value instanceof Integer
                || value instanceof Float
                || value instanceof Double
                || value instanceof Map
                || value instanceof HashMap
                || value instanceof ArrayList
                || value instanceof String;
    }
}
