package com.dntutty.ui.customview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

public class ProvinceItem {
    private Path path;

    /**
     * 绘制颜色
     */
    private int drawColor;

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }

    public void drawItem(Canvas canvas, Paint mPaint, boolean isSelected) {
        if (isSelected) {
            //绘制内部颜色
            mPaint.clearShadowLayer();
            mPaint.setStrokeWidth(1);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(0xFF8909AC);
            canvas.drawPath(path,mPaint);

            //绘制边界
            mPaint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFFFFFFF;
            mPaint.setColor(strokeColor);
            canvas.drawPath(path,mPaint);
        } else {
            //绘制内部颜色
            mPaint.setShadowLayer(8,0,0,0xffffff);
            mPaint.setStrokeWidth(2);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(drawColor);
            canvas.drawPath(path,mPaint);

            //绘制边界
            mPaint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFD0E8F4;
            mPaint.setColor(strokeColor);
            canvas.drawPath(path,mPaint);
        }
    }

    public boolean isTouch(float x, float y) {
        RectF rectF = new RectF();
        Region region = new Region();
        path.computeBounds(rectF,true);
        region.setPath(path,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        return region.contains((int)x,(int)y);
    }
}
