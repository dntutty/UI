package com.dntutty.ui.materialdesign.customrecyclerview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dntutty.ui.R;
import com.squareup.picasso.Picasso;

public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FeedAdapter mFeedAdapter;
    private RelativeLayout mSuspensionBar;
    private TextView mSuspensionTV;
    private ImageView mSuspensionIV;
    private int mSuspensionHeight;
    private int mCurrentPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mRecyclerView = findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mFeedAdapter = new FeedAdapter();
        mRecyclerView.setAdapter(mFeedAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSuspensionBar = findViewById(R.id.suspension_bar);
        mSuspensionIV = findViewById(R.id.iv_avatar);
        mSuspensionTV = findViewById(R.id.tv_nickname);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取悬浮条的高度
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //对悬浮条的位置进行调整
                //找到下一条itemview
                View view = layoutManager.findViewByPosition(mCurrentPosition+1);
                if (view != null) {
                    Log.e("suspension", "onScrolled: view.getTop(): "+view.getTop());
                    Log.e("suspension", "onScrolled: mSuspensionHeight: "+mSuspensionHeight);
                    if (((View) view).getTop() <= mSuspensionHeight) {
                        //需要对悬浮条进行移动
                        mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                    } else {
                        //保持在原来的位置
                        mSuspensionBar.setY(0);
                    }
                    Log.e("suspension", "onScrolled: mCurrentPosition(not first) "+mCurrentPosition);
                    if (mCurrentPosition != layoutManager.findFirstVisibleItemPosition()) {
                        mCurrentPosition = layoutManager.findFirstVisibleItemPosition();
                        Log.e("suspension", "onScrolled: mCurrentPosition(first) "+mCurrentPosition);
                        updateSuspensionBar();
                    }
                }
            }
        });
        updateSuspensionBar();
    }

    private void updateSuspensionBar() {
        Picasso.with(this)
                .load(getAvatarResId(mCurrentPosition))
                .centerInside()
                .fit()
                .into(mSuspensionIV);
        mSuspensionTV.setText("NetEase " + mCurrentPosition);
    }

    private int getAvatarResId(int position) {
        switch (position % 4) {
            case 0:
                return R.drawable.avatar1;
            case 1:
                return R.drawable.avatar2;
            case 2:
                return R.drawable.avatar3;
            case 3:
                return R.drawable.avatar4;
        }
        return 0;
    }
}
