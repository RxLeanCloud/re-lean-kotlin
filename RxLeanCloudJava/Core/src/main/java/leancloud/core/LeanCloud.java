package leancloud.core;


public class LeanCloud {

    public static void Initialize(String appId, String appKey) {
        LeanCloudApp.AVRegion region = LeanCloudApp.AVRegion.Public_North_CN;
        if (appId.endsWith("9Nh9j0Va")) {
            region = LeanCloudApp.AVRegion.Public_East_CN;
        } else if (appId.endsWith("MdYXbMMI")) {
            region = LeanCloudApp.AVRegion.Public_North_US;
        }

        LeanCloudApp app = new LeanCloudApp(appId, appKey, region);
        Initialize(app);
    }

    public static void Initialize(LeanCloudApp app) {
        RxAVClient.getInstance().setCurrentApp(app);
    }

    private static boolean _log;
    public static boolean isLogOpened(){
        return _log;
    }

    public static void toggleLog(boolean toggle) {
        _log = toggle;
    }

    public static void log(String text) {
        System.out.println(text);
    }

}
