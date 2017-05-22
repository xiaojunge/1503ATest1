package baway.com.cj.mvptest1503a.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import baway.com.cj.mvptest1503a.R;
import baway.com.cj.mvptest1503a.presenter.HomePresenter;

/**
 * Created by caojun on 2017/5/19.
 */

public class PulltoRefreshAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> data = new ArrayList<>();

    public PulltoRefreshAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (contentView == null) {
            viewHolder = new ViewHolder();
            contentView = View.inflate(mContext, R.layout.home_item, null);
            viewHolder.textView = (TextView) contentView.findViewById(R.id.tv);
            viewHolder.imageView = (ImageView) contentView.findViewById(R.id.imageView);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.textView.setText(getItem(position));
        return contentView;
    }

    public void setData(List<String> data) {
        if (data != null) {
            this.data.addAll(data);
        }

    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}

