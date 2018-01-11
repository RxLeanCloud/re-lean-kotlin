import HttpClient.GeneralHttpClient;
import JSON.GeneralJSONClient;
import leancloud.core.RxAVCorePlugins;

public class RxLeanCloudJavaGeneral {
    public static void link() {
        RxAVCorePlugins.getInstance().setJson(GeneralJSONClient.getInstance());
        RxAVCorePlugins.getInstance().setHttpClient(GeneralHttpClient.getInstance());
    }
}
