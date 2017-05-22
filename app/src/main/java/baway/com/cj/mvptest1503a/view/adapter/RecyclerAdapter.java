package baway.com.cj.mvptest1503a.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import baway.com.cj.mvptest1503a.R;
import baway.com.cj.mvptest1503a.model.home.HomeBean;
import baway.com.cj.mvptest1503a.presenter.HomePresenter;
import baway.com.cj.mvptest1503a.view.iview.RecylerViewCallback;

/**
 * Created by caojun on 2017/5/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<HomeBean.DataBean> mDatas = new ArrayList<>();
    private HomePresenter mHomePresenter;

    public RecyclerAdapter (Context context){
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.home_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder)holder;
        myViewHolder.viewById.setText("text "+mDatas.get(position).getNews_title());
        mHomePresenter.getImageFormServer(myViewHolder.imageView,mDatas.get(position).getPic_url());
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理点击事件
                //mHomePresenter.handleClickEvent(v,position);
                mRecylerViewCallback.callbackPosition(v,position);
            }
        });
    }

    private RecylerViewCallback mRecylerViewCallback;

    public void setClickListener(RecylerViewCallback recylerViewCallback) {
        this.mRecylerViewCallback = recylerViewCallback;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }



    public void setData(List<HomeBean.DataBean> datas) {
        if (datas!=null) {
            mDatas.addAll(datas);
        }

    }

    public void setPresenter(HomePresenter homePresenter) {
        this.mHomePresenter = homePresenter;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView viewById;
        private final ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            viewById = (TextView) itemView.findViewById(R.id.tv);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
}
