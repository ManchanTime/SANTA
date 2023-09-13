package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

//그림 선택하기
public class FigureChooseActivity extends AppCompatActivity {

    private MyPaintView myView;
    private PaintBoard paintBoard;
    private Button btnTh, btnClear, btnSave, btnLoad, btnComplete;
    int thickness = 0;

    private final String path = "figure";


    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figure_choose);


        Button btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(onClickListener);

        ImageView figureSquare = findViewById(R.id.image_square);
        figureSquare.setOnClickListener(onClickListener);

        ImageView figureTriangle = findViewById(R.id.image_triangle);
        figureTriangle.setOnClickListener(onClickListener);

        ImageView figureCircle = findViewById(R.id.image_circle);
        figureCircle.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = (v) -> {
        Intent intent;
        switch(v.getId()){
            case R.id.button_next:
                intent = new Intent(this, FigureActivity.class);
                startActivity(intent);
                break;
            case R.id.image_square:
                intent = new Intent(this, FigureActivity.class);
                intent.putExtra("figure", "square");
                startActivity(intent);
                break;

            case R.id.image_triangle:
                intent = new Intent(this, FigureActivity.class);
                intent.putExtra("figure", "triangle");
                startActivity(intent);
                break;

            case R.id.image_circle:
                intent = new Intent(this, FigureActivity.class);
                intent.putExtra("figure", "circle");
                startActivity(intent);
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
        btnComplete.setOnClickListener(onClickListener);
    }
}