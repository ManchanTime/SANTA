package com.gachon.santa.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gachon.santa.activity.CommentActivity;
import com.gachon.santa.activity.MainActivity;
import com.gachon.santa.entity.PaintInfo;

import java.util.ArrayList;

public class MyPaintAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PaintInfo> dataset;

    public MyPaintAdapter(Context c, ArrayList<PaintInfo> dataset){
        context = c;
        this.dataset = dataset;
        Log.e("test", dataset.size()+"");
    }

    // BaseAdapter를 상속받은 클래스가 구현해야 할 함수들은
    // { getCount(), getItem(), getItemId(), getView() }
    // Ctrl + i 를 눌러 한꺼번에 구현할 수 있습니다.
    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return dataset.get(i).hashCode();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400,400));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(5,5,5,5);
        Glide.with(context).load(dataset.get(i).getUrl()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
        return imageView;
    }
}
