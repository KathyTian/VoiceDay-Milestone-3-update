package com.iflytek.voicedemo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class PublicCycle extends Activity {

    private RecyclerView mRecyclerView;
    private MySecondAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Cursor cursor;
    private MySQLiteHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_cycle);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<String> myDataset1 = new ArrayList<String>() {
        };
        ArrayList<String> myDataset2 = new ArrayList<String>() {
        };
        ArrayList<String> myDataset3 = new ArrayList<String>() {
        };
        myHelper = new MySQLiteHelper(this, "my.db", null, 1);
        SQLiteDatabase db = myHelper.getReadableDatabase();

        cursor = getAllDiary(myHelper);
        mAdapter = new MySecondAdapter(myDataset1, myDataset2, myDataset3, this, cursor);
        mAdapter.swapCursor(cursor);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (cursor.getString(3).equals("public")) {
                myDataset1.add(cursor.getString(4));
                myDataset2.add(cursor.getString(2));
                myDataset3.add(cursor.getString(0));
            }
        }

        mRecyclerView.setAdapter(mAdapter);
    }

    public Cursor getAllDiary(MySQLiteHelper myHelper) {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("diary_table", null, null, null, null, null, "_id asc");
        return cursor;
    }
}