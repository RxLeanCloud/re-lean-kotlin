package rx.leancloud.internal;

import java.util.Map;

public interface IJSONClient {

    String encode(Object data);

    Object parse(String input);
}
