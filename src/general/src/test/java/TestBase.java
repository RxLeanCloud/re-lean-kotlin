

import rx.leancloud.core.LeanCloud;
import org.junit.Before;
import rx.leancloud.core.LeanCloudApp;
import rx.leancloud.core.RxAVClient;
import rx.leancloud.general.RxLeanCloudJavaGeneral;

public class TestBase {
    @Before
    public void setUp() {
        LeanCloud.initialize("uay57kigwe0b6f5n0e1d4z4xhydsml3dor24bzwvzr57wdap","kfgz7jjfsk55r5a8a3y4ttd3je1ko11bkibcikonk32oozww");
        LeanCloud.toggleLog(true);
        RxLeanCloudJavaGeneral.link();

        LeanCloudApp app = new LeanCloudApp("315XFAYyIGPbd98vHPCBnLre-9Nh9j0Va","Y04sM6TzhMSBmCMkwfI3FpHc", LeanCloudApp.AVRegion.Public_East_CN);
        LeanCloud.addApp(app,"test");
    }
}
