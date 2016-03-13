package com.lovejjfg.zhifou.view;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lovejjfg.zhifou.R;
import com.lovejjfg.zhifou.data.BmobUtil;
import com.lovejjfg.zhifou.data.model.DailyStories;
import com.lovejjfg.zhifou.presenters.ListPresenter;
import com.lovejjfg.zhifou.presenters.ListPresenterImpl;
import com.lovejjfg.zhifou.ui.recycleview.OnItemClickListener;
import com.lovejjfg.zhifou.ui.recycleview.StoriesAdapter;
import com.lovejjfg.zhifou.ui.recycleview.holder.DateViewHolder;
import com.lovejjfg.zhifou.ui.widget.SwipRefreshRecycleView;

public class ListStory extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListPresenter.View, View.OnClickListener,OnItemClickListener, SwipRefreshRecycleView.OnRefreshLoadMoreListener, SwipRefreshRecycleView.OnScrollListener {

    private SwipRefreshRecycleView mRecyclerView;
    private ListPresenterImpl mMainPresenter;
    private GridLayoutManager manager;
    private StoriesAdapter adapter;
    private String mDate;
//    private SwipeRefreshLayout mSwip;
    private String mTitle;
    private double lastTitlePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mRecyclerView = (RecyclerView) findViewById(R.id.rcv);
//        mSwip = (SwipeRefreshLayout) findViewById(R.id.srl);
        mRecyclerView = (SwipRefreshRecycleView) findViewById(R.id.srrv);
        manager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(manager);
        adapter = new StoriesAdapter();
        adapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnScrollListener(this);

        mMainPresenter = new ListPresenterImpl(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Log.e("请求开始了！！", "");
            new Thread(){
                @Override
                public void run() {
                    BmobUtil.send(ListStory.this);
                }
            }.start();



        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLoadMore(DailyStories stories) {
        adapter.appendList(stories);
        mDate = stories.getDate();
    }

    @Override
    public void onLoadError(String errorCode) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent i = new Intent(this, DatePick.class);
                final ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(this,
                                Pair.create(v,
                                        getString(R.string.date_picker)));
                this.startActivity(i, options.toBundle());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.onDestroy();
    }

    @Override
    public void isLoading(final boolean isLoading) {
        mRecyclerView.setRefresh(isLoading);
    }

    @Override
    public void isLoadingMore(boolean loading) {
        adapter.isLoadingMore(loading);
        adapter.notifyItemChanged(adapter.getItemCount());
    }

//    @Override
//    public void onRefresh() {
//        mMainPresenter.onLoading();
//    }

    @Override
    public void onItemClick( int id) {
        mMainPresenter.onItemClicked(id);
    }

    @Override
    public void onRefresh() {
        mMainPresenter.onLoading();
    }

    @Override
    public void onLoadMore() {
        mMainPresenter.onLoadMore(mDate);
    }

    @Override
    public void onScrolled(SwipRefreshRecycleView recyclerView, int dx, int dy) {
        int position = manager.findFirstVisibleItemPosition();
            if (lastTitlePos == position) {
                return;
            }
            StoriesAdapter.Item item = adapter.getItem(position);
            int type = item.getType();
            if (type == StoriesAdapter.Type.TYPE_HEADER) {
                mTitle = getString(R.string.title_activity_main);
            } else if (dy > 0 ) {
//                mTitle = DateViewHolder.getDate(item.getDate(), ListStory.this);
                mTitle = DateViewHolder.getDate(adapter.getTitleAtPosition(position), ListStory.this);
                if (TextUtils.isEmpty(mTitle)) {
                    return;
                }
            } else if (dy < 0) {
                mTitle = DateViewHolder.getDate(adapter.getTitleBeforePosition(position), ListStory.this);
            }
            ListStory.this.getSupportActionBar().setTitle(mTitle);
            lastTitlePos = position;
    }
}