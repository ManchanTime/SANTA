package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gachon.santa.R;
import com.gachon.santa.adapter.CommentAdapter;
import com.gachon.santa.adapter.MyPaintAdapter;
import com.gachon.santa.adapter.NotificationAdapter;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.entity.Comments;
import com.gachon.santa.entity.NotificationInfo;
import com.gachon.santa.entity.PaintInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    private ImageView imagePaint;
    private FirebaseFirestore firestore;
    private CollectionReference collectionReference;
    //골뱅이 돌리기
    private ProgressDialog customProgressDialog;
    private ArrayList<Comments> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private String paintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Intent getIntent = getIntent();
        if(getIntent != null) {
            imagePaint = findViewById(R.id.image_paint);
            paintId = getIntent.getStringExtra("paintId");
            recyclerView = findViewById(R.id.recycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemViewCacheSize(100);
            commentAdapter = new CommentAdapter(this, commentList);
            commentAdapter.setHasStableIds(true);
            loadComment();
        }
    }

    private void loadComment(){
        commentList.clear();
        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("comments");
        collectionReference
                .whereEqualTo("pid", paintId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                commentList.add(new Comments(
                                                document.getData().get("pid").toString(),
                                                document.getData().get("uid").toString(),
                                                document.getData().get("content").toString(),
                                                document.getData().get("url").toString(),
                                                new Date(document.getDate("createdAt").getTime())
                                        )
                                );
                            }
                            Log.e("size",commentList.size()+"");
                            Glide.with(getApplicationContext()).load(commentList.get(0).getUrl()).into(imagePaint);
                            recyclerView.setAdapter(commentAdapter);
                        }
                        else{
                            Log.e("error", task.getException().toString());
                        }
                        customProgressDialog.cancel();
                        customProgressDialog.dismiss();
                    }
                });
    }

}