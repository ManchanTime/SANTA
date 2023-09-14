package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gachon.santa.R;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LmtChooseActivity extends AppCompatActivity {
    int cnt = 0;
    List<String> list = new ArrayList<>();
    TextView text;

    private final String path = "lmt";

    private MyPaintView myView;
    private PaintBoard paintBoard;
    private Button btnTh, btnClear, btnSave, btnLoad, btnComplete;
    int thickness = 0;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmt_choose);

        text = findViewById(R.id.notice);
        list.add("나무");
        list.add("산");
        list.add("바위");

        text.setText(list.get(cnt));

        setTitle("간단 그림판");
        myView = new MyPaintView(this);
        paintBoard = new PaintBoard(myView, getCacheDir().toString());

        init();

        ((LinearLayout) findViewById(R.id.paintLayout)).addView(myView);
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId) {
                            case R.id.btnRed:
                                myView.mPaint.setColor(Color.RED);
                                break;
                            case R.id.btnGreen:
                                myView.mPaint.setColor(Color.GREEN);
                                break;
                            case R.id.btnBlue:
                                myView.mPaint.setColor(Color.BLUE);
                                break;
                            case R.id.btnErase:
                                myView.mPaint.setColor(Color.WHITE);
                                break;
                        }
                    }
                });
    }

    View.OnClickListener onClickListener = (v) -> {
        Intent intent;
        switch (v.getId()){
            case R.id.btnComplete:
                if(btnComplete.getText().equals("저장"))
                {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    paintBoard.storeImage(path, path, user);
                    finishAffinity();
                    break;
                }
                cnt++;
                text.setText(list.get(cnt));
                if(cnt == list.size()-1)
                {
                    btnComplete.setText("저장");
                    break;
                }
                break;
            case R.id.btnTh:
                if(thickness % 2 == 1){
                    btnTh.setText("Thin");
                    myView.mPaint.setStrokeWidth(10);
                } else {
                    btnTh.setText("Thick");
                    myView.mPaint.setStrokeWidth(20);
                }
                thickness++;
                break;
            case R.id.btnClear:
                paintBoard.clearPicture();
                break;
            case R.id.btnSave:
                paintBoard.savePicture(path);
                break;
            case R.id.btnLoad:
                paintBoard.loadPicture(path);
                break;
        }
    };

    private void init(){
        btnTh = findViewById(R.id.btnTh);
        btnTh.setOnClickListener(onClickListener);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(onClickListener);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);
        btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(onClickListener);
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setText("다음으로");
        btnComplete.setOnClickListener(onClickListener);
    }

}