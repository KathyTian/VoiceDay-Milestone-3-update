package com.iflytek.voicedemo;

/**
 * Created by Astrid_Ti on 2017/3/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private MySQLiteHelper myHelper;
    public static final String login = "login";
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tv2=(TextView)findViewById(R.id.tv2);
        myHelper = new MySQLiteHelper(this,"my.db",null,1);
    }

    public void login(View view) {
        myHelper = new MySQLiteHelper(this,"my.db",null,1);
        TextView userName = (TextView) findViewById(R.id.username);
        TextView passWord = (TextView) findViewById(R.id.password);
        if (userName.getText().toString().equals("") ||passWord.getText().toString().equals("")) {
            tv2.setTextColor(Color.RED);
            tv2.setText("Invalid Input, Please try again.");
        }
        else {
            queryUser(myHelper,userName.getText().toString(),passWord.getText().toString(),tv2);
        }


    }

    public void queryUser(MySQLiteHelper myHelper,String userName, String passWord,TextView tv2){
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("user_table", null, null, null, null, null, "id asc");
        int valueIndex1 = cursor.getColumnIndex("userName");
        int flagIndex1 = cursor.getColumnIndex("passWord");
        boolean success = false;
        System.out.println(userName);
        System.out.println(passWord);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            if (cursor.getString(valueIndex1).equals(userName) && cursor.getString(flagIndex1).equals(passWord)) {
                Intent intent = new Intent(this, MainPage.class);
                success = true;
                intent.putExtra(login,userName);
                startActivity(intent);
            }
        }
        if(!success) {
            tv2.setTextColor(Color.RED);
            tv2.setText("Can't find user, Please try again.");
        }
        cursor.close();
        db.close();
    }

    public void register(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
