package com.example.lenovo.downloaddemo;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2016/5/27.
 */
public class DownAnsycTask extends AsyncTask <Void, Integer, Boolean>{

    private String downloadUrl;// 下载链接地址
    private int threadNum;// 开启的线程数
    private File filePath;// 保存文件路径地址
    private int blockSize;// 每一个线程的下载量
    DownloadHelper downloadHelper;
    public boolean flag=true;

    public DownAnsycTask(String downloadUrl, int threadNum, File fileptah, DownloadHelper downloadHelper) {
        this.downloadUrl = downloadUrl;
        this.threadNum = threadNum;
        this.filePath = fileptah;
        this.downloadHelper=downloadHelper;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        DownThread[] threads = new DownThread[threadNum];
        try{
            URL url = new URL(downloadUrl);
            Log.e("URL", "download file http path:" + downloadUrl);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            // 读取下载文件总大小
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                return false;
            }
            blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
                    : fileSize / threadNum + 1;

            Log.e("URL", "fileSize:" + fileSize + "  blockSize:"+blockSize);

            //File file = new File(filePath);
            for (int i = 0; i < threads.length; i++) {
                // 启动线程，分别下载每个线程需要下载的部分
                //threads[i] = new DownThread(url, filePath, blockSize,
                 //       (i + 1));
                if(!downloadHelper.isExist(i+1)){
                    ContentValues values=new ContentValues();
                    values.put("id", i+1);
                    values.put("length",0);
                    downloadHelper.saveSqlite(values);
                }
                threads[i]=new DownThread(url, filePath, i*blockSize+downloadHelper.query(i+1),(i+1)*blockSize-1, i+1, downloadHelper);
                threads[i].setName("Thread:" + i);
                threads[i].start();



            }

            boolean isfinished = false;
            int downloadedAllSize = 0;
            while (!isfinished) {
                isfinished = true;
                // 当前所有线程下载总量
                downloadedAllSize = 0;
                for (int i = 0; i < threads.length; i++) {
                    if(!flag){
                        threads[i].flag=false;
                    }
                    downloadedAllSize += threads[i].getDownloadLength();

                    if (!threads[i].isCompleted()) {
                        isfinished = false;
                    }
                   // downloadHelper.query(i+1);
                }
                publishProgress(downloadedAllSize);

            }
        }catch (IOException e){

        }

        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values){
       // Log.e("percent", values[0]+"");
    }

    @Override
    protected void onPostExecute(Boolean result){

        Log.e("percent", "finish");
    }

}
