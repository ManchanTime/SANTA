package com.gachon.santa.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachon.santa.R;
import com.gachon.santa.activity.CommentActivity;
import com.gachon.santa.entity.NotificationInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private final ArrayList<NotificationInfo> mDataset;
    private Activity activity;
    private FirebaseFirestore firestore;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout relativeLayout;
        public NotificationViewHolder(RelativeLayout v){
            super(v);
            relativeLayout = v;
        }
    }

    public NotificationAdapter(Activity activity, ArrayList<NotificationInfo> myDataset){
        this.activity = activity;
        mDataset = myDataset;
        firestore = FirebaseFirestore.getInstance();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        RelativeLayout relativeLayout =
                (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.item_notification, parent, false);
        final NotificationAdapter.NotificationViewHolder notificationViewHolder = new NotificationAdapter.NotificationViewHolder(relativeLayout);

        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, @SuppressLint("RecyclerView") int position){
        RelativeLayout relativeLayout = holder.relativeLayout;
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = mDataset.get(position).getCid();
                DocumentReference documentReference = firestore.collection("comments").document(id);
                documentReference.update("read", true);
                String pid = mDataset.get(position).getPid();
                Intent intent = new Intent(activity, CommentActivity.class);
                intent.putExtra("paintId", pid);
                activity.startActivity(intent);
                removeItem(position);
            }
        });
        TextView textContent = relativeLayout.findViewById(R.id.text_content);
        TextView textTime = relativeLayout.findViewById(R.id.text_time);


        DocumentReference documentReference = firestore.collection("users").document(mDataset.get(position).getPublisherId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name = task.getResult().getData().get("name").toString();
                    textContent.setText(name + "님이 그림에 답글을 달아주셨어요!!");
                    Date date = mDataset.get(position).getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
                    //원하는 데이터 포맷 지정
                    String strNowDate = simpleDateFormat.format(date);
                    textTime.setText(strNowDate);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void removeItem(int position){
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
