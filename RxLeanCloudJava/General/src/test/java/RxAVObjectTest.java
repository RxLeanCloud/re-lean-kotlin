import leancloud.core.RxAVObject;
import org.junit.Test;

public class RxAVObjectTest extends TestBase {

    @Test
    public void save() {
        RxAVObject todo = new RxAVObject("JavaTodo");
        todo.set("foo", "bar");
        todo.save();
        System.out.println(todo.getObjectId());
    }
}