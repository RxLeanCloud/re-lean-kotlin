package rx.leancloud.android;

import com.alibaba.fastjson.JSON;
import rx.leancloud.internal.IJSONClient;

public class RxLeanCloudJavaAndroidJSONClient implements IJSONClient {
    private static RxLeanCloudJavaAndroidJSONClient ourInstance = new RxLeanCloudJavaAndroidJSONClient();

    public static RxLeanCloudJavaAndroidJSONClient getInstance() {
        return ourInstance;
    }

    @Override
    public String encode(Object data) {
        return JSON.toJSONString(data);
    }

    @Override
    public Object parse(String input) {
        return JSON.parse(input);
    }
}
