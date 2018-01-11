package leancloud.internal;

import leancloud.core.RxAVCorePlugins;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    public int statusCode;
    public byte[] body;

    public Object getJSONBody() {
        String str = new String(this.body, StandardCharsets.UTF_8);
        return RxAVCorePlugins.getInstance().getJson().parse(str);
    }

}
