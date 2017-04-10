package com.iflytek.voicedemo;

/**
 * Created by Kathy.Tian on 17/4/5.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.iflytek.voicedemo.Diary.sentim;
import static com.iflytek.voicedemo.Diary.username;
import static com.iflytek.voicedemo.Diary.word;

public class Tab2_diary extends Fragment {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Cursor cursor;
    private MySQLiteHelper myHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_diary, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // specify an adapter (see also next example)
        ArrayList<String> myDataset1 = new ArrayList<String>() {
        };
        ArrayList<String> myDataset2 = new ArrayList<String>() {
        };
        ArrayList<String> myDataset3 = new ArrayList<String>() {
        };
        myHelper = new MySQLiteHelper(getActivity(), "my.db", null, 1);
        SQLiteDatabase db = myHelper.getWritableDatabase();
//        Intent intent = getIntent();
//        String text = intent.getStringExtra(textKey);
//        String name = intent.getStringExtra(login);
//        String senti = intent.getStringExtra(sentiment);
        Bundle bundle = new Bundle();
        bundle = getArguments();
        String Word=bundle.getString(word);
        String Name = bundle.getString(username);
        String Senti = bundle.getString(sentim);

        ContentValues newValues = new ContentValues();
        newValues.put("username", Name);
        newValues.put("diary", Word);
        newValues.put("sentiment",Senti);
        db.insert("diary_table", null, newValues);

        cursor = getAllDiary(myHelper);
        mAdapter = new MyAdapter(myDataset1, myDataset2, myDataset3, getActivity(), cursor);
        mAdapter.swapCursor(cursor);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if(cursor.getString(1).equals(Name)) {
                myDataset1.add(cursor.getString(4));
                myDataset2.add(cursor.getString(2));
                myDataset3.add(cursor.getString(0));
                //myDataset3.add(cursor.getString(5));
            }
        }

        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    public Cursor getAllDiary(MySQLiteHelper myHelper) {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("diary_table", null, null, null, null, null, "_id asc");
        return cursor;
    }

}

