package leancloud.internal;

import java.io.IOException;

public interface IAVCommandRunner {
    AVCommandResponse execute(AVCommand command) throws IOException;
}
