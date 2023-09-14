package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gachon.santa.R;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PaintBoardActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private MyPaintView myView;
    private Button btnTh, btnClear, btnSave, btnLoad, btnComplete;
    private int thickness = 0;
    private PaintBoard testView;
    private final String type = "test";
    private final String path = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_board);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        setTitle("간단 그림판");
        myView = new MyPaintView(this);
        testView = new PaintBoard(myView, getCacheDir().toString());

        btnTh = findViewById(R.id.btnTh);
        btnTh.setOnClickListener(onClickListener);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(onClickListener);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);
        btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(onClickListener);
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(onClickListener);

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
        switch (v.getId()){
            case R.id.btnTh:
                changePontSize();
                break;
            case R.id.btnClear:
                //clearPicture();
                testView.clearPicture();
                break;
            case R.id.btnSave:
                //savePicture();
                testView.savePicture(path);
                break;
            case R.id.btnLoad:
                //loadPicture();
                testView.loadPicture(path);
                break;
            case R.id.btnComplete:
                testView.storeImage(path, type, user);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(this, "업로드 성공!!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                //storeImage();
                break;
        }
    };

    private void changePontSize() {
        if (thickness % 2 == 1) {
            btnTh.setText("Thin");
            myView.mPaint.setStrokeWidth(10);
        } else {
            btnTh.setText("Thick");
            myView.mPaint.setStrokeWidth(20);
        }
        thickness++;
    }
}