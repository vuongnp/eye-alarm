package com.example.eyealarm.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//
//
//
//public class ImageAdapter extends BaseAdapter {
//    private Context context;
//    private String[] list;
//
//    public ImageAdapter(Context c) {
//        context = c;
//        try {
//            list = context.getAssets().list("imgs");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int getCount() {
//        return list.length;
//    }
//
//    public Object getItem(int position) {
//        return null;
//    }
//
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView img;
//
//        if (convertView == null) {
//
//            img = new ImageView(context);
//            LinearLayout.LayoutParams params = new      LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,   ViewGroup.LayoutParams.WRAP_CONTENT);
//            img.setLayoutParams(new GridView.LayoutParams(params));
//            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
////            img.setPadding(8, 8, 8, 8);
//
//        } else {
//
//            img = (ImageView) convertView;
//        }
//        try {
//
//            InputStream ims = context.getAssets().open("imgs/" + list[position]);
//
//            //Drawable d = Drawable.createFromStream(ims, null);
//            Bitmap bitmap = BitmapFactory.decodeStream(ims);
//            img.setImageBitmap(bitmap);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return img;
//    }}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.eyealarm.R;

public class ImageAdapter extends BaseAdapter {
    Context context;
    int logos[];
    LayoutInflater inflter;
    public ImageAdapter(Context applicationContext, int[] logos) {
        this.context = applicationContext;
        this.logos = logos;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return logos.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.activity_gridview_image); // get the reference of ImageView
        icon.setImageResource(logos[i]); // set logo images
        return view;
    }
}

