package com.ritek.freshwalls.frameutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;


final public class FrameDrawable {
    private static final String TAG = FrameDrawable.class.getSimpleName();

    private String mDrawableResPath;
    private Matrix mMatrix;
    private Context mContext;
    private Paint mPaint;

    float mX;
    float mY;
    float mScale = 1.0f;
    float mAlpha = 1.0f;
    public long mDuration; //当前帧持续显示时间

    public FrameDrawable(String drawableResPath, long duration, Context context) {
        mDrawableResPath = drawableResPath;
        mDuration = duration;
        mContext = context;
        mMatrix = new Matrix();
        mPaint = new Paint();
    }

    public Bitmap draw(Canvas canvas, long start) {
        //这里是从assets中获取bitmap，当然也可以从sdcard中获取，这样就可以热更了帧动画了
        final Bitmap frameBitmap = Utils.loadBitmap(mDrawableResPath,mContext);
        if (null != frameBitmap) {
            final float scaleX = (float) canvas.getWidth() / frameBitmap.getWidth();
            final float scaleY = (float) canvas.getHeight() / frameBitmap.getHeight();
            mMatrix.setTranslate(mX, mY);
            mMatrix.preScale(scaleX * mScale, scaleY * mScale, 0, 0);
            mPaint.setAlpha((int) (mAlpha * 255));
            canvas.drawBitmap(frameBitmap, mMatrix, mPaint);
        }
        return frameBitmap;
    }

}
