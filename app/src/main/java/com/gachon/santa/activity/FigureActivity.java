package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.gachon.santa.R;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FigureActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_figure);

        Intent intent = getIntent();
        String figure = intent.getStringExtra("figure");
        setFigure(figure);

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
        switch(v.getId()){
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
            case R.id.btnComplete:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                paintBoard.storeImage(path, path, user);
                finishAffinity();
                break;
        }
    };

    public void setFigure(String figure){
        switch (figure){
            case "square":
                findViewById(R.id.image_square).setVisibility(View.VISIBLE);
                break;

            case "triangle":
                findViewById(R.id.image_triangle).setVisibility(View.VISIBLE);
                break;

            case "circle":
                findViewById(R.id.image_circle).setVisibility(View.VISIBLE);
                break;
        }
    }

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