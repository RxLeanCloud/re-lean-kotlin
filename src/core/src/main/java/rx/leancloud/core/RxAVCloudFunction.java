package rx.leancloud.core;

import io.reactivex.Single;
import rx.leancloud.internal.AVTaskQueue;

import java.util.Map;
import java.util.function.Function;

public class RxAVCloudFunction<T, R> {

    public String functionName;

    public Function<T, Map<String, Object>> encoder;

    public Function<Map<String, Object>, R> decoder;

    public R rpc(T payload) {
        Map<String, Object> postData = encoder.apply(payload);
        Map<String, Object> resultData = RxAVClient.getInstance().runCommand("/call/" + this.functionName, "POST", postData);
        R result = decoder.apply(resultData);
        return result;
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

    public static Single<Map<String, Object>> callRx(String functionName, Map<String, Object> payload) {
        RxAVCloudFunction<Map<String, Object>, Map<String, Object>> cloudFunction = new RxAVCloudFunction<>();

        cloudFunction.functionName = functionName;

        return AVTaskQueue.once(cloudFunction::call, payload);
    }


    public static <T, R> R rpc(String functionName, T payload, Function<T, Map<String, Object>> encoder, Function<Map<String, Object>, R> decoder) {
        RxAVCloudFunction<T, R> cloudFunction = new RxAVCloudFunction<>();

        cloudFunction.functionName = functionName;
        cloudFunction.encoder = encoder;
        cloudFunction.decoder = decoder;
        R result = cloudFunction.rpc(payload);

        return result;
    }

    public static <T, R> Single<R> rpcRx(String functionName, T payload, Function<T, Map<String, Object>> encoder, Function<Map<String, Object>, R> decoder) {
        RxAVCloudFunction<T, R> cloudFunction = new RxAVCloudFunction<>();

        cloudFunction.functionName = functionName;
        cloudFunction.encoder = encoder;
        cloudFunction.decoder = decoder;

        return AVTaskQueue.once(cloudFunction::rpc, payload);
    }

}
