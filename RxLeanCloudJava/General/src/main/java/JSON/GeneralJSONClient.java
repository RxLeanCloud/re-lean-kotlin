package JSON;

import com.alibaba.fastjson.JSON;
import leancloud.internal.IJSONClient;


public class GeneralJSONClient implements IJSONClient {
    private static GeneralJSONClient ourInstance = new GeneralJSONClient();

    public static GeneralJSONClient getInstance() {
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
