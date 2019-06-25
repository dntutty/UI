package com.dntutty.ui.musicalbum;

import android.app.LauncherActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dntutty.ui.R;
import com.dntutty.ui.musicalbum.ui.UIUtils;
import com.dntutty.ui.musicalbum.ui.ViewCalculateUtil;
import com.dntutty.ui.musicalbum.util.FastBlurUtil;
import com.dntutty.ui.musicalbum.util.StatusBarUtil;
import com.dntutty.ui.musicalbum.util.ToolbarUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicAlbumActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private View headerView;
    private ImageView iv_header_bg;//顶部背景图片
    private ImageView iv_header_poster;//header中的封面

    private LinearLayout ll_album_detail;//header中的封面右边布局
    private LinearLayout ll_play_all;//header中播放全部布局
    private LinearLayout ll_album_info;//header中封面图和标题等的包裹布局
    private LinearLayout ll_options;//header中评论下载等按钮的包裹
    private LinearLayout ll_list_top;//header中会员享高品质及下方的悬浮区域的包裹布局
    private LinearLayout ll_be_member;//header中会员尊享高品质的包裹布局
    private TextView tv_music_list_name;//header中歌单名字
    private FrameLayout fl_layout_float;//真正悬浮的view包裹布局
    private LinearLayout ll_layout_float;//真正悬浮的view布局
    private Drawable backdrawable;

    //onCreate中是获取不到控件宽高的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_album);
        UIUtils.getInstance(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.more));
        toolbar.setTitle("歌单");
        ToolbarUtils.setMarqueeForToolbarTitleView(toolbar);
        setSupportActionBar(toolbar);
        //设置沉浸式,并且toolbar设置padding
        StatusBarUtil.setStateBar(this, toolbar);


        headerView = getLayoutInflater().inflate(R.layout.header_lv_album, null, false);
        iv_header_poster = headerView.findViewById(R.id.iv_header_poster);
        iv_header_bg = headerView.findViewById(R.id.iv_header_bg);
        ll_album_info = headerView.findViewById(R.id.ll_album_info);
        iv_header_poster = headerView.findViewById(R.id.iv_header_poster);
        ll_album_detail = headerView.findViewById(R.id.ll_album_detail);
        tv_music_list_name = headerView.findViewById(R.id.tv_music_list_name);
        ll_options = headerView.findViewById(R.id.ll_option);
        ll_list_top = headerView.findViewById(R.id.ll_list_top);
        ll_be_member = headerView.findViewById(R.id.ll_be_member);
        ll_play_all = headerView.findViewById(R.id.ll_play_all);
        fl_layout_float = findViewById(R.id.fl_layout_float);
        ll_layout_float = findViewById(R.id.ll_layout_float);
        listView = findViewById(R.id.list_album);
        listView.addHeaderView(headerView);


        ViewCalculateUtil.setViewLinearLayoutParam(iv_header_poster, 252, 252, 0, 0, 32, 0, true);
        ViewCalculateUtil.setViewLinearLayoutParam(ll_album_detail, 560, 252, 0, 0, 16, 0, true);

        //创建一个list集合,list集合的元素的是Map
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("music_no", i);
            listItem.put("music_name", "歌曲" + i);
            listItem.put("auth_name", "歌手" + i);
            //加入list集合
            listItems.add(listItem);

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item_list_album, new String[]{"music_no", "music_name", "music_auth"}, new int[]{R.id.item_position, R.id.music_name, R.id.item_auth});
        listView.setAdapter(simpleAdapter);

        try2UpdatePicBackground(R.drawable.poster);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //播放全部与toolbar之前的距离  初始距离即滑动过程中需要计算的距离
                float originHeight = headerView.getHeight() - toolbar.getHeight() - ll_play_all.getHeight();
                //当前滑动距离
                float currentHeight = headerView.getBottom() - toolbar.getHeight() - ll_play_all.getHeight();
                if (originHeight == 0) {
                    return;
                }

                //获取歌单名县归于屏幕顶部的距离
                Rect rect = new Rect();
                tv_music_list_name.getGlobalVisibleRect(rect);
                Log.e("rect", rect.top + "");
                if (rect.top < 10) {
                    //标题显示歌单名
                    if (!toolbar.getTitle().equals(getResources().getText(R.string.list_name)))
                        toolbar.setTitle(getResources().getText(R.string.list_name));
                } else if (rect.top > 10 && currentHeight > 0) {
                    //标题显示默认歌单
                    if (!toolbar.getTitle().equals(getResources().getText(R.string.list_name_default)))
                        toolbar.setTitle(getResources().getText(R.string.list_name_default));
                }

                if (currentHeight > 0) {
                    //控制悬浮按钮的显示和隐藏
                    //拖动比
                    float part = currentHeight / originHeight;
                    ll_album_info.setAlpha(part);
                    ll_options.setAlpha(part);
                    ll_be_member.setAlpha(part);
                    ll_list_top.getBackground().setAlpha((int) (255 * part));
                    Drawable drawable = toolbar.getBackground();
                    drawable.setAlpha((int) (255 * (1 - part)));
                    toolbar.setBackground(drawable);
//                    fl_layout_float.setBackground(null);
                    fl_layout_float.setVisibility(View.INVISIBLE);
                } else {
                    ll_album_info.setAlpha(0);
                    ll_options.setAlpha(0);
                    ll_be_member.setAlpha(0);
                    Drawable drawable = toolbar.getBackground();
                    drawable.setAlpha(0);
                    toolbar.setBackground(drawable);
                    fl_layout_float.setPaddingRelative(0, toolbar.getHeight(), 0, 0);
                    fl_layout_float.setVisibility(View.VISIBLE);
//                    fl_layout_float.setBackground(toolbar.getBackground());
                }
            }
        });
    }

    private void try2UpdatePicBackground(final int poster) {
        //后台处理
        new Thread(new Runnable() {
            @Override
            public void run() {
//                backdrawable = getForegroundDrawable(poster);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fl_layout_float.setBackground(getForegroundDrawable(poster));
                        iv_header_bg.setImageDrawable(getForegroundDrawable(poster));
                        toolbar.setBackground(getForegroundDrawable(poster));
                    }
                });
            }
        }).start();
    }

    private Drawable getForegroundDrawable(int poster) {
        //得到屏幕的宽高比,以便按比例切割图片一部分
        final float widthHeightSize = (float) (UIUtils.getScreenWidth(this) * 1.0 / UIUtils.getScreenHeight(this) * 1.0);

        Bitmap bitmap = getForegroundBitmap(poster);

        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        //切割部分图片
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth, bitmap.getHeight());
        //缩小图片
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap.getHeight() / 50, false);
        //模糊化
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);

        final Drawable foregroundDrawable = new BitmapDrawable(getResources(), blurBitmap);
        //加入灰色遮罩层,避免图片过亮影响其他控件
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }


    private Bitmap getForegroundBitmap(int picRes) {
        int screenWidth = UIUtils.getScreenWidth(this);
        int screenHeight = UIUtils.getScreenHeight(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getResources(), picRes, options);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            return BitmapFactory.decodeResource(getResources(), picRes);
        }

        int sample = 2;
        int sampleX = imageWidth / UIUtils.getScreenWidth(this);
        int sampleY = imageHeight / UIUtils.getScreenHeight(this);

        if (sampleX > sampleY && sampleY > 1) {
            sample = sampleX;
        } else if (sampleY > sampleX && sampleX > 1) {
            sample = sampleY;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = sample;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(getResources(), picRes, options);
    }


    //在该回调可以获取控件宽高
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music, menu);
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    ((Method) m).setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
