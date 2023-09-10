package com.gachon.santa;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaintBoard extends AppCompatActivity {

    private MyPaintView myView;
    private Button btnTh, btnClear, btnSave, btnLoad, btnPrevious;
    int thickness = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_board);

        setTitle("간단 그림판");
        myView = new MyPaintView(this);

        btnTh = findViewById(R.id.btnTh);
        btnTh.setOnClickListener(onClickListener);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(onClickListener);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);
        btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(onClickListener);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(onClickListener);

        ((LinearLayout) findViewById(R.id.paintLayout)).addView(myView);
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                        switch (checkedId) {
                            case R.id.btnRed:
                                myView.mPaint.setColor(Color.RED);
                                break;
                            case R.id.btnGreen:
                                myView.mPaint.setColor(Color.GREEN);
                                break;
                            case R.id.btnBlue:
                                myView.mPaint.setColor(Color.BLUE);
                                break;
                            case R.id.btnErase:
                                myView.mPaint.setColor(Color.WHITE);
                                break;
                        }
                    }
                });
    }

    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()){
            case R.id.btnTh:
                if(thickness % 2 == 1){
                    btnTh.setText("Thin");
                    myView.mPaint.setStrokeWidth(10);
                } else {
                    btnTh.setText("Thick");
                    myView.mPaint.setStrokeWidth(20);
                }
                thickness++;
                break;
            case R.id.btnClear:
                clearPicture();
                break;
            case R.id.btnSave:
                savePicture();
                break;
            case R.id.btnLoad:
                loadPicture();
                break;
            case R.id.btnPrevious:
                rollback();
                break;
        }
    };

    private void clearPicture(){
        myView.mBitmap.eraseColor(Color.WHITE);
        myView.invalidate();
    }

    private void loadPicture() {
        clearPicture();
        Bitmap bitmap = BitmapFactory.decodeFile(getCacheDir() + "/test.jpg").copy(Bitmap.Config.ARGB_8888, true);
        myView.draw(bitmap);
        Toast.makeText(getApplicationContext(), "로딩 완료", Toast.LENGTH_SHORT).show();
    }

    private void savePicture() {
        myView.setDrawingCacheEnabled(true);    // 캐쉬허용
        // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
        Bitmap image = myView.getDrawingCache();
        Log.e("save", image.toString());
        File storage = getCacheDir();
        File file = new File(storage, "test.jpg");
        OutputStream outputStream = null;
        try{
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
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

    private void rollback(){
        clearPicture();
        myView.rollback();
        Toast.makeText(this, "롤백 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private static class MyPaintView extends View {
        private Bitmap mBitmap;
        private Bitmap previousBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;
        public MyPaintView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            previousBitmap = mBitmap;
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.WHITE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, null); //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
        }

        public void draw(Bitmap bitmap){
            previousBitmap = mBitmap;
            mBitmap = bitmap;
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawBitmap(bitmap, 0,0, mPaint);
        }

        public void rollback(){
            mCanvas = new Canvas(previousBitmap);
            mCanvas.drawBitmap(previousBitmap, 0, 0, mPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int)event.getX();
            int y = (int)event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint); //mBitmap 에 기록
                    mPath.reset();
                    break;
            }
            this.invalidate();
            return true;
        }
    }
}