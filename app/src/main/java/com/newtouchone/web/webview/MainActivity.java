package com.newtouchone.web.webview;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends ActionBarActivity implements CordovaInterface {

    private static final String TAG = "CordovaActivity";
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    protected CordovaPlugin activityResultCallback = null;
    private CordovaWebView mWebView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_main);
        Config.init(this);

        mWebView = (CordovaWebView) findViewById(R.id.webview);

        SharedPreferences preferences = this.getSharedPreferences("newtouchone-h5",  MODE_PRIVATE);
        String value = preferences.getString("url", null);

        if(value != null && !value.equals("") && !value.equals("http://")){
            url = value;
            mWebView.loadUrl(url);
        } else {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.handleDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = this.getSharedPreferences("newtouchone-h5",  MODE_PRIVATE);
        String value = preferences.getString("url", null);
        if(value != null && !value.equals(url)){
            url = value;
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void startActivityForResult(CordovaPlugin command, Intent intent,
                                       int requestCode) {
        this.activityResultCallback = command;
        super.startActivityForResult(intent, requestCode);
    }
    @Override
    public void setActivityResultCallback(CordovaPlugin plugin) {
        this.activityResultCallback = plugin;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        CordovaPlugin callback = this.activityResultCallback;
        Log.d(TAG, "CordovaActivity------>onActivityResult");
        if(callback != null) {
            Log.d(TAG, "We have a callback to send this result to");
            callback.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }
    @Override
    public Object onMessage(String id, Object data) {
        return null;
    }
    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
