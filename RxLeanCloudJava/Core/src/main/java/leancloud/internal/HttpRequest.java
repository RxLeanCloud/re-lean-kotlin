package leancloud.internal;

import java.io.InputStream;
import java.util.Map;

public class HttpRequest {
    public String url;
    public String method;
    public InputStream body;
    public Map<String, String> headers;

    public String  getContentType() {
        return this.headers.get("Content-Type");
    }

    public String getUrl() {
        return url;
    }

    public InputStream getBody() {
        return body;
    }
}
