package azsecuer.zhuoxin.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class Main2Activity extends AppCompatActivity {
    TextView textView;
    List<User> userList;
    RecyclerView r;
    FloatingActionButton f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView= (TextView) findViewById(R.id.text2);
        r= (RecyclerView) findViewById(R.id.recycler);
        f= (FloatingActionButton) findViewById(R.id.floating);
        final List<String> lists=new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            lists.add(""+i);
        }
        Myadapter adapter=new Myadapter(lists,this);
        r.setLayoutManager(new LinearLayoutManager(this));//少了这一步都不能显示
        r.setAdapter(adapter);

         f.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Main2Activity.this,MainActivity.class));
             }
         });

        initdata();
//        Observable.create(new Observable.OnSubscribe<List<User>>() {
//            @Override
//            public void call(Subscriber<? super List<User>> subscriber) {
//                  subscriber.onNext(userList);//发送数据
//            }
//        }).subscribe(new Action1<List<User>>() {//订阅
//            @Override
//            public void call(List<User> users) {
//                //得到数据
//                textView.setText(users.toString());
//            }
//        });
        Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                subscriber.onNext(userList);//发送数据
            }
        }).flatMap(new Func1<List<User>, Observable<User>>() {//.map返回的是数据对象。而flatmap返回的是Observable
            @Override
            public Observable<User> call(List<User> users) {
                return Observable.from(users);//返回一个users
            }
        }).filter(new Func1<User, Boolean>() {
            @Override
            public Boolean call(User user) {
                return user.getName().equals("3");//过滤器  只保留满足此条件的数据
            }
        }).subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                Log.i("CALL",user.getName());
             textView.setText(user.getName());//处理数据
            }
        });


    }

    private void initdata() {
        userList=new ArrayList<>();
        for (int i = 0; i <10; i++) {
            User u=new User(i+"",2*i+"",3*i+"");
            userList.add(u);
        }
    }

    class Myadapter extends RecyclerView.Adapter{
        private List<String> list;
        private Context context;

        public Myadapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Myviewholder(LayoutInflater.from(context).
                    inflate(R.layout.stringitem,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String s = list.get(position);
            ( (Myviewholder)holder).t.setText(s);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Myviewholder extends RecyclerView.ViewHolder{
            TextView t;
            public Myviewholder(View itemView) {
                super(itemView);
                t= (TextView) itemView.findViewById(R.id.texts);

            }
        }
    }


}
