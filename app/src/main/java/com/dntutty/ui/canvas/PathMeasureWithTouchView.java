package com.dntutty.ui.canvas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dntutty.ui.R;

public class PathMeasureWithTouchView extends View {
    private Path mPath = new Path();
    private Path mSrcPath = new Path();
    private Paint mPaint;
    private Paint mSrcPaint;
    private Bitmap mBitmap;
    private float lastX;
    private float lastY;
    private PathMeasure mPathMeasure;
    private float[] pos = new float[2];
    private float[] tan = new float[2];
    private float degrees;
    private int state;
    private Matrix matrix;

    public PathMeasureWithTouchView(Context context) {
        this(context, null);
    }

    public PathMeasureWithTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureWithTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mPaint.setStrokeMiter(40);
        mPaint.setColor(Color.parseColor("#8909ac"));
        mSrcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSrcPaint.setStyle(Paint.Style.STROKE);
        mSrcPaint.setStrokeCap(Paint.Cap.ROUND);
        mSrcPaint.setStrokeWidth(20);
        mSrcPaint.setStrokeMiter(40);
        mSrcPaint.setColor(Color.parseColor("#00ff00"));
        mPathMeasure = new PathMeasure();
        matrix = new Matrix();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        if (state == 1) {
            canvas.drawPath(mSrcPath,mSrcPaint);
            mSrcPath.reset();
            matrix.reset();
            matrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getWidth() / 2);
            matrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);
            canvas.drawBitmap(mBitmap, matrix, mPaint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                mPath.moveTo(lastX, lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - lastX;
                float deltaY = event.getY() - lastY;
//                mPath.rLineTo(deltaX,deltaY);
                mPath.lineTo(event.getX(), event.getY());
                lastX = event.getX();
                lastY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mPathMeasure.setPath(mPath, false);
                ValueAnimator animator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mPathMeasure.getPosTan((Float) animation.getAnimatedValue(), pos, tan);
                        mPathMeasure.getSegment(0, (Float) animation.getAnimatedValue(),mSrcPath,true);
                        degrees = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
                        invalidate();
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        state = 0;
                        mPath.reset();
                        mSrcPath.reset();
                        super.onAnimationEnd(animation);
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        state = 1;
                        super.onAnimationStart(animation);
                    }
                });
                animator.setDuration(1200);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                break;
        }
        return true;
    }
}
