package com.iflytek.voicedemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	
	public MySQLiteHelper(Context context, String name, CursorFactory factory,
						  int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists negation_table("
				+ "id integer primary key,"
				+ "value varchar ,"
				+ "flag varchar)");
		//negative_table
		db.execSQL("create table if not exists negative_table("
				+ "id integer primary key,"
				+ "value varchar ,"
				+ "flag integer)");
		//positive_table
		db.execSQL("create table if not exists positive_table("
				+ "id integer primary key,"
				+ "value varchar ,"
				+ "flag integer)");
		db.execSQL("create table if not exists user_table("
				+ "id integer primary key autoincrement,"
				+ "userName varchar ,"
				+ "passWord varchar)");
		db.execSQL("create table if not exists diary_table("
				+ "_id integer primary key autoincrement,"
				+ "userName varchar ,"
				+ "sentiment varchar ,"
				+ "permission varchar default 'private',"
				+ "diary varchar)");
		db.execSQL("create table if not exists mood_table("
				+ "_id integer primary key autoincrement,"
				+ "userName varchar ,"
				+ "positive integer ,"
				+ "negative integer ,"
				+ "neutral integer)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS negation_table");
		db.execSQL("DROP TABLE IF EXISTS negative_table");
		db.execSQL("DROP TABLE IF EXISTS positive_table");
		db.execSQL("DROP TABLE IF EXISTS user_table");
		db.execSQL("DROP TABLE IF EXISTS diary_table");
		db.execSQL("DROP TABLE IF EXISTS mood_table");
        onCreate(db);
	}




}
