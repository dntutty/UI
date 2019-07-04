package com.dntutty.ui.customview.minerecyclerview;

import android.view.View;

import java.util.Stack;

public class Recycler {
    //定义一个栈数组,不同类型对应不同的栈
    private Stack<View>[] views;//存取效率最高

    public Recycler(int typeNumber) {
        views = new Stack[typeNumber];
        for (int i = 0; i < typeNumber; i++) {
            views[i] = new Stack<View>();
        }
    }

    public void put(View view, int type) {
        views[type].push(view);
    }

    public View get(int type) {
        try {
            return views[type].pop();
        } catch (Exception e) {
            return null;
        }
    }
}
