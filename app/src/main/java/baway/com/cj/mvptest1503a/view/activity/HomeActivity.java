package baway.com.cj.mvptest1503a.view.activity;

import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import baway.com.cj.mvptest1503a.R;
import baway.com.cj.mvptest1503a.model.home.HomeBean;
import baway.com.cj.mvptest1503a.presenter.HomePresenter;
import baway.com.cj.mvptest1503a.view.adapter.HomeAdapter;
import baway.com.cj.mvptest1503a.view.adapter.RecyclerAdapter;
import baway.com.cj.mvptest1503a.view.iview.IHomeView;
import baway.com.cj.mvptest1503a.view.iview.RecylerViewCallback;
import baway.com.cj.mvptest1503a.view.widget.MyDividerItemDecoration;

public class HomeActivity extends AppCompatActivity implements IHomeView<HomeBean>,SwipeRefreshLayout.OnRefreshListener{

    private HomePresenter homePresenter;
    private HomeAdapter homeAdapter;
    private ListView listView;
    private String uploadUrl = "http://169.254.159.111:8080/ssm/user/photoUpload";
    private String upUrl;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        pb = (ProgressBar) findViewById(R.id.progress);
        //listView = (ListView) findViewById(R.id.listView);
        Button viewById = (Button)findViewById(R.id.uploade);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.YELLOW,Color.BLUE);
        swipeRefreshLayout.setProgressViewOffset(true,10,100);

        try {
            Picasso.with(this).load("").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d("myMessage","dx "+ dx +" dy = "+dy);
                if (dy > 0) {
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                    int itemCount = linearLayoutManager.getItemCount();

                    if (lastVisibleItemPosition + 1 == itemCount) {
                        pb.setVisibility(View.VISIBLE);
                      Toast.makeText(HomeActivity.this,"JJJ= "+lastVisibleItemPosition +" itemCount = "+itemCount,Toast.LENGTH_SHORT).show();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    sleep(2000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pb.setVisibility(View.GONE);
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }.start();
                    }
                }
            }
        });


        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homePresenter.uploadImage(uploadUrl,upUrl);
            }
        });
    }

    private void initData() {
        homePresenter = new HomePresenter();
        // this == HomeActivity
        homePresenter.attachView(this);
        //homeAdapter = new HomeAdapter(this);
        //homeAdapter.setPresenter(homePresenter);
       // listView.setAdapter(homeAdapter);

        upUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pic/hh.png";
        Log.e("myMessage"," upUrl= " + upUrl);

        recyclerAdapter = new RecyclerAdapter(this);
        recyclerAdapter.setPresenter(homePresenter);
        recyclerAdapter.setClickListener(new RecylerViewCallback() {
            @Override
            public void callbackPosition(View view, int position) {
                Log.e("myMessage","position "+position);
            }
        });
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this));

    }

    @Override
    public void callbackStr(HomeBean data) {
//        homeAdapter.setData(data.getData());
//        homeAdapter.notifyDataSetChanged();

        recyclerAdapter.setData(data.getData());
        recyclerAdapter.notifyDataSetChanged();
        //swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void callbackErr(String errMsg, int errCode) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        homePresenter.getHomeDataFromServer(HomeBean.class);
    }
}
