package com.iflytek.voicedemo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import org.achartengine.GraphicalView;

import static com.iflytek.voicedemo.Diary.negative2;
import static com.iflytek.voicedemo.Diary.neutral2;
import static com.iflytek.voicedemo.Diary.positive2;

/**
 * Created by Kathy.Tian on 5/10/17.
 */

public class Tab4_image extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_image, container, false);
        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

        Button newPage = (Button) rootView.findViewById(R.id.button_recognize);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Recognize.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void activityRecognize(View v) {
        Intent intent = new Intent(getActivity(), Recognize.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
