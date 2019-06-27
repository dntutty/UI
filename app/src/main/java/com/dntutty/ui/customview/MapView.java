package com.dntutty.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.View;

import com.dntutty.ui.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapView extends View {
    private List<ProvinceItem> itemList;
    private int defaultColor = 0xFFCCCCCC;
    private Paint mPaint;
    private ProvinceItem selectItem;

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
                InputStream inputStream = getResources().openRawResource(R.raw.chinaHigh);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//取得DocumentBuilderFactory的实例
                DocumentBuilder builder = null;//从factory获取DocumentBuilder实例
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);  //解析输入流得到Document实例
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("d");
                    @SuppressLint("RestrictedApi") Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path);
                    provinceItem.setDrawColor(defaultColor);
                    itemList.add(provinceItem);
                }
                postInvalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (itemList != null) {
            canvas.save();
            for (ProvinceItem provinceItem : itemList) {
                if (provinceItem != selectItem) {
                    provinceItem.drawItem(canvas,mPaint,false);
                } else {
                    provinceItem.drawItem(canvas,mPaint,true);
                }

            }
        }
    }
}
