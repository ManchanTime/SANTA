package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.gachon.santa.R;
import com.gachon.santa.adapter.MyPaintAdapter;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.entity.PaintInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MyPaintActivity extends AppCompatActivity {

    private ArrayList<PaintInfo> paintList = new ArrayList<>();
    private GridView gridPaint;
    private MyPaintAdapter myPaintAdapter;
    //골뱅이 돌리기
    private ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_paint);

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadPaints();
    }

    public void loadPaints(){
        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("paints");
        assert user != null;
        Log.e("userId", user.getUid());
        collectionReference
                .whereEqualTo("uid", user.getUid())
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.e("te","Test");
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.e("test", document.getData().get("uid").toString());
                                paintList.add(new PaintInfo(
                                        document.getData().get("pid").toString(),
                                        document.getData().get("uid").toString(),
                                        document.getData().get("url").toString(),
                                        document.getData().get("type").toString(),
                                        new Date(document.getDate("date").getTime())
                                    )
                                );
                            }
                            gridPaint = findViewById(R.id.grid_paint);
                            myPaintAdapter = new MyPaintAdapter(MyPaintActivity.this, paintList);
                            gridPaint.setAdapter(myPaintAdapter);
                            customProgressDialog.cancel();
                            customProgressDialog.dismiss();
                        }else{
                            Log.e("te",task.getException().toString());
                        }
                    }
                });
    }
}