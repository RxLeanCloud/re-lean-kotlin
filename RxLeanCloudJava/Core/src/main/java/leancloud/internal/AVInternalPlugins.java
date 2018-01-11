package leancloud.internal;

import leancloud.core.RxAVCorePlugins;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class AVInternalPlugins {

    private static AVInternalPlugins ourInstance = new AVInternalPlugins();

    public static AVInternalPlugins getInstance() {
        return ourInstance;
    }

    private AVObjectController objectController;

    public AVObjectController getObjectController() {
        if (objectController == null) {
            this.objectController = new AVObjectController(this.getAVEncoder(), this.getAVDecoder(), this.getHttpCommandRunner());
        }
        return this.objectController;
    }

    private IAVEncoder avEncoder;

    public IAVEncoder getAVEncoder() {
        if (avEncoder == null) {
            this.avEncoder = AVEncoder.getInstance();
        }
        return avEncoder;
    }

    private IAVDecoder avDecoder;

    public IAVDecoder getAVDecoder() {
        if (avDecoder == null) {
            this.avDecoder = AVDecoder.getInstance();
        }
        return avDecoder;
    }

    private IAVCommandRunner httpCommandRunner;

    public IAVCommandRunner getHttpCommandRunner() {
        if (httpCommandRunner == null) {
            this.httpCommandRunner = new AVHttpCommandRunner(RxAVCorePlugins.getInstance().getHttpClient());
        }
        return httpCommandRunner;
    }

    public DateFormat getAVDateFormatter() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df;
    }
}
