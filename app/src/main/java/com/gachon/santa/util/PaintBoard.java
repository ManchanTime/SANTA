package com.gachon.santa.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.entity.PaintInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    int thickness = 0;
    MyPaintView myView;
    String cacheDir;
    private ProgressDialog customProgressDialog;


    public PaintBoard(MyPaintView myView, String cacheDir) {
        this.myView = myView;
        this.cacheDir = cacheDir;
        customProgressDialog = new ProgressDialog(myView.getContext());
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void changePontSize(){
        if(thickness % 2 == 1){
            myView.mPaint.setStrokeWidth(10);
        } else {
            myView.mPaint.setStrokeWidth(20);
        }
        thickness++;
    }

    /**
     * 그림판 비우기
     */
    public void clearPicture(){
        myView.mBitmap.eraseColor(Color.WHITE);
        myView.invalidate();
    }

    /**
     * 이미지를 캐시메모리에 가져오기(DB에는 저장x)
     */
    public void loadPicture(String path) {
        clearPicture();
        Bitmap bitmap = BitmapFactory.decodeFile(cacheDir + "/" + path + ".jpg").copy(Bitmap.Config.ARGB_8888, true);
        myView.draw(bitmap);
    }

    /**
     * 이미지를 기기 캐시 메모리에 저장(DB에는 저장x)
     */
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

    /**
     * 이미지를 파이어베이스 storage에 저장
     */
    public void storeImage(String path, String type, FirebaseUser user){
        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);
        savePicture(path);
        Uri file = Uri.fromFile(new File(cacheDir + "/" + path + ".jpg"));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child("images/"  + user.getUid() + "/" + type + "/" + file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               
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
                            PaintInfo paint = new PaintInfo(documentReference.getId(), user.getUid(), uri.toString(), type, new Date());
                            documentReference.set(paint).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.e("success", "success");
                                    customProgressDialog.cancel();
                                    customProgressDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
