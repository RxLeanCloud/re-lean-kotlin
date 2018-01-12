package rx.leancloud.internal;

import java.io.IOException;

public interface IHttpClient {
    HttpResponse execute(HttpRequest request) throws IOException;
}
