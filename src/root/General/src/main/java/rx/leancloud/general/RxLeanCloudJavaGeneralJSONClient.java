package rx.leancloud.general;

import com.alibaba.fastjson.JSON;
import rx.leancloud.internal.IJSONClient;

public class RxLeanCloudJavaGeneralJSONClient implements IJSONClient {
    private static RxLeanCloudJavaGeneralJSONClient ourInstance = new RxLeanCloudJavaGeneralJSONClient();

    public static RxLeanCloudJavaGeneralJSONClient getInstance() {
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
