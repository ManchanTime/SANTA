package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gachon.santa.R;
import com.gachon.santa.adapter.NotificationAdapter;
import com.gachon.santa.entity.NotificationInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationInfo> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent getIntent = getIntent();
        if(getIntent != null){
            notificationList = (ArrayList<NotificationInfo>) getIntent.getSerializableExtra("object");
            recyclerView = findViewById(R.id.recycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemViewCacheSize(100);
            notificationAdapter = new NotificationAdapter(this, notificationList);
            notificationAdapter.setHasStableIds(true);
            recyclerView.setAdapter(notificationAdapter);
        }
    }
}