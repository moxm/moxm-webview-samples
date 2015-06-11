package com.newtouchone.web.webview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.newtouchone.web.event.UrlChangedEvent;
import com.newtouchone.web.otto.BusProvider;

/**
 * Created by Richard on 15/6/11.
 */
public class SettingActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = this.getSharedPreferences("newtouchone-h5",  MODE_PRIVATE);
        String value = preferences.getString("url", "http://");

        EditText text = (EditText) findViewById(R.id.edit_url);
        text.setText(value);
    }

    public void onSaveClick(View v) {


        EditText text = (EditText) findViewById(R.id.edit_url);
        String value = text.getText().toString();


        Log.d("+++++++++++++++", value);

        if(value == null || value.equals("") || value.equals("http://")){
            Toast.makeText(this, "值为空", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = this.getSharedPreferences("newtouchone-h5",  MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("url", value);
        editor.commit();

        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();

        BusProvider.getInstance().post(new UrlChangedEvent(value));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
