package com.iflytek.voicedemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.iflytek.voicedemo.MyAdapter.ViewHolder.id_adapter;
import static com.iflytek.voicedemo.MyAdapter.ViewHolder.sentiment_adapter;
import static com.iflytek.voicedemo.MyAdapter.ViewHolder.word_adapter;

/**
 * Created by ff_ti on 4/5/17.
 */

public class Details extends Activity {

    private MySQLiteHelper myHelper;
    public String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        // TODO get book as parcelable intent extra and populate the UI with book details.

        Intent result = getIntent();
        myHelper = new MySQLiteHelper(this,"my.db",null,1);
        String sentiment = result.getStringExtra(sentiment_adapter);
        String word = result.getStringExtra(word_adapter);
        id = result.getStringExtra(id_adapter);
        final TextView Status = (TextView) findViewById(R.id.sentiment);
        final TextView Word = (TextView) findViewById(R.id.word);
        Status.setText(sentiment);
        Word.setText(word);
    }

    public void MakePublic(View view) {

        Toast.makeText(this, "Save your preference!", Toast.LENGTH_LONG).show();
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("permission", "public");
        db.update("diary_table", cv, "_id="+id, null);
        db.close();
    }

}