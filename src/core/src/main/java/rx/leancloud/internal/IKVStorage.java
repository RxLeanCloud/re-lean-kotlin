package rx.leancloud.internal;

import java.util.Map;

/**
 * JSON-format key-value local storage interface.
 */
public interface IKVStorage {

    String set(String key, String value);

    boolean remove(String key);

    String get(String key);

    String setJson(String key, Map<String, Object> value);
}
