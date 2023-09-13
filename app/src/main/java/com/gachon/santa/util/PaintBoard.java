package com.gachon.santa.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PaintBoard {

    int thickness = 0;
    MyPaintView myView;
    String cacheDir;

    public PaintBoard(MyPaintView myView, String cacheDir) {
        this.myView = myView;
        this.cacheDir = cacheDir;
    }

    public void changePontSize(){
        if(thickness % 2 == 1){
            myView.mPaint.setStrokeWidth(10);
        } else {
            myView.mPaint.setStrokeWidth(20);
        }
        thickness++;
    }

    public void clearPicture(){
        myView.mBitmap.eraseColor(Color.WHITE);
        myView.invalidate();
    }

    public void loadPicture(String path) {
        clearPicture();
        Bitmap bitmap = BitmapFactory.decodeFile(cacheDir + "/" + path + ".jpg").copy(Bitmap.Config.ARGB_8888, true);
        myView.draw(bitmap);
        //Toast.makeText(getApplicationContext(), "로딩 완료", Toast.LENGTH_SHORT).show();
    }

    public void savePicture(String path) {
        myView.setDrawingCacheEnabled(true);    // 캐쉬허용
        // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
        Bitmap image = myView.getDrawingCache();
        Log.e("save", image.toString());
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
        savePicture(path);
        Uri file = Uri.fromFile(new File(cacheDir + "/" + path + ".jpg"));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference riversRef = storageRef.child("images/"  + user.getUid() + "/" + type + "/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(com.gachon.santa.activity.PaintBoardActivity.this, "저장 실패", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(com.gachon.santa.activity.PaintBoardActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
