package rx.leancloud.internal;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.function.Function;
import java.util.function.Supplier;


public class AVTaskQueue {
    public static <T> void enqueue(Single<T> single) {
        single.subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe();
    }

    public static <T, R> Single<R> once(Function<T, R> function, T input) {
        return Single.create((source) -> {
            source.onSuccess(function.apply(input));
        });
    }

    public static <R> Single<R> once(Supplier<R> supplier) {
        return Single.create((source) -> {
            source.onSuccess(supplier.get());
        });
    }
}
