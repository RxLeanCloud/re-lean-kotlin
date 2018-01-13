package rx.leancloud.general;

import rx.leancloud.core.RxAVCorePlugins;

public class RxLeanCloudJavaGeneral {
    public static void link() {
        RxAVCorePlugins.getInstance().setJson(RxLeanCloudJavaGeneralJSONClient.getInstance());
        RxAVCorePlugins.getInstance().setHttpClient(RxLeanCloudJavaGeneralHttpClient.getInstance());
    }
}
