package com.hyyy.gallery.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * LruCache缓存工具类
 */
public class LruCacheUtils {

    /**
     * 创建LruChche缓存，大小为程序最大可用内存的1/8
     */
    private static LruCache<String, Bitmap> mLruCache =  new LruCache<String, Bitmap>(
            (int) Runtime.getRuntime().maxMemory()/8){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    /**
     * 将下载后的图片存储到LruCache中
     * @param key
     * @param bitmap
     */
    public static void addBitmapToCache(String key, Bitmap bitmap){
        if(getBitmapFromCache(key) == null){
            mLruCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取图片，如果没有图片的话就返回null
     * @param key
     * @return
     */
    public static Bitmap getBitmapFromCache(String key) {
        return mLruCache.get(key);
    }

}
