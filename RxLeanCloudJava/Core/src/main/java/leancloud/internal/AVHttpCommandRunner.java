package leancloud.internal;

import leancloud.core.RxAVClient;

import java.io.IOException;

public class AVHttpCommandRunner implements IAVCommandRunner {
    private IHttpClient httpClient;

    public AVHttpCommandRunner(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public AVCommandResponse execute(AVCommand command) throws IOException {
        HttpResponse httpResponse = this.httpClient.execute(command);
        AVCommandResponse response = new AVCommandResponse(httpResponse);
        RxAVClient.getInstance().commandLog(command, response);
        return response;
    }
}
