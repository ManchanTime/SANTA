package com.gachon.santa.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class MyPaintView extends View {
    public Bitmap mBitmap;
    public Bitmap previousBitmap;
    public Canvas mCanvas;
    public Path mPath;
    public Paint mPaint;
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