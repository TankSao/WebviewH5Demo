package com.example.administrator.webviewdemo;

/**
 * Created by Administrator on 2018/10/9.
 */

import android.webkit.JavascriptInterface;

/**
 * Created by loonggg on 2017/5/11.
 */
public class Image {
    private String sourse;
    private int width;
    private int height;

    @JavascriptInterface
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @JavascriptInterface
    public int getHeight() {
        return height;
    }

    @JavascriptInterface
    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }
}