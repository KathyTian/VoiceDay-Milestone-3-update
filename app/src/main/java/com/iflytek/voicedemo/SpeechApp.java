package com.iflytek.voicedemo;

import android.app.Application;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;

public class SpeechApp extends Application {

	@Override
	public void onCreate() {
		
		SpeechUtility.createUtility(SpeechApp.this, "appid=" + getString(R.string.app_id));

		super.onCreate();
	}
	
}
