package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gachon.santa.R;
import com.gachon.santa.fragment.PaintBoardFragment;
import com.gachon.santa.util.BasicFunctions;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class LmtActivity extends BasicFunctions {
    int cnt = 0;
    ArrayList<String> list = new ArrayList<>();
    TextView text;

    private final String path = "lmt";
    private Fragment paintBoardFragment;
    private MyPaintView myView;
    private PaintBoard paintBoard;
    int thickness = 0;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmt);


        text = findViewById(R.id.notice);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("lmt_data_list").document("list");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot != null){
                    list = (ArrayList<String>)documentSnapshot.getData().get("items");
                    setTitle("간단 그림판");
                    myView = new MyPaintView(getApplicationContext());
                    paintBoard = new PaintBoard(myView, getCacheDir().toString());
                    paintBoardFragment = new PaintBoardFragment(getCacheDir().toString(), path, list);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.fragment_container,paintBoardFragment).commit();
                }
            }
        });
    }
}