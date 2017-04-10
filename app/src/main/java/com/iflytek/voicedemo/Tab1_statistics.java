package com.iflytek.voicedemo;

/**
 * Created by Kathy.Tian on 17/4/5.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.achartengine.GraphicalView;

import static com.iflytek.voicedemo.Diary.positive2;
import static com.iflytek.voicedemo.Diary.negative2;
import static com.iflytek.voicedemo.Diary.neutral2;
public class Tab1_statistics extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_statistics, container, false);
        Bundle bundle =new Bundle();
        bundle=getArguments();
        int positive1 = 0;
        int negative1 = 0;
        int neutral1 = 0;
        if(bundle!=null) {
            positive1 = bundle.getInt(positive2,1);
            negative1 = bundle.getInt(negative2,1);
            neutral1 = bundle.getInt(neutral2,1);
        }
        //print the value
        //Toast.makeText(getActivity(), positive1 + " " + negative1 + " " + neutral1, Toast.LENGTH_LONG).show();
        PieGraph voicePie=new PieGraph();
        GraphicalView graphicalView=voicePie.getGraphicalView(getActivity(), positive1,neutral1,negative1);
        LinearLayout pieGraph=(LinearLayout) rootView.findViewById(R.id.pieGraph);
        pieGraph.addView(graphicalView);

        return rootView;
    }

}