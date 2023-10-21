package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gachon.santa.R;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.util.BasicFunctions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends BasicFunctions {

    private EditText etId, etPwd;
    private Button btnLogin, btnRegister;
    //골뱅이 돌리기
    private ProgressDialog customProgressDialog;

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

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();//데이터베이스의 인스턴스를 가져온다. (즉, root를 가져온다.)

        firebaseAuth.signInWithEmailAndPassword(email, pwd)//메일이랑 패스워드를 참조
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            DocumentReference documentReference = firestore.collection("users")
                                    .document(user.getUid());
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    customProgressDialog.cancel();
                                    customProgressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        String type = task.getResult().getData().get("type").toString();
                                        if(type.equals("user")){
                                            Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(LoginActivity.this, "환영합니다!!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Log.e("error", task.getException().toString());
                                    }
                                }
                            });
                        }
                        else
                            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        customProgressDialog.cancel();
                        customProgressDialog.dismiss();
                    }
                });
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