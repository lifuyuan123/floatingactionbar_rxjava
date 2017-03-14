package azsecuer.zhuoxin.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Future;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    Subscriber<String> s;
    private TextView t;
    String s3="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         t= (TextView) findViewById(R.id.text);
//        Observer 即观察者，它决定事件触发的时候将有怎样的行为。
        Observer<String> o=new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String s) {

            }
        };
//        除了 Observer 接口之外，RxJava 还内置了一个实现了 Observer 的抽象类：Subscriber。
// Subscriber 对 Observer 接口进行了一些扩展，但他们的基本使用方式是完全一样的：
        s=new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String s) {
                           s3+=s;
                           Log.i("onnext",s);

            }
        };
//        s.onStart();
//        s.isUnsubscribed();
//        s.unsubscribe();

        //    Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件。
// RxJava 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则：
        Observable<String> ob=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
//        可以看到，这里传入了一个 OnSubscribe 对象作为参数。OnSubscribe 会被存储在返回的 Observable 对象中，
// 它的作用相当于一个计划表，当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，
// 事件序列就会依照设定依次触发（对于上面的代码，就是观察者Subscriber 将会被调用三次 onNext() 和一次 onCompleted()）。
// 这样，由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式。
            }
        });

//    以下为效果相同的快捷的方法  just方法
        Observable<String> objust=Observable.just("1","2","3");
        // 将会依次调用：
// onNext("Hello");
// onNext("Hi");
// onNext("Aloha");
// onCompleted();

        //from方法  from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        String[] ss={"1","2","3"};
        Observable<String> obfrom=Observable.from(ss);
        // 将会依次调用：
// onNext("Hello");
// onNext("Hi");
// onNext("Aloha");
// onCompleted();


//    创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，
// 整条链子就可以工作了。代码形式很简单：
//        ob.subscribe(o);
//        或
        ob.subscribe(s);
        t.setText(s3);


        //简便方法打印321
//        String [] names={"3","2","1"};
//        Observable.from(names).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                s3+=s;
//            }
//        });
//        t.setText(s3);



//        在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，
// 就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，
// 就需要用到 Scheduler （调度器）。
//        1) Scheduler 的 API (一)
//        在RxJava 中，Scheduler ——调度器，相当于线程控制器，RxJava 通过它来指定每一段代码应该运行在什么样的线程。
//        RxJava 已经内置了几个 Scheduler ，它们已经适合大多数的使用场景：
//        Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
//        Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
//        Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多
//                        ，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比
//                        newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
//        Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，
//                        例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation()
//                        中，否则 I/O 操作的等待时间会浪费 CPU。
//        AndroidSchedulers.mainThread()：另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android
//                        主线程运行。有了这几个 Scheduler ，就可以使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制了。
//       * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。
//        或者叫做事件产生的线程。 * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。


    }


    @Override
    protected void onStop() {
        super.onStop();
        s.unsubscribe();
    }
}
