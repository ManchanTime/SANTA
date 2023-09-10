package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gachon.santa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etId, etPwd;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = findViewById(R.id.et_id);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(onClickListener);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = (v) -> {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_login:
                validate();
                break;
            case R.id.btn_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    };

    public void validate(){
        String email = etId.getText().toString() + "@santa.com";
        String pwd = etPwd.getText().toString();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "환영합니다!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}