package com.gachon.santa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FigureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figure);

        Button btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = (v) -> {
        switch(v.getId()){
            case R.id.button_next:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                //액티비티 스택 초기화
                finishAffinity();
        }
    };
}