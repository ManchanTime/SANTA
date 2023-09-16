package com.gachon.santa.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gachon.santa.R;
import com.gachon.santa.util.BasicFunctions;

public class HtpExampleActivity extends BasicFunctions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htp_example);

        Button btnNext = findViewById(R.id.button_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HtpExampleActivity.this, HtpActivity.class);
                startActivity(intent);
            }
        });
    }
}