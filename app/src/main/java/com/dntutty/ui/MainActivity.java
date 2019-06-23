package com.dntutty.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance(this).getStatusBarHeight(this);
        Utils.getInstance(this).getHorizontalScale();
        Utils.getInstance(this).getVerticalScale();
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.text);

    }
}
