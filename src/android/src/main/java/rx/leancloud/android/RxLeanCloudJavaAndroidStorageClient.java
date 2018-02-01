package rx.leancloud.android;

import android.content.SharedPreferences;

import java.util.Map;

import rx.leancloud.internal.IKVStorage;

/**
 * Created by wujun on 23/01/2018.
 */

public class RxLeanCloudJavaAndroidStorageClient implements IKVStorage {

    public static final String PREFS_NAME = "rxLeanCloudCacheFile.json";
    @Override
    public String set(String key, String value) {
        return null;
    }

    @Override
    public boolean remove(String key) {
        return false;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String setJson(String key, Map<String, Object> value) {
        return null;
    }
}
