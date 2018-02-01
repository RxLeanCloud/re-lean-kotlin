package rx.leancloud.internal;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.function.Function;
import java.util.function.Supplier;


public class AVTaskQueue {
    public static <T> void enqueue(Single<T> single) {
        single.subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe();
    }

    public static <T, R> Single<R> once(Function<T, R> function, T t) {
        return Single.create((source) -> {
            source.onSuccess(function.apply(t));
        });
    }

    public static <T1, T2, R> Single<R> once(DoubleFunction<T1, T2, R> function, T1 t1, T2 t2) {
        return Single.create((source) -> {
            source.onSuccess(function.apply(t1, t2));
        });
    }

    public static <T1, T2, T3, R> Single<R> once(TripleFunction<T1, T2, T3, R> function, T1 t1, T2 t2, T3 t3) {
        return Single.create((source) -> {
            source.onSuccess(function.apply(t1, t2, t3));
        });
    }

    public static <T1, T2, T3, T4, R> Single<R> once(UltraFunction<T1, T2, T3, T4, R> function, T1 t1, T2 t2, T3 t3, T4 t4) {
        return Single.create((source) -> {
            source.onSuccess(function.apply(t1, t2, t3, t4));
        });
    }

    public static <T1, T2, T3, T4, T5, R> Single<R> once(PentaFunction<T1, T2, T3, T4, T5, R> function, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return Single.create((source) -> {
            source.onSuccess(function.apply(t1, t2, t3, t4, t5));
        });
    }

    public static <R> Single<R> once(Supplier<R> supplier) {
        return Single.create((source) -> {
            source.onSuccess(supplier.get());
        });
    }

    @FunctionalInterface
    public interface DoubleFunction<T1, T2, R> {
        R apply(T1 t1, T2 t2);
    }

    @FunctionalInterface
    public interface TripleFunction<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3);
    }

    @FunctionalInterface
    public interface UltraFunction<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4);
    }

    @FunctionalInterface
    public interface PentaFunction<T1, T2, T3, T4, T5, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
    }
}
