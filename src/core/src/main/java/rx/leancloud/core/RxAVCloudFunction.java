package rx.leancloud.core;

import io.reactivex.Single;
import rx.leancloud.internal.AVTaskQueue;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class RxAVCloudFunction<T, R> {

    public String functionName;

    public Function<T, Map<String, Object>> encoder;

    public Function<Map<String, Object>, R> decoder;

    protected static <T> Function<T, Map<String, Object>> getStringEncoder() {
        Function<T, Map<String, Object>> encoder = (t) -> {
            String jsonString = RxAVCorePlugins.getInstance().getJson().encode(t);
            Map<String, Object> jsonMap = (Map<String, Object>) RxAVCorePlugins.getInstance().getJson().parse(jsonString);
            return jsonMap;
        };
        return encoder;
    }

    public static <T> Function<T, Map<String, Object>> getDefaultEncoder() {
        return RxAVCloudFunction.getStringEncoder();
    }

    protected static <R> Function<Map<String, Object>, R> getStringDecoder() {
        Function<Map<String, Object>, R> decoder = (m) -> {
            String jsonString = RxAVCorePlugins.getInstance().getJson().encode(m);
            R result = (R) RxAVCorePlugins.getInstance().getJson().parse(jsonString);
            return result;
        };
        return decoder;
    }

    public static <R> Function<Map<String, Object>, R> gerDefaultDecoder() {
        return RxAVCloudFunction.getStringDecoder();
    }

    public Map<String, Object> call(Map<String, Object> payload) {

        Map<String, Object> resultData = RxAVClient.getInstance().runCommand("/functions/" + this.functionName, "POST", payload);
        return resultData;
    }

    public static Map<String, Object> call(String functionName, Map<String, Object> payload) {

        RxAVCloudFunction<Map<String, Object>, Map<String, Object>> cloudFunction = new RxAVCloudFunction<>();
        cloudFunction.functionName = functionName;
        Map<String, Object> result = cloudFunction.call(payload);
        return result;
    }

    public static Map<String, Object> call(String functionName) {
        return RxAVCloudFunction.call(functionName, null);
    }

    public static Single<Map<String, Object>> callRx(String functionName, Map<String, Object> payload) {
        return AVTaskQueue.once(RxAVCloudFunction::call, functionName, payload);
    }

    public static Single<Map<String, Object>> callRx(String functionName) {
        return RxAVCloudFunction.callRx(functionName, null);
    }

    public R rpc(T payload) {
        Map<String, Object> postData = null;
        if (payload != null && encoder != null) {
            postData = encoder.apply(payload);
        }
        Map<String, Object> resultData = RxAVClient.getInstance().runCommand("/call/" + this.functionName, "POST", postData);
        R result = decoder.apply(resultData);
        return result;
    }

    public static <T, R> R rpc(String functionName, T payload, Function<T, Map<String, Object>> encoder, Function<Map<String, Object>, R> decoder) {
        RxAVCloudFunction<T, R> cloudFunction = new RxAVCloudFunction<>();

        cloudFunction.functionName = functionName;
        cloudFunction.encoder = encoder;
        cloudFunction.decoder = decoder;
        R result = cloudFunction.rpc(payload);

        return result;
    }

    public static <R> R rpc(String functionName, Function<Map<String, Object>, R> decoder) {
        return RxAVCloudFunction.rpc(functionName, null, null, decoder);
    }

    public static <R> R rpc(String functionName) {
        return RxAVCloudFunction.rpc(functionName, RxAVCloudFunction.gerDefaultDecoder());
    }

    public static <T, R> Single<R> rpcRx(String functionName, T payload, Function<T, Map<String, Object>> encoder, Function<Map<String, Object>, R> decoder) {
        return AVTaskQueue.once(RxAVCloudFunction::rpc, functionName, payload, encoder, decoder);
    }

    public static <R> Single<R> rpcRx(String functionName, Function<Map<String, Object>, R> decoder) {
        return RxAVCloudFunction.rpcRx(functionName, null, null, decoder);
    }

    public static <R> Single<R> rpcRx(String functionName) {
        return RxAVCloudFunction.rpcRx(functionName, RxAVCloudFunction.gerDefaultDecoder());
    }
}
