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

        Button btnHTP = findViewById(R.id.button_htp);
        btnHTP.setOnClickListener(onClickListener);

        Button btnLMT = findViewById(R.id.button_lmt);
        btnLMT.setOnClickListener(onClickListener);

        Button btnPITR = findViewById(R.id.button_pitr);
        btnPITR.setOnClickListener(onClickListener);

        Button btnPaintBoard = findViewById(R.id.button_paintBoard);
        btnPaintBoard.setOnClickListener(onClickListener);


    }

    View.OnClickListener onClickListener = (v) -> {
        Intent intent;
        switch (v.getId()){
            case R.id.button_figure:
                intent = new Intent(this, FigureExplainActivity.class);
                startActivity(intent);
                break;

            case R.id.button_htp:
                intent = new Intent(this, HtpExampleActivity.class);
                startActivity(intent);
                break;

            case R.id.button_lmt:
                intent = new Intent(this, LmtExampleActivity.class);
                startActivity(intent);
                break;

            case R.id.button_pitr:
                intent = new Intent(this, PitrExampleActivity.class);
                startActivity(intent);
                break;

            case R.id.button_paintBoard:
                intent = new Intent(this, PaintBoard.class);
                startActivity(intent);
                break;

        }
    };
}