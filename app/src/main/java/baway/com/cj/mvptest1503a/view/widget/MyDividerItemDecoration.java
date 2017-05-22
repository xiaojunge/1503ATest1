package baway.com.cj.mvptest1503a.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * Created by caojun on 2017/5/18.
 */

public class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int widthPixels = 0;

    public MyDividerItemDecoration(Context context) {
        this.mContext = context;
        getScreenSize();
    }

    private void getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        widthPixels = dm.widthPixels;
        System.out.println("width-display :" + dm.widthPixels);
        System.out.println("heigth-display :" + dm.heightPixels);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        //RectF rectF = new RectF(0,100,1000,110);
        //c.drawLine(0,1000,10,10,paint);

        //c.drawCircle(200,200,200,paint);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            int itemBottom = childAt.getBottom();
            int left = childAt.getLeft();
            int right = childAt.getRight();
            c.drawRect(left,itemBottom,widthPixels,itemBottom+10,paint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        c.drawCircle(200,400,200,paint);
    }
}
