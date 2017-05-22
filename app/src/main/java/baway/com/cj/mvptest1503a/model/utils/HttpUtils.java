package baway.com.cj.mvptest1503a.model.utils;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by cj on 2017/5/10.
 */

public class HttpUtils {

    public static <T>void getTestData(String url,HashMap<String,String> hashMap, final CallbackVideoData callbackVideoData, final Class<T> clazz) {
        RequestParams requestParams = new RequestParams();
        requestParams.setUri(url);
        if (hashMap != null) {
            Iterator<String> iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                String value = hashMap.get(key);
                requestParams.addQueryStringParameter(key, value);
            }
        }
       // requestParams.addQueryStringParameter("token","1234");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                T t = gson.fromJson(result,clazz);
                callbackVideoData.callback(t);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public interface CallbackVideoData<T>{
        void callback(T t);
    }
}
