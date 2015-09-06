package com.hyyy.gallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hyyy.gallery.R;
import com.hyyy.gallery.utils.BitmapTask;
import com.hyyy.gallery.utils.LruCacheUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/9/6.
 */
public class GalleryAdapter extends ArrayAdapter<String> implements AbsListView.OnScrollListener{

    private GridView mGridView;
    private Set<BitmapTask> mBitmapTasks;   //所有下载任务集合
    private int mFirstVisibleItem;  //第一张可见图片的下标
    private int mVisibleItemCount;  //屏幕的图片可见数
    private boolean isFirstEnter = true;   //是否首次进入程序

    public GalleryAdapter(Context context, int resource, String[] objects, GridView gridView) {
        super(context, resource, objects);
        this.mGridView = gridView;
        mBitmapTasks = new HashSet<BitmapTask>();
        mGridView.setOnScrollListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = getItem(position);
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_layout, null);
        }else{
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.photo_layout);
        imageView.setTag(url);
        setImageView(url, imageView);
        return view;
    }

    /**
     * 设置图片，先从LruCache中取出缓存图片，如果没有缓存图片，那么就设置成默认图片。
     * @param url
     * @param imageView
     */
    private void setImageView(String url, ImageView imageView) {
        Bitmap bitmap = LruCacheUtils.getBitmapFromCache(url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{
            imageView.setImageResource(R.mipmap.default_image);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当GridView滑动时，取消正在下载的图片，当GridView静止时，下载图片
        if(scrollState == SCROLL_STATE_IDLE){
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
        }else{
            cancelAllTasks();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //滑动时取得当前首个图片的下标和总可见图片数
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;

        //首次进入程序并不会调用onScrollStateChanged()，因此首次启动执行预加载图片
        if(isFirstEnter && visibleItemCount > 0){
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
            isFirstEnter = false;
        }

    }
}
