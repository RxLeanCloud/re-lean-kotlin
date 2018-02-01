package rx.leancloud.core;

import rx.leancloud.internal.AVCommand;
import rx.leancloud.internal.AVCommandResponse;
import rx.leancloud.internal.AVHttpCommandRunner;
import rx.leancloud.internal.IAVCommandRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RxAVClient {
    private static RxAVClient ourInstance = new RxAVClient();

    public static RxAVClient getInstance() {
        return ourInstance;
    }

    public RxAVClient() {
    }

    private Map<String, LeanCloudApp> remoteApps = new HashMap<>();

    public Map<String, LeanCloudApp> getRemoteApps() {
        return remoteApps;
    }

    public LeanCloudApp getCurrentApp() {
        return this.remoteApps.get("default");
    }

    private IAVCommandRunner httpCommandRunner;

    public IAVCommandRunner getHttpCommandRunner() {
        if (this.httpCommandRunner == null) {
            this.httpCommandRunner = new AVHttpCommandRunner(RxAVCorePlugins.getInstance().getHttpClient());
        }
        return this.httpCommandRunner;
    }


    public String getSDKVersion() {
        return "0.1.0";
    }

    public RxAVClient add(LeanCloudApp app, String shortName) {
        this.getRemoteApps().put(shortName, app);
        return RxAVClient.getInstance();
    }

    public Map<String, Object> runCommand(String relativeUrl, String method, Map<String, Object> data) {
        AVCommand command = new AVCommand();
        command.jsonData = data;
        command.method = method;
        command.url = this.getUrl(relativeUrl);

        try {
            AVCommandResponse response = this.getHttpCommandRunner().execute(command);
            return response.jsonBody();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RxAVException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getUrl(String relativeUrl) {
        return RxAVClient.getInstance().getCurrentApp().getUrl(relativeUrl);
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
