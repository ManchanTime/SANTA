package com.gachon.santa.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gachon.santa.R;
import com.gachon.santa.entity.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    TextView etName, etPwd, etPwdCh, etAge, etAddress;
    Spinner spSex;
    Button btnComp;
    String[] items = {"남자", "여자"};
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.et_name);
        etPwd = findViewById(R.id.et_pwd);
        etPwdCh = findViewById(R.id.et_check);
        etAge = findViewById(R.id.et_age);
        spSex = findViewById(R.id.sp_sex);
        spSex.setSelection(0);
        etAddress = findViewById(R.id.et_address);
        btnComp = findViewById(R.id.btn_complete);
        btnComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(sex);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSex.setAdapter(adapter);
        spSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sex = items[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }


    public void register(String sex){
        String name = etName.getText().toString();
        String pwd = etPwd.getText().toString();
        String chk = etPwdCh.getText().toString();
        String age = etAge.getText().toString();
        String address = etAddress.getText().toString();
        if(!pwd.equals(chk)){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(name + "@santa.com", pwd)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                            Member member = new Member(user.getUid(), name, age, sex, address, "user");
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection("users").document(user.getUid())
                                    .set(member)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RegisterActivity.this, "회원가입 성공!!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }
                        else{
                            Log.e("error","error");
                        }
                    }
                });
    }
}