package com.chen.fy.slidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<MyBean> list;
    private Context context;

    MyAdapter(ArrayList<MyBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main, null);
        //传入接口
        SlideFrameLayout slideFrameLayout = (SlideFrameLayout) view;
        slideFrameLayout.setSlideChangeListener(new MyOnSlideChangeListener());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MyBean myBean = list.get(i);
        String info = myBean.getName();
        viewHolder.tv_context.setText(info);
        viewHolder.tv_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(myBean);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_context;
        private TextView tv_menu;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_context = itemView.findViewById(R.id.tv_context);
            tv_menu = itemView.findViewById(R.id.tv_menu);
        }
    }

    private SlideFrameLayout slideFrameLayout;

    class MyOnSlideChangeListener implements SlideFrameLayout.onSlideChangeListener {

        @Override
        public void onOpen(SlideFrameLayout layout) {
            slideFrameLayout = layout;
        }

        @Override
        public void onClose(SlideFrameLayout layout) {
            if (slideFrameLayout == layout) {
                slideFrameLayout = null;
            }
        }

        @Override
        public void onDown(SlideFrameLayout layout) {
            if (slideFrameLayout != null && slideFrameLayout != layout) {
                slideFrameLayout.closeMenu();
            }
        }
    }
}
