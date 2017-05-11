package com.iflytek.voicedemo;

/**
 * Created by Astrid_Ti on 2017/3/15.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Register extends Activity {
    private MySQLiteHelper myHelper;
    public static final String register = "register";
    private TextView tv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent intent = getIntent();
        tv3=(TextView)findViewById(R.id.tv3);
        myHelper = new MySQLiteHelper(this,"my.db",null,1);
    }
    public void Register(View view){
        myHelper = new MySQLiteHelper(this,"my.db",null,1);

        TextView userName = (TextView) findViewById(R.id.username1);
        TextView passWord = (TextView) findViewById(R.id.password1);


        if(queryUser(myHelper,userName.getText().toString(),passWord.getText().toString(),tv3)) {
            SQLiteDatabase db = myHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            ContentValues values_mood=new ContentValues();
//        values.put("id", 1);
            values.put("userName", userName.getText().toString());
            System.out.print(userName.getText().toString());
            values.put("passWord", passWord.getText().toString());
            values_mood.put("userName",userName.getText().toString());
            values_mood.put("positive",0);
            values_mood.put("neutral",0);
            values_mood.put("negative",0);
            db.insert("mood_table",null,values_mood);
            db.insert("user_table", null, values);
            values.clear();
            values_mood.clear();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(register, userName.getText().toString());
            startActivity(intent);
        }

    }
    public boolean queryUser(MySQLiteHelper myHelper, String userName, String passWord, TextView tv3){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("user_table", null, null, null, null, null, "id asc");
        int valueIndex1 = cursor.getColumnIndex("userName");
        int flagIndex1 = cursor.getColumnIndex("passWord");
        boolean success = false;
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (cursor.getString(valueIndex1).equals(userName)) {
                tv3.setTextColor(Color.RED);
                tv3.setText("The user name already exists, Please try again.");
                return false;
            }
        }
        cursor.close();
        db.close();
        return true;
    }
    public void login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        System.out.print("1");
        startActivity(intent);
    }

}
