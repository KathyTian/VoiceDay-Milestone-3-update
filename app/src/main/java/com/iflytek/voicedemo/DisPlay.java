package com.iflytek.voicedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static com.iflytek.voicedemo.IatDemo.textKey;
import static com.iflytek.voicedemo.MainActivity.login;

/**
 * Created by Astrid_Ti on 2017/3/16.
 */

public class DisPlay extends Activity {

    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;
    private MySQLiteHelper myHelper;

    @SuppressLint("NewApi")
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String text = intent.getStringExtra(textKey);
        setContentView(R.layout.cart);
        myHelper = new MySQLiteHelper(this, "my.db", null, 1);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ListView lv = (ListView) findViewById(android.R.id.list);

        String[] from = new String[]{"diary"};
        int[] to = new int[]{android.R.id.text1};
        simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to);
        lv.setAdapter(simpleCursorAdapter);
        ContentValues newValues = new ContentValues();
        String name = intent.getStringExtra(login);
        newValues.put("username", name);
        newValues.put("diary", text);
        db.insert("diary_table", null, newValues);
        Cursor cursor = getAllDiary(myHelper);
        simpleCursorAdapter.swapCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    public Cursor getAllDiary(MySQLiteHelper myHelper){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor =db.query("diary_table", null, null, null, null, null, "_id asc");
        return cursor;
    }
}
//        int nameIndex = cursor.getColumnIndex("userName");
//        int diaryIndex = cursor.getColumnIndex("diary");
//
//        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
//            System.out.println(cursor.getString(nameIndex));
//            System.out.println(cursor.getString(diaryIndex));
//        }
//
//        cursor.close();
//        db.close();