package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gachon.santa.R;
import com.gachon.santa.adapter.NotificationAdapter;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.entity.NotificationInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationInfo> notificationList = new ArrayList<>();
    private RelativeLayout layoutNoSearch;
    private ImageView image_reload;
    //골뱅이 돌리기
    private ProgressDialog customProgressDialog;

    @Override
    protected void onResume(){
        super.onResume();
        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);
        if(notificationList.isEmpty()){
            layoutNoSearch.setVisibility(View.VISIBLE);
        }
        else{
            layoutNoSearch.setVisibility(View.GONE);
            recyclerView = findViewById(R.id.recycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemViewCacheSize(100);
            notificationAdapter = new NotificationAdapter(this, notificationList);
            notificationAdapter.setHasStableIds(true);
            recyclerView.setAdapter(notificationAdapter);
        }
        customProgressDialog.cancel();
        customProgressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layoutNoSearch = findViewById(R.id.layout_no_search);
        image_reload = findViewById(R.id.image_reload);
        image_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();//인텐트 종료
                overridePendingTransition(0, 0);//인텐트 효과 없애기
                Intent intent = getIntent(); //인텐트
                startActivity(intent); //액티비티 열기
                overridePendingTransition(0, 0);//인텐트 효과 없애기
            }
        });
        Intent getIntent = getIntent();
        if(getIntent != null){
            notificationList = (ArrayList<NotificationInfo>) getIntent.getSerializableExtra("object");
        }else{
            Log.e(this + " ", "error");
        }
    }
}