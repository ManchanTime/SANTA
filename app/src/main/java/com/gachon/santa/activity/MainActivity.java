package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gachon.santa.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFigure = findViewById(R.id.button_figure);
        btnFigure.setOnClickListener(onClickListener);

        Button btnPaintBoard = findViewById(R.id.button_paintBoard);
        btnPaintBoard.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()){
            case R.id.button_figure:
                Intent intent = new Intent(this, FigureExplainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_paintBoard:
                Intent intent1 = new Intent(this, PaintBoardActivity.class);
                startActivity(intent1);
                break;
        }
    };
}