package com.dntutty.ui.materialdesign;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.dntutty.ui.R;
import com.dntutty.ui.materialdesign.adapter.LoadMoreAdapter;
import com.dntutty.ui.materialdesign.bean.Movie;
import com.dntutty.ui.materialdesign.view.LoadingView;

import java.util.ArrayList;

public class ToolbarActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private int end;
    private int start;
    private ArrayList<Movie.SubjectsBean> mMovieList = new ArrayList<>();
    private LoadMoreAdapter loadMoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        //如果不设置,则不会出现标题
        Toolbar toolbar = findViewById(R.id.toolbar);
        //不设置会显示label的属性,也可以在清单文件中进行设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("toolbar标题");
        }
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            //设置Toolbar home键可点击
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            //设置Toolbar home键图标
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_am);
//        }

        //Toobar关联侧滑菜单
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //浮动按钮
        FloatingActionButton floatingActionButton = findViewById(R.id.float_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出提示
                Snackbar.make(ToolbarActivity.this.findViewById(16908290), "snack action", 1000)
                        //Snackbar点击响应
                        .setAction("Toast", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ToolbarActivity.this, " to do ", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        refreshLayout = findViewById(R.id.srl_refresh);
        refreshLayout.setRefreshing(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMovieList.clear();
                end = 20;
                getNetData(start, end, true);
            }
        });

        getNetData(start, end, false);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMoreData() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMovieList.size() < 250) {
                            getNetData(end, 20, false);
                            end += 20;
                        } else {
                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                        }
                    }
                }, 2000);
            }
        });


    }

    private void getNetData(int start, int end, boolean b) {
        for (int i = 0; i < 10; i++) {
            mMovieList.add(new Movie.SubjectsBean());
        }
        if (loadMoreAdapter == null) {
            loadMoreAdapter = new LoadMoreAdapter(mMovieList, ToolbarActivity.this);
            recyclerView.setAdapter(loadMoreAdapter);
        } else {
            loadMoreAdapter.notifyDataSetChanged();
        }
        refreshLayout.setRefreshing(false);
        initItemListener(mMovieList);
        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
    }

    private void initItemListener(final ArrayList<Movie.SubjectsBean> movie) {
        loadMoreAdapter.setOnItemClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Activity共享元素转场动画
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ToolbarActivity.this,view.findViewById(R.id.iv_icon),
                        "basic"
                );
                Intent intent = new Intent(ToolbarActivity.this,MovieDitailActivity.class);
                intent.putExtra("URL",movie.get(position).getImages().getMedium());
                intent.putExtra("NAME",movie.get(position).getTitle());
                startActivity(intent,optionsCompat.toBundle());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobalr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.add:
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tb_setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
