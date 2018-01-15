import io.reactivex.schedulers.Schedulers;
import rx.leancloud.core.RxAVObject;
import org.junit.Test;

import java.util.Date;

public class RxAVObjectTest extends TestBase {

    @Test
    public void save() {
        RxAVObject todo = new RxAVObject("JavaTodo");
        todo.set("foo", "bar");
        todo.save();
        System.out.println(todo.getObjectId());
    }

    @Test
    public void rxSave() {
        RxAVObject todo = new RxAVObject("JavaTodo");
        todo.set("foo", "rxBar");
        todo.set("fooDate",new Date());
        todo.saveRx().observeOn(Schedulers.computation()).subscribe(System.out::println, Throwable::printStackTrace);
    }

    @Test
    public void saveAsync() {
        RxAVObject todo = new RxAVObject("JavaTodo");
        todo.set("foo", "asyncBar");
        todo.saveAsync();
    }
}