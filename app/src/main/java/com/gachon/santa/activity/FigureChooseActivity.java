package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gachon.santa.R;

public class FigureChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figure_choose);

        Button btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(onClickListener);

        ImageView imageFigure = findViewById(R.id.image_square);
        imageFigure.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = (v) -> {
        Intent intent = new Intent(this, FigureActivity.class);
        switch(v.getId()){
            case R.id.button_next:
                startActivity(intent);
                break;
            case R.id.image_square:
                intent.putExtra("figure", "square");
                startActivity(intent);
                break;
        }
    };
}