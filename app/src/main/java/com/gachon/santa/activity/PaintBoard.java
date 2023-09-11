package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gachon.santa.R;
import com.gachon.santa.util.MyPaintView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.C;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PaintBoard extends AppCompatActivity {

    private MyPaintView myView;
    private Button btnTh, btnClear, btnSave, btnLoad, btnComplete;
    int thickness = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_board);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        // signInAnonymously를 호출하여 익명 사용자로 로그인
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(getApplicationContext(), "익명 인증 성공", Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("test", task.getException().toString());
                    Toast.makeText(getApplicationContext(), "익명 인증 실패", Toast.LENGTH_LONG).show();
                }
            }
        });

        setTitle("간단 그림판");
        myView = new MyPaintView(this);

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
                clearPicture();
                break;
            case R.id.btnSave:
                savePicture();
                break;
            case R.id.btnLoad:
                loadPicture();
                break;
            case R.id.btnComplete:
                storeImage();
                break;
        }
    };

    private void clearPicture(){
        myView.mBitmap.eraseColor(Color.WHITE);
        myView.invalidate();
    }

    private void loadPicture() {
        clearPicture();
        Bitmap bitmap = BitmapFactory.decodeFile(getCacheDir() + "/test.jpg").copy(Bitmap.Config.ARGB_8888, true);
        myView.draw(bitmap);
        Toast.makeText(getApplicationContext(), "로딩 완료", Toast.LENGTH_SHORT).show();
    }

    private void savePicture() {
        myView.setDrawingCacheEnabled(true);    // 캐쉬허용
        // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
        Bitmap image = myView.getDrawingCache();
        Log.e("save", image.toString());
        File storage = getCacheDir();
        File file = new File(storage, "test.jpg");
        OutputStream outputStream = null;
        try{
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                outputStream.close();
                myView.setDrawingCacheEnabled(false);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeImage(){
        Uri file = Uri.fromFile(new File(getCacheDir() + "/test.jpg"));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference riversRef = storageRef.child("images/"+ file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(PaintBoard.this, "저장 실패", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PaintBoard.this, "저장 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }

}