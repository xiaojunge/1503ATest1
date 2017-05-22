package baway.com.cj.mvptest1503a.presenter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import baway.com.cj.mvptest1503a.model.utils.HttpUtils;
import baway.com.cj.mvptest1503a.view.iview.IHomeView;

/**
 * Created by cj on 2017/5/9.
 */

public class HomePresenter extends BasePresenter<IHomeView> {
    private String url = "http://api.expoon.com/AppNews/getNewsList/type/2/p/1";
    private HashMap<String, String> hashMap = new HashMap<>();


    public void getImageFormServer(ImageView imageView, String url) {
        x.image().bind(imageView, url);
    }

    public <T> void getHomeDataFromServer(Class<T> t) {
        HttpUtils.getTestData(url, hashMap, new HttpUtils.CallbackVideoData<T>() {
            @Override
            public void callback(T t) {
                if (getMvpView() != null) {
                    getMvpView().callbackStr(t);
                } else {
                    Log.e("HomePresenter", "请调用 attachView ");
                }
            }
        }, t);
    }

    public void uploadImage(String url, String filePath) {
        RequestParams params = new RequestParams(url);

        //params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", new File(filePath)));
        //list.add(new KeyValue("parameters", json.toString()));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setMultipart(true);
        params.setRequestBody(body);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("myMessage", " " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("myMessage", " " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void uploadFile(String uri, String fileName, String filePath) {

        HttpURLConnection conn = null;
        /// boundary就是request头和上传文件内容的分隔符(可自定义任意一组字符串)
        String BOUNDARY = "******";
        // 用来标识payLoad+文件流的起始位置和终止位置(相当于一个协议,告诉你从哪开始,从哪结束)
        String preFix = ("\r\n--" + BOUNDARY + "--\r\n");
        try {
            // (HttpConst.uploadImage 上传到服务器的地址
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方法
            conn.setRequestMethod("POST");
            // 设置header
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            // 获取写输入流
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 获取上传文件
            File file = new File(filePath);
            // 要上传的数据
            StringBuffer strBuf = new StringBuffer();
            // 标识payLoad + 文件流的起始位置
            strBuf.append(preFix);

            // 下面这三行代码,用来标识服务器表单接收文件的name和filename的格式
            // 在这里,我们是file和filename.后缀[后缀是必须的]。
            // 这里的fileName必须加个.jpg,因为后台会判断这个东西。
            // 这里的Content-Type的类型,必须与fileName的后缀一致。
            // 如果不太明白,可以问一下后台同事,反正这里的name和fileName得与后台协定！
            // 这里只要把.jpg改成.txt，把Content-Type改成上传文本的类型，就能上传txt文件了。
            strBuf.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + ".jpg" + "\"\r\n");
            strBuf.append("Content-Type: image/jpeg" + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());

            // 获取文件流
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream inputStream = new DataInputStream(fileInputStream);

            // 每次上传文件的大小(文件会被拆成几份上传)
            int bytes = 0;
            // 计算上传进度
            float count = 0;
            // 获取文件总大小
            int fileSize = fileInputStream.available();
            // 每次上传的大小
            byte[] bufferOut = new byte[1024];
            // 上传文件
            while ((bytes = inputStream.read(bufferOut)) != -1) {
                // 上传文件(一份)
                out.write(bufferOut, 0, bytes);
                // 计算当前已上传的大小
                count += bytes;
                // 打印上传文件进度(已上传除以总大*100就是进度)
                Log.e("progress:", (count / fileSize * 100) + "%");
            }

            // 关闭文件流
            inputStream.close();

            // 标识payLoad + 文件流的结尾位置
            out.write(preFix.getBytes());

            // 至此上传代码完毕

            // 总结上传数据的流程：preFix + payLoad(标识服务器表单接收文件的格式) + 文件(以流的形式) + preFix
            // 文本与图片的不同,仅仅只在payLoad那一处的后缀的不同而已。

            // 输出所有数据到服务器
            out.flush();

            // 关闭网络输出流
            out.close();

            // 重新构造一个StringBuffer,用来存放从服务器获取到的数据
            strBuf = new StringBuffer();

            // 打开输入流 , 读取服务器返回的数据
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            String line;

            // 一行一行的读取服务器返回的数据
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }

            // 关闭输入流
            reader.close();


        } catch (Exception e) {
            Log.e("上传图片出错:", e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    public void handleClickEvent(View v, int position) {
        Log.e("myMessage","position "+position);
    }
}
