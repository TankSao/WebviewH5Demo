package com.example.administrator.webviewdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.functions.Consumer;

import butterknife.internal.Utils;

import static com.example.administrator.webviewdemo.ForResult.CHOOSE_PICTURE;
import static com.example.administrator.webviewdemo.ForResult.CROP_SMALL_PICTURE;
import static com.example.administrator.webviewdemo.ForResult.TAKE_PICTURE;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private WebViewMod webview;
    private TextView tv;
    private JsInterface jsInterface;
    private DialogCircle newDialog;
    private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();
    private LinearLayout linearLayout;
    private String[] checkboxText;
    Uri uri;
    protected static Uri tempUri;
    private File picture;
    private static final int PERMISSON_REQUESTCODE = 0;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };

    private void refresh(int in) {
        // TODO Auto-generated method stub
        linearLayout.removeAllViews();
        checkBoxs.clear();
        for (int i = 0; i < in; i++) {
            // 先获得checkbox.xml的对象
            CheckBox checkBox = (CheckBox) getLayoutInflater().inflate(
                    R.layout.checkbox, null);
            checkBoxs.add(checkBox);
            checkBoxs.get(i).setText(checkboxText[i]);
            // 实现了在
            linearLayout.addView(checkBox, i);
        }
    }
    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }
    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                Log.e("err","ungranted");
            }
        }
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        checkPermissions(needPermissions);
        showPop();
    }
    @SuppressLint("JavascriptInterface")
    private void showPop() {
        //1填写金额  2立即充值
        View view = View.inflate(this, R.layout.activity_pop, null);


        checkboxText = new String[] { "A", "B",
                "C", "D","E","F","G","H","I","J","K","L","M","N"
                ,"O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        linearLayout = (LinearLayout) view.findViewById(R.id.ll);
        for (int i = 0; i < 4; i++) {
            // 先获得checkbox.xml的对象
            CheckBox checkBox = (CheckBox) getLayoutInflater().inflate(
                    R.layout.checkbox, null);
            checkBoxs.add(checkBox);
            checkBoxs.get(i).setText(checkboxText[i]);
            // 实现了在
            linearLayout.addView(checkBox, i);
        }
        final EditText et = (EditText) view.findViewById(R.id.et);
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable ete) {
                // TODO Auto-generated method stub
                if(ete.toString().length()>1){
                    Toast.makeText(getBaseContext(), "taichang", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(ete.toString())){
                }else{
                    //refresh
                    int in = Integer.parseInt(ete.toString());
                    refresh(in);
                }
            }
        });


        webview = (WebViewMod) view.findViewById(R.id.webview);
        jsInterface = new JsInterface(webview,new PicListener() {
            @Override
            public void takePic() {
                File file = new File(getExternalCacheDir(), "img.jpg");
                tempUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "image.jpg"));
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(getBaseContext(), "com.example.administrator.webviewdemo.fileprovider", file));
                } else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                }
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });
        tv = (TextView) findViewById(R.id.params);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用JavaScript
        webview.addJavascriptInterface(jsInterface, "jsInterface");
        //允许弹出框
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webview.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);//利用反射机制去修改设置对象
                if (method != null) {
                    method.invoke(webview.getSettings(), true);//修改设置
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        int width,height;
        webview.loadUrl("file:///android_asset/index.html");
        if (Build.VERSION.SDK_INT > 23) {
            width= 1024;
            height = 580;
        }else {
            width = 700;
            height = 580;
        }
        String src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528113069672&di=4ffc00f9b4da8cd7d104b70c3f1a27d1&imgtype=0&src=http%3A%2F%2Fimage.woshipm.com%2Fwp-files%2F2016%2F08%2Fbaiduwaimai.png";
        com.example.administrator.webviewdemo.Image im = new com.example.administrator.webviewdemo.Image();
        im.setHeight(height);
        im.setSourse(src);
        im.setWidth(width);
        Log.e("functionsss", "javascript:setImg()");
        webview.addJavascriptInterface(im, "im");

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                tv.setText("prompt方式，参数：" + message);
                // 调用一下cancel或者confirm都行
                result.cancel();
                return true;
            }
        });
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                tv.setText("url方式交互，url是：" + url);
                return true;
            }
        });
        newDialog = new DialogCircle(this, 1000, 700, view,
                R.style.dialog);
        newDialog.setCancelable(false);
        newDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (Build.VERSION.SDK_INT > 23) {
                        picture = new File(getExternalCacheDir() + "/img.jpg");
                        uri = FileProvider.getUriForFile(this, "com.example.administrator.webviewdemo.fileprovider", picture);
                        Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                        intent.putExtra("imgurl",picture.getAbsolutePath());
                        startActivity(intent);
                    } else {
                        startPhotoZoom(tempUri);// 开始对图片进行裁剪处理
                        picture = new File(tempUri.getPath());
                        Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                        intent.putExtra("imgurl",picture.getAbsolutePath());
                        startActivity(intent);
                    }
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        if (Build.VERSION.SDK_INT > 23) {
                            saveView(data);
                        }else {
                            if (picture != null) {
                                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                                intent.putExtra("imgurl",picture.getAbsolutePath());
                                startActivity(intent);
                            }
                        }
                    }
                    break;
            }
        }
    }

    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        picture = new File(tempUri.getPath());
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //保存裁剪后的图片
    protected void saveView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String imagePath = null;
            imagePath = com.example.administrator.webviewdemo.Utils.savePhoto(photo, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), String
                    .valueOf(System.currentTimeMillis()));
            if (imagePath != null) {
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                intent.putExtra("imgurl",imagePath);
                startActivity(intent);
            }
        }
    }
}
