package com.example.minh.cropimageviewfull;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by Minh on 11/7/2018.
 */

public class CanvasView extends View {
    public Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private float mX, mY;
    ImageView mImageView;
    private static final float TOLERANCE = 5;

    public CanvasView(Context context, ImageView image) {
        super(context);
        mImageView = image;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        mBitmap = Bitmap.createBitmap(290, 290, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        event(image);
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        mImageView.setImageBitmap(mBitmap);
    }
    public Paint getPaint() {
        return mPaint;
    }

    private void event(final ImageView mImageView) {
        mImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mX = x;
                        mY = y;
                        drawOnProjectedBitMap(mImageView, mBitmap, mX, mY, x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        drawOnProjectedBitMap(mImageView, mBitmap, mX, mY, x, y);
                        mX = x;
                        mY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        drawOnProjectedBitMap(mImageView, mBitmap, mX, mY, x, y);
                        break;
                }
                return true;
            }
        });
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    private void drawOnProjectedBitMap(ImageView iv, Bitmap bm,
                                       float x0, float y0, float x, float y) {
        if (x < 0 || y < 0 || x > iv.getWidth() || y > iv.getHeight()) {
            //outside ImageView
            return;
        } else {
            float ratioWidth = (float) bm.getWidth() / (float) iv.getWidth();
            float ratioHeight = (float) bm.getHeight() / (float) iv.getHeight();

            mCanvas.drawLine(
                    x0 * ratioWidth,
                    y0 * ratioHeight,
                    x * ratioWidth,
                    y * ratioHeight,
                    mPaint);
            mImageView.invalidate();
        }
    }

    public void clearCanavas() {
        mBitmap = Bitmap.createBitmap(290, 290, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        mImageView.setImageBitmap(mBitmap);
        invalidate();
    }

}
