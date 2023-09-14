package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gachon.santa.R;

public class LmtExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmt_example);

        Button btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LmtExampleActivity.this, LmtChooseActivity.class);
                startActivity(intent);
            }
        });
    }
}