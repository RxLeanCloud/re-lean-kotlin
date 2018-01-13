

import rx.leancloud.core.LeanCloud;
import org.junit.Before;
import rx.leancloud.general.RxLeanCloudJavaGeneral;

public class TestBase {
    @Before
    public void setUp() {
        LeanCloud.Initialize("uay57kigwe0b6f5n0e1d4z4xhydsml3dor24bzwvzr57wdap","kfgz7jjfsk55r5a8a3y4ttd3je1ko11bkibcikonk32oozww");
        LeanCloud.toggleLog(true);
        RxLeanCloudJavaGeneral.link();
    }
}
