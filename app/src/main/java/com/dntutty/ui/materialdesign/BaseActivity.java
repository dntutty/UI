package com.dntutty.ui.materialdesign;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder bind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置内容布局
        setContentView(getLayoutId());
        //初始化黄油刀控件绑定框架
        bind = ButterKnife.bind(this);
        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolBar();
    }


    /**
     * 设置内容布局
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化views
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化toolbar
     */
    public abstract void initToolBar();

    /**
     * 加载数据
     */
    public void loadData() {

    }

    /**
     * 显示进度条
     */
    public void showProgressBar() {

    }

    /**
     * 隐藏进度条
     */
    public void hideProgressBar() {

    }

    /**
     * 初始化recyclerView
     */
    public void initRecyclerView() {

    }

    /**
     * 初始化refreshLayout
     */
    public void initRefreshLayout() {

    }

    /**
     * 设置数据显示
     */
    public void finishTask() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
