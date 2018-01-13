package rx.leancloud.core;

import rx.leancloud.internal.AVCommand;
import rx.leancloud.internal.AVCommandResponse;

public class RxAVClient {
    private static RxAVClient ourInstance = new RxAVClient();

    public static RxAVClient getInstance() {
        return ourInstance;
    }

    private LeanCloudApp currentApp;

    public LeanCloudApp getCurrentApp() {
        return currentApp;
    }

    public void setCurrentApp(LeanCloudApp currentApp) {
        this.currentApp = currentApp;
    }

    public String getSDKVersion() {
        return "0.1.0";
    }


    public void commandLog(AVCommand request, AVCommandResponse response) {
        if (LeanCloud.isLogOpened()) {
            LeanCloud.log("===Command-START===");
            LeanCloud.log("===Request-START===");
            LeanCloud.log("Url: " + request.url);
            LeanCloud.log("Method: " + request.method);
            LeanCloud.log("Headers: " + request.headers.toString());
            LeanCloud.log("RequestBody: " + request.jsonData);
            LeanCloud.log("===Request-END===");
            LeanCloud.log("===Response-START===");
            LeanCloud.log("StatusCode: " + response.statusCode);
            LeanCloud.log("ResponseBody: " + response.jsonBody());
            LeanCloud.log("===Response-END===");
            LeanCloud.log("===Command-END===");
        }
    }
}
