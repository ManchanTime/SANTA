package com.gachon.santa.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.gachon.santa.R;
import com.gachon.santa.util.BasicFunctions;

import java.util.Locale;

public class IntroActivity extends BasicFunctions {

    private TextToSpeech tts;
    private Button btnNext;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        text = "어르신의 행복하고 활력있는 삶을 그림 그리기를 통해 찾아드리겠습니다!!";
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(onClickListener);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                    tts.setPitch(1.0f);
                    tts.setSpeechRate(1.0f);
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    View.OnClickListener onClickListener = (v) -> {
        switch(v.getId()){
            case R.id.btn_next:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    };

    @Override public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Dialog dialog;
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.show();

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
    }
}