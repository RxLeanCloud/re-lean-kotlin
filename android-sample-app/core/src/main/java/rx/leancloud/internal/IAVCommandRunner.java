package rx.leancloud.internal;

import rx.leancloud.core.RxAVException;

import java.io.IOException;

public interface IAVCommandRunner {
    AVCommandResponse execute(AVCommand command) throws IOException, RxAVException;
}
