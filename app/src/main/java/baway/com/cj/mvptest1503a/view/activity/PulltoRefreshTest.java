package baway.com.cj.mvptest1503a.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import baway.com.cj.mvptest1503a.R;
import baway.com.cj.mvptest1503a.view.adapter.PulltoRefreshAdapter;

/**
 * Created by caojun on 2017/5/19.
 */

public class PulltoRefreshTest extends Activity implements PullToRefreshListView.OnRefreshListener2 {

    private PullToRefreshListView pullToRefreshListView;
    private int currentPage = 1;
    private PulltoRefreshAdapter pulltoRefreshAdapter;
    private Void dataFromServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullto_test);

        initView();
        initData();
        getDataFromServer();

    }

    private void initData() {
        pulltoRefreshAdapter = new PulltoRefreshAdapter(this);
        pullToRefreshListView.setAdapter(pulltoRefreshAdapter);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }

    private void initView() {
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_list);
        pullToRefreshListView.setOnRefreshListener(this);

        ListView refreshableView = pullToRefreshListView.getRefreshableView();

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        //刷新数据
        refreshData();
    }

    private void refreshData() {
        currentPage = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshListView.onRefreshComplete();
            }
        },2000);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        //加载更多
        loadMore();
    }

    private void loadMore() {
        currentPage++;

    }

    public void getDataFromServer() {
        ArrayList<String> datas = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            datas.add("ssss = "+i);
        }
        pulltoRefreshAdapter.setData(datas);
        pulltoRefreshAdapter.notifyDataSetChanged();


    }
}
