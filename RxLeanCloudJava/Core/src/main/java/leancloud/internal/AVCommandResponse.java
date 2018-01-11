package leancloud.internal;

import leancloud.core.RxAVCorePlugins;

import java.util.HashMap;
import java.util.Map;

public class AVCommandResponse extends HttpResponse {

    public AVCommandResponse(HttpResponse httpResponse) {
        this.body = httpResponse.body;
        this.statusCode = httpResponse.statusCode;
    }

    public Map<String, Object> jsonBody() {
        Object result = this.getJSONBody();
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        } else if (result instanceof HashMap) {
            return (HashMap<String, Object>) result;
        }
        return (Map<String, Object>) result;
    }
}
