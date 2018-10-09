package com.example.administrator.webviewdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.webkit.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/24.
 */

public class CaptureTool {
    public static Bitmap captureWebView(WebView webView){
        float scale = webView.getScale();
        int width = webView.getWidth();
        int height = (int) (webView.getHeight() * scale);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        return bitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path, String filename) throws IOException
    {
        File file = new File(path + filename);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out))
            {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e("eeeeee1",e.getMessage());
            return  false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e("eeeeee2",e.getMessage());
            return  false;
        }
        return true;
    }
}
