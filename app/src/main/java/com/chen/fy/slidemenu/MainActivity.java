package com.chen.fy.slidemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    private ArrayList<MyBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        initData();
        adapter = new MyAdapter(list,this);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 0; i < 50; i++) {
            MyBean bean = new MyBean("Context" + i);
            list.add(bean);
        }
    }
}
