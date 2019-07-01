package com.dntutty.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dntutty.ui.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapView extends View {
    private List<ProvinceItem> itemList;
    private int defaultColor = 0xFFCCCCCC;
    private Paint mPaint;
    private ProvinceItem selectItem;
    private RectF rectTotal;
    private float scale = 1.0f;

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemList = new ArrayList<>();
        loadThread.start();
    }

    private Thread loadThread = new Thread() {
        @Override
        public void run() {
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.china_high);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//取得DocumentBuilderFactory的实例
                DocumentBuilder builder = null;//从factory获取DocumentBuilder实例
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);  //解析输入流得到Document实例
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");
                ArrayList<ProvinceItem> tempList = new ArrayList<>();
                float left = -1;
                float top =-1;
                float right = -1;
                float bottom = -1;
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("d");
                    @SuppressLint("RestrictedApi") Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path);
                    provinceItem.setDrawColor(defaultColor);
                    RectF rectF = new RectF();
                    path.computeBounds(rectF, true);
                    left = left != -1 ? Math.min(rectF.left, left) : rectF.left;
                    top = top != -1 ? Math.min(rectF.top, top) : rectF.top;
                    right = right != -1 ? Math.max(rectF.right, right) : rectF.right;
                    bottom = bottom != -1 ? Math.max(rectF.bottom, bottom) : rectF.bottom;
                    tempList.add(provinceItem);
                }
                itemList = tempList;
                rectTotal = new RectF(left,top,right,bottom);
                //刷新界面
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                        invalidate();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (itemList != null) {
            canvas.drawColor(0xFF0000FF);
            canvas.save();
            canvas.scale(scale, scale);
            for (ProvinceItem provinceItem : itemList) {
                if (provinceItem != selectItem) {
                    provinceItem.drawItem(canvas, mPaint, false);
                } else {
                    provinceItem.drawItem(canvas, mPaint, true);
                }

            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (rectTotal != null) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            scale = widthSize / rectTotal.width();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouch(event.getX()/scale, event.getY()/scale);
        return super.onTouchEvent(event);

    }

    private void handleTouch(float x, float y) {
        if (itemList == null) {
            return;
        }

        ProvinceItem select = null;
        for (ProvinceItem provinceItem : itemList) {
            if (provinceItem.isTouch(x, y)) {
                select = provinceItem;
            }
        }
        if (select != null) {
            selectItem = select;
            postInvalidate();
        }
    }
}
