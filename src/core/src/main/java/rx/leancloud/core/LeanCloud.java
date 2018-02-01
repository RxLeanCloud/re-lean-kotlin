package rx.leancloud.core;


public class LeanCloud {

    public static void initialize(String appId, String appKey) {
        LeanCloudApp.AVRegion region = LeanCloudApp.AVRegion.Public_North_CN;
        if (appId.endsWith("9Nh9j0Va")) {
            region = LeanCloudApp.AVRegion.Public_East_CN;
        } else if (appId.endsWith("MdYXbMMI")) {
            region = LeanCloudApp.AVRegion.Public_North_US;
        }

        LeanCloudApp app = new LeanCloudApp(appId, appKey, region);
        initialize(app);
    }

    /**
     * @param app LeanCloud app,you can get appId and appKey from https://leancloud.cn/dashboard
     */
    public static void initialize(LeanCloudApp app) {

        RxAVClient.getInstance().add(app, "default");

        RxAVObject.registerInternalSubclasses();
    }

    public static void addApp(LeanCloudApp app,String shortName) {
        RxAVClient.getInstance().add(app, "shortName");
    }

    private static boolean _log;

    public static boolean isLogOpened() {
        return _log;
    }

    public static void toggleLog(boolean toggle) {
        _log = toggle;
    }

    public static void log(String text) {
        System.out.println(text);
    }

}
