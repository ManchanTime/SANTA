package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.gachon.santa.R;
import com.gachon.santa.fragment.PaintBoardFragment;
import com.gachon.santa.util.BasicFunctions;
import com.gachon.santa.util.MyPaintView;
import com.gachon.santa.util.PaintBoard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HtpActivity extends BasicFunctions {

    private MyPaintView myView;
    private PaintBoard paintBoard;
    private final String path = "htp";
    private Fragment paintBoardFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htp);

        setTitle("간단 그림판");
        myView = new MyPaintView(getApplicationContext());
        paintBoard = new PaintBoard(myView, getCacheDir().toString());
        paintBoardFragment = new PaintBoardFragment(getCacheDir().toString(), path);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container,paintBoardFragment).commit();
    }
}