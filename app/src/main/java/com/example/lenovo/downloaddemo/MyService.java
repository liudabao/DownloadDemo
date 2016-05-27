package com.example.lenovo.downloaddemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Manifest;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by lenovo on 2016/5/26.
 */
public class MyService extends Service {
    int max=0;
    NotificationManager manager;
    Notification.Builder builder;
    Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        Log.e("service","start");
       // httpUtil("http://101.200.164.87:8080/visa/download/Visa.apk");
        String url="http://101.200.164.87:8080/visa/download/Visa.apk";
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"test.apk");
        new DownAnsycTask(url, 3, file).execute();
        return  super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void httpUtil(final String str){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                FileOutputStream out;
                try {
                    URL url=new URL(str);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(3000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    max=connection.getContentLength();
                    if(connection.getResponseCode()==200){
                        Log.e("service","connect");
                        InputStream in=connection.getInputStream();
                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"test.apk");
                            if(!file.exists()){
                                file.createNewFile();
                                Log.e("FILE",""+file);
                            }
                            out=new FileOutputStream(file);
                            Log.e("service","sdcard exist");
                        }
                        else {
                            out=openFileOutput("test.apk",Context.MODE_PRIVATE);
                        }

                        // BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        byte[] bytes=new byte[1024];
                        int len=0;
                        int count=0;
                        while ((len=in.read(bytes))!=-1){
                            out.write(bytes, 0, len);
                            count+=len;
                            int process=(count*100)/max;
                            Log.e("FILE",""+process+"%: "+count);

                        }
                        in.close();
                        out.close();
                    }
                    else {
                        Log.e("service","connet failed");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {

                    connection.disconnect();
                }

            }
        }).start();


    }

}
