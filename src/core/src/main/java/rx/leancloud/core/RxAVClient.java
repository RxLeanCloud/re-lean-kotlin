package rx.leancloud.core;

import rx.leancloud.internal.AVCommand;
import rx.leancloud.internal.AVCommandResponse;
import rx.leancloud.internal.AVInternalPlugins;
import rx.leancloud.internal.IAVCommandRunner;

import java.io.IOException;
import java.util.Map;

public class RxAVClient {
    private static RxAVClient ourInstance = new RxAVClient();

    public static RxAVClient getInstance() {
        return ourInstance;
    }

    public RxAVClient() {
        this.commandRunner = AVInternalPlugins.getInstance().getHttpCommandRunner();
    }

    private LeanCloudApp currentApp;

    public LeanCloudApp getCurrentApp() {
        return currentApp;
    }

    private IAVCommandRunner commandRunner;

    public IAVCommandRunner getCommandRunner() {
        return this.commandRunner;
    }

    public void setCurrentApp(LeanCloudApp currentApp) {
        this.currentApp = currentApp;
    }

    public String getSDKVersion() {
        return "0.1.0";
    }

    public Map<String, Object> runCommand(String relativeUrl, String method, Map<String, Object> data) {
        AVCommand command = new AVCommand();
        command.jsonData = data;
        command.method = method;
        command.url = this.getUrl(relativeUrl);

        try {
            AVCommandResponse response = this.getCommandRunner().execute(command);
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
