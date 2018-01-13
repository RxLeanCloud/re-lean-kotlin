package rx.leancloud.internal;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class AVTaskQueue {
    public static <T> void enqueue(Single<T> single) {
        single.subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe();
    }
}
