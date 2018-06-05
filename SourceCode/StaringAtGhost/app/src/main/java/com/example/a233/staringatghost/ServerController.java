package com.example.a233.staringatghost;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class ServerController {
    String result="";
    String msg="";
    public void showRank(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection=null;
                try {
                    URL url=new URL("http://lllovol.com:3389/show");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET"); //设置请求方式GET，POST
                    //设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作
                    urlConnection.setConnectTimeout(8000);
                    if (urlConnection.getResponseCode() != 200) {
                        //对响应码进行判断,200为成功
                        throw new RuntimeException("请求url失败");
                    }
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    result="";
                    while ((line = bufferedReader.readLine())!=null){
                        result+=line;
                    }
                    msg="Show all data";

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection!=null) urlConnection.disconnect();
                }
            }
        }).start();
    }
    //添加记录
    public void createData(final long score){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    String url_string="http://lllovol.com:3389/insert?score="+score;
                    URL url = new URL(url_string);
                    //利用HttpURLConnection对象从网络中请求网络数据
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET"); //设置请求方式GET，POST
                    //设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作
                    urlConnection.setConnectTimeout(8000);
                    //connection.setReadTimeout(5000);    //设置读取超时
                    if (urlConnection.getResponseCode() != 200) {
                        //对响应码进行判断,200为成功
                        throw new RuntimeException("请求url失败");
                    }
                    //从Internet获取网页,发送请求,将网页以流的形式读回来
                    InputStream inputStream = urlConnection.getInputStream();
                    ////对输入流进行读取
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    msg="";
                    while ((line = bufferedReader.readLine())!=null){
                        msg+=line;
                    }

                } catch (Exception e) {
                    msg="insert failed";
                    e.printStackTrace();
                } finally {
                    if (urlConnection!=null) urlConnection.disconnect();
                }
            }
        }).start();
    }

}
