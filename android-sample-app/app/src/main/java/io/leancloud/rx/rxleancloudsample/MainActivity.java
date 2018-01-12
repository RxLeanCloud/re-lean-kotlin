package io.leancloud.rx.rxleancloudsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rx.leancloud.android.RxLeanCloudJavaAndroid;
import rx.leancloud.core.LeanCloud;
import rx.leancloud.core.RxAVObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LeanCloud.Initialize("uay57kigwe0b6f5n0e1d4z4xhydsml3dor24bzwvzr57wdap", "kfgz7jjfsk55r5a8a3y4ttd3je1ko11bkibcikonk32oozww");
        LeanCloud.toggleLog(true);
        RxLeanCloudJavaAndroid.link();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonClick = (Button) findViewById(R.id.button1);

        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxAVObject todo = new RxAVObject("JavaTodo");
                todo.set("foo", "bar");
                todo.save();
                System.out.println(todo.getObjectId());
            }
        });
    }
}
