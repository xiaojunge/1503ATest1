package baway.com.cj.mvptest1503a.view.iview;


/**
 * Created by cj on 2017/5/9.
 */

public interface IHomeView<T> extends IMvpView{
    void callbackStr(T t);
    void callbackErr(String errMsg,int errCode);
}
