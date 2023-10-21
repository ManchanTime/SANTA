package com.gachon.santa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gachon.santa.activity.MainActivity;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.entity.PaintInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class PaintBoard {

    MyPaintView myView;
    String cacheDir;

    public PaintBoard(MyPaintView myView, String cacheDir) {
        this.myView = myView;
        this.cacheDir = cacheDir;
    }

    public void clearPicture(){
        myView.mBitmap.eraseColor(Color.WHITE);
        myView.invalidate();
    }

    public void loadPicture(String path) {
        clearPicture();
        if(BitmapFactory.decodeFile(cacheDir + "/" + path + ".jpg") != null){
            Bitmap bitmap = BitmapFactory.decodeFile(cacheDir + "/" + path + ".jpg").copy(Bitmap.Config.ARGB_8888, true);
            myView.draw(bitmap);
        }
        else{
            Toast.makeText(myView.getContext(), "저장된 그림이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void savePicture(String path) {
        myView.setDrawingCacheEnabled(true);    // 캐쉬허용
        // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
        Bitmap image = myView.getDrawingCache();
        File storage = new File(cacheDir);
        File file = new File(storage, path + ".jpg");
        OutputStream outputStream = null;
        try{
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                outputStream.close();
                myView.setDrawingCacheEnabled(false);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void storeImage(String path, String type, FirebaseUser user){
        //골뱅이 돌리기
        ProgressDialog customProgressDialog;
        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(myView.getContext());
        savePicture(path);

        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);
        Uri file = Uri.fromFile(new File(cacheDir + "/" + path + ".jpg"));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("images/"  + user.getUid() + "/" + type + "/" + new Date() + "/" + file.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("check", "fail");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl = imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null) {
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = firestore.collection("paints").document();
                            PaintInfo paint = new PaintInfo(documentReference.getId(), user.getUid(), uri.toString(), type, false, new Date());
                            documentReference.set(paint).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    customProgressDialog.cancel();
                                    customProgressDialog.dismiss();
                                    ((Activity)myView.getContext()).finishAffinity();
                                    Intent intent = new Intent(myView.getContext(), MainActivity.class);
                                    myView.getContext().startActivity(intent);
                                    Log.e("success", "success");
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
