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

import com.gachon.santa.R;
import com.gachon.santa.fragment.PaintBoardFragment;
import com.gachon.santa.util.BasicFunctions;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FigureActivity extends BasicFunctions {

    private MyPaintView myView;
    private PaintBoard paintBoard;
    private Fragment paintBoardFragment;
    private LinearLayout layoutFigure;

    private final String path = "figure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figure);

        layoutFigure = findViewById(R.id.layout_figure);
        Intent intent = getIntent();
        String figure = intent.getStringExtra("figure");
        setFigure(figure);

        myView = new MyPaintView(getApplicationContext());
        paintBoard = new PaintBoard(myView, getCacheDir().toString());
        paintBoardFragment = new PaintBoardFragment(getCacheDir().toString(), path);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container,paintBoardFragment).commit();

    }
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
}