package com.example.administrator.webviewdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import static com.example.administrator.webviewdemo.ForResult.TAKE_PICTURE;


import java.io.File;

/**
 * Created by Administrator on 2018/5/6.
 */

public class JsInterface {
    private WebView mWebView;
    private PicListener listener;
    // 构造方法，传入一个参数WebView
    public JsInterface(WebView webView,PicListener listener) {
        this.mWebView = webView;
        this.listener = listener;
    }

    @JavascriptInterface
    public void consoleE(final String str) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 主线程更新UI
                Log.e("JavaScriptHB",str);
            }
        });
    }

    @JavascriptInterface
    public void upTakenPic() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 主线程更新UI
                listener.takePic();
            }
        });
    }

}