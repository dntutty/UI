package com.dntutty.ui.canvas;


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
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dntutty.ui.R;


public class PathMeasureView extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//坐标系
    private Path mPath = new Path();//绘图路径
    private float[] tan = new float[2];
    private float[] pos = new float[2];
    private Bitmap mBitmap;
    private Matrix mMatrix = new Matrix();
    private ValueAnimator animator;
    private PathMeasure pathMeasure;
    private double degrees;

    public PathMeasureView(Context context) {
        this(context, null);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);

        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStrokeWidth(6);

        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        pathMeasure = new PathMeasure(mPath, false);
        pathMeasure.getPosTan(0, pos, tan);
        degrees = Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI;

        //采样压缩
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mLinePaint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mLinePaint);
        canvas.translate(getWidth() / 2, getHeight() / 2);


        canvas.drawPath(mPath, mPaint);

        mMatrix.reset();
        //以图片中心坐标为旋转中心,旋转到当前点切线的角度
        mMatrix.postRotate((float) degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
        //将图片的绘制中心移动到当前点
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            animator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    pathMeasure.setPath(mPath, false);
                    pathMeasure.getPosTan((Float) animation.getAnimatedValue(), pos, tan);
                    Log.e("TAG", "onDraw: pos[0]=" + pos[0] + ";pos[1]=" + pos[1]);
                    Log.e("TAG", "onDraw: tan[0]=" + tan[0] + ";tan[1]=" + tan[1]);

                    //计算当前点的切线方向与x轴的夹角的度数
                    degrees = Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI;
                    Log.e("TAG", "onDraw: degrees=" + degrees);
                    invalidate();

                }
            });
            animator.start();
        }
        return super.onTouchEvent(event);
    }
}
