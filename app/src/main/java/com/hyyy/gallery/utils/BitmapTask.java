package com.hyyy.gallery.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 异步下载图片
 */
public class BitmapTask extends AsyncTask<String, Void, Bitmap>{

    private String imageUrl;    //图片的URL

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];

        //开始下载图片
        Bitmap bitmap = downloadBitmap(params[0]);
        if(bitmap != null){
            //下载图片后，将图片缓存到LruCache中
            LruCacheUtils.addBitmapToCache(params[0], bitmap);
        }
        return bitmap;
    }

    /**
     * 利用imageUrl获取其Bitmap对象
     * @param imageUrl
     * @return
     */
    private Bitmap downloadBitmap(String imageUrl) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;

        try {
            URL url = new URL(imageUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5*1000);
            con.setReadTimeout(10 * 1000);
            bitmap = BitmapFactory.decodeStream(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(con != null){
                con.disconnect();
            }
        }
        return bitmap;
    }
}
