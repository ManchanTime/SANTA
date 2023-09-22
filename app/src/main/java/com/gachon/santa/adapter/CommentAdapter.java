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
import com.gachon.santa.entity.Comments;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final ArrayList<Comments> mDataset;
    private Activity activity;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout relativeLayout;
        public CommentViewHolder(RelativeLayout v){
            super(v);
            relativeLayout = v;
        }
    }

    public CommentAdapter(Activity activity, ArrayList<Comments> myDataset){
        this.activity = activity;
        mDataset = myDataset;
        firestore = FirebaseFirestore.getInstance();
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        RelativeLayout relativeLayout =
                (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(relativeLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, @SuppressLint("RecyclerView") int position){
        RelativeLayout relativeLayout = holder.relativeLayout;
        TextView textName = relativeLayout.findViewById(R.id.text_name);
        TextView textContent = relativeLayout.findViewById(R.id.text_content);
        TextView textTime = relativeLayout.findViewById(R.id.text_time);

        documentReference = firestore.collection("users").document(mDataset.get(position).getPublisherId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    textName.setText(task.getResult().getData().get("name").toString());
                    Date date = mDataset.get(position).getCreatedAt();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
                    //원하는 데이터 포맷 지정
                    String strNowDate = simpleDateFormat.format(date);
                    textTime.setText(strNowDate);
                    textContent.setText(mDataset.get(position).getContent());
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
    }
}
