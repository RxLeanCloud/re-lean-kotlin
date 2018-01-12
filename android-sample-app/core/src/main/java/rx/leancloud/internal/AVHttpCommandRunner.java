package rx.leancloud.internal;

import rx.leancloud.core.RxAVClient;
import rx.leancloud.core.RxAVException;

import java.io.IOException;

public class AVHttpCommandRunner implements IAVCommandRunner {
    private IHttpClient httpClient;

    public AVHttpCommandRunner(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public AVCommandResponse execute(AVCommand command) throws IOException, RxAVException {
        HttpResponse httpResponse = this.httpClient.execute(command);
        AVCommandResponse response = new AVCommandResponse(httpResponse);
        RxAVClient.getInstance().commandLog(command, response);
        if (response.statusCode > 201 || response.statusCode < 200) {
            throw new RxAVException(response.statusCode, response.errorMessage());
        }
        return response;
    }
}
