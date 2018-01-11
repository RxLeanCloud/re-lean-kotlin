package leancloud.internal;

import leancloud.core.RxAVObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AVDecoder implements IAVDecoder {
    private static AVDecoder ourInstance = new AVDecoder();

    public static AVDecoder getInstance() {
        return ourInstance;
    }
    @Override
    public Object decode(Object value) {
        if (value instanceof Map) {
            Map<String, Object> vMap = (Map<String, Object>) value;
            if (!vMap.containsKey("__type")) {
                Map<String, Object> newMap = new HashMap<>();
                for (Map.Entry<String, Object> entry : vMap.entrySet()) {
                    String key = entry.getKey();
                    Object metaValue = entry.getValue();
                    Object encodedValue = this.decode(metaValue);
                    newMap.put(key, encodedValue);
                }
                return newMap;
            } else {
                String typeName = (String) vMap.get("__type");
                if (typeName == "Date") {
                    String iosString = (String) vMap.get("ios");
                    return this.decodeDate(iosString);
                } else if (typeName == "Pointer") {
                    String className = (String) vMap.get("className");
                    String objectId = (String) vMap.get("objectId");
                    return this.decodePointer(className, objectId);
                }
            }
        } else if (value.getClass().isArray()) {

        }
        return value;
    }

    public RxAVObject decodePointer(String className, String objectId) {
        return RxAVObject.createWithoutData(className, objectId);
    }

    public Date decodeDate(String iosString) {
        DateFormat format = AVInternalPlugins.getInstance().getAVDateFormatter();
        try {
            return format.parse(iosString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
