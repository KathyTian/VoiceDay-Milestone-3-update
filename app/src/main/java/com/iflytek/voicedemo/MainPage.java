package com.iflytek.voicedemo;

/**
 * Created by Astrid_Ti on 2017/3/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.iflytek.sunflower.FlowerCollector;

import static com.iflytek.voicedemo.MainActivity.login;

public class MainPage extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toast mToast;
    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        SimpleAdapter listitemAdapter = new SimpleAdapter();
        ((ListView) findViewById(R.id.listview_main)).setAdapter(listitemAdapter);
    }

    @Override
    public void onClick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        Intent intent = null;
        switch (tag) {
            case 0:
                Intent intent1 = getIntent();
                String username = intent1.getStringExtra(login);
                intent = new Intent(MainPage.this, IatDemo.class);
                intent.putExtra(login,username);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
    String items[] = {"Start"};

    private class SimpleAdapter extends BaseAdapter {
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                LayoutInflater factory = LayoutInflater.from(MainPage.this);
                View mView = factory.inflate(R.layout.list_items, null);
                convertView = mView;
            }

            Button btn = (Button) convertView.findViewById(R.id.btn);
            btn.setOnClickListener(MainPage.this);
            btn.setTag(position);
            btn.setText(items[position]);

            return convertView;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onResume() {
        FlowerCollector.onResume(MainPage.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(MainPage.this);
        super.onPause();
    }
}
