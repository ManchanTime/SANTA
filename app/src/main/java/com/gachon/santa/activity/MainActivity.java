package com.gachon.santa.activity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.gachon.santa.R;
import com.gachon.santa.dialog.ProgressDialog;
import com.gachon.santa.entity.NotificationInfo;
import com.gachon.santa.entity.PaintInfo;
import com.gachon.santa.util.BasicFunctions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends BasicFunctions {

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_GALLERY = 1;
    private RelativeLayout chooseGC, chooseType;
    private String type;
    private Uri uri;
    private final ArrayList<NotificationInfo> notificationData = new ArrayList<>();
    private Button btnNotification;
    private ImageView imgRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFigure = findViewById(R.id.button_figure);
        btnFigure.setOnClickListener(onClickListener);

        Button btnHTP = findViewById(R.id.button_htp);
        btnHTP.setOnClickListener(onClickListener);

        Button btnLMT = findViewById(R.id.button_lmt);
        btnLMT.setOnClickListener(onClickListener);

        Button btnPITR = findViewById(R.id.button_pitr);
        btnPITR.setOnClickListener(onClickListener);

        Button btnCamera = findViewById(R.id.button_camera);
        btnCamera.setOnClickListener(onClickListener);

        Button btnGoGallery = findViewById(R.id.btn_go_gallery);
        btnGoGallery.setOnClickListener(onClickListener);

        Button btnGoCamera = findViewById(R.id.btn_go_camera);
        btnGoCamera.setOnClickListener(onClickListener);

        Button btnChooseType = findViewById(R.id.btn_choose_type);
        btnChooseType.setOnClickListener(onClickListener);

        Button btnMyPaint = findViewById(R.id.button_my_paint);
        btnMyPaint.setOnClickListener(onClickListener);

        btnNotification = findViewById(R.id.button_notification);
        btnNotification.setOnClickListener(onClickListener);

        //갤러리 or 카메라 선택
        chooseGC = findViewById(R.id.relative_gallery_camera);
        chooseGC.setOnClickListener(onClickListener);
        //업로드 타입(선, PITR 등) 선택
        chooseType = findViewById(R.id.relative_type);
        chooseType.setOnClickListener(onClickListener);

        //새로운 알림이 있다면 보임 아니면 안 보임
        imgRead = findViewById(R.id.image_read);

        //타입 종류 라디오 버튼
        RadioGroup radioChoose = findViewById(R.id.radio_type);
        radioChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioId = radioChoose.getCheckedRadioButtonId();
                switch (radioId){
                    case R.id.radio_figure:
                        type = "figure";
                        break;
                    case R.id.radio_htp:
                        type = "k_htp";
                        break;
                    case R.id.radio_lmt:
                        type = "lmt";
                        break;
                    case R.id.radio_pitr:
                        type = "pitr";
                        break;
                }
                btnChooseType.setEnabled(true);
            }
        });

        setNotification();
    }

    @SuppressLint("NonConstantResourceId")
    View.OnClickListener onClickListener = (v) -> {
        Intent intent;
        switch (v.getId()){
            case R.id.button_figure:
                intent = new Intent(this, FigureExplainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_htp:
                intent = new Intent(this, HtpExampleActivity.class);
                startActivity(intent);
                break;
            case R.id.button_lmt:
                intent = new Intent(this, LmtExampleActivity.class);
                startActivity(intent);
                break;
            case R.id.button_pitr:
                intent = new Intent(this, PitrExampleActivity.class);
                startActivity(intent);
                break;
            case R.id.button_camera:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    chooseGC.setVisibility(View.VISIBLE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
                }
                break;
            case R.id.button_my_paint:
                intent = new Intent(this, MyPaintActivity.class);
                startActivity(intent);
                break;
            case R.id.button_notification:
                intent = new Intent(this, AlarmActivity.class);
                intent.putExtra("object", notificationData);
                startActivity(intent);
                break;
            case R.id.btn_go_camera:
                chooseGC.setVisibility(View.GONE);
                goCamera();
                break;
            case R.id.btn_go_gallery:
                chooseGC.setVisibility(View.GONE);
                goGallery();
                break;
            case R.id.relative_gallery_camera:
                if(chooseGC.getVisibility() == View.VISIBLE){
                    chooseGC.setVisibility(View.GONE);
                }else {
                    chooseGC.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.relative_type:
                if(chooseType.getVisibility() == View.VISIBLE){
                    chooseType.setVisibility(View.GONE);
                }else{
                    chooseType.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_choose_type:
                storeImage();
                break;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case (REQUEST_GALLERY):
                    try {
                        uri = data.getData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                case (REQUEST_CAMERA):
                    try {
                        chooseType.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    /**
     * 카메라나 갤러리에서 받아온 uri를 파이어베이스 storage에 저장
     */
    public void storeImage(){
        //골뱅이 돌리기
        ProgressDialog customProgressDialog;
        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //로딩창
        customProgressDialog.show();
        //화면터치 방지
        customProgressDialog.setCanceledOnTouchOutside(false);
        //뒤로가기 방지
        customProgressDialog.setCancelable(false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        assert user != null;
        final StorageReference imageRef = storageRef.child("images/"  + user.getUid() + "/" + type + "/" + new Date() + "/" + uri.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(uri);
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
                            PaintInfo paint = new PaintInfo(documentReference.getId(), user.getUid(), uri.toString(), type, false, new Date());
                            documentReference.set(paint).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    chooseType.setVisibility(View.GONE);
                                    customProgressDialog.cancel();
                                    customProgressDialog.dismiss();
                                    Log.e("success", "success");
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void setNotification(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference chatRef = firebaseFirestore.collection("comments");
        //채팅방이름으로 된 컬렉션에 저장되어 있는 데이터들 읽어오기
        //chatRef의 데이터가 변경될때마다 반응하는 리스너 달기 : get()은 일회용
        chatRef.addSnapshotListener(new EventListener<QuerySnapshot>() { //데이터가 바뀔떄마다 찍음
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //데이터가 바뀔때마다 그냥 add하면 그 순간의 모든것을 찍어 가져오기 때문에 중복되어버림
                //따라서 변경된 Document만 찾아달라고 해야함
                //1. 바뀐 애들 찾온다 - 왜 리스트인가? 처음 시작할 때 문제가 됨 그래서 여러개라고 생각함
                List<DocumentChange> documentChanges = value.getDocumentChanges();
                int count = 0;
                notificationData.clear();
                for (DocumentChange documentChange : documentChanges) {
                    //2.변경된 문서내역의 데이터를 촬영한 DocumentSnapshot얻어오기
                    DocumentSnapshot snapshot = documentChange.getDocument();
                    //3.Document에 있는 필드값 가져오기
                    Map<String, Object> comments = snapshot.getData();
                    if (comments != null) {
                        boolean read = (boolean) comments.get("read");
                        if (!read) {
                            String commentId = comments.get("cid").toString();
                            String publisherId = comments.get("publisher").toString();
                            String targetId = comments.get("target").toString();
                            String postId = comments.get("postId").toString();
                            Date time = new Date(snapshot.getDate("createdAt").getTime());
                            if (targetId.equals(user.getUid())) {
                                count++;
                                notificationData.add(new NotificationInfo(commentId, publisherId, targetId, postId, time));
                            }
                        }
                    }
                }
                if(count != 0){
                    imgRead.setVisibility(View.VISIBLE);
                }else imgRead.setVisibility(View.GONE);
            }
        });
    }

    //갤러리 intent
    public void goGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    //카메라 intent
    public void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }
        // 사진을 저장하고 이미지뷰에 출력
        if (photoFile != null) {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    @Override
    public void onBackPressed() {
        if(chooseGC.getVisibility() == View.VISIBLE){
            chooseGC.setVisibility(View.INVISIBLE);
        }
        else {
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
}