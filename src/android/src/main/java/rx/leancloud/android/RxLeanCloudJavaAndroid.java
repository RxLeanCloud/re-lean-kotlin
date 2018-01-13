package rx.leancloud.android;

import rx.leancloud.core.RxAVCorePlugins;

public class RxLeanCloudJavaAndroid {
    public static void link() {
        RxAVCorePlugins.getInstance().setJson(RxLeanCloudJavaAndroidJSONClient.getInstance());
        RxAVCorePlugins.getInstance().setHttpClient(RxLeanCloudJavaAndroidHttpClient.getInstance());
    }
}
