package com.iflytek.voicedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emokit.sdk.util.SDKAppInit;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.model.request.RequestDoc;
import com.iflytek.model.request.RequestDocIncludeLanguage;
import com.iflytek.model.request.keyphrases_sentiment.TextRequest;
import com.iflytek.model.request.language.LanguageRequest;
import com.iflytek.model.response.sentiment.SentimentResponse;
import com.iflytek.retrofit.ServiceCall;
import com.iflytek.retrofit.ServiceCallback;
import com.iflytek.retrofit.ServiceRequestClient;
import com.iflytek.speech.setting.IatSettings;
import com.iflytek.speech.util.ApkInstaller;
import com.iflytek.speech.util.JsonParser;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.iflytek.voicedemo.MainActivity.login;

//import static com.iflytek.voicedemo.MainActivity.login;

public class IatDemo extends Activity implements OnClickListener {
	//Updated Sentimental analysis variables************************************-->
	private String mSubscriptionKey="7ad0f620da984d47be06bd9212393d51";

	private static final String TAGs = IatDemo.class.getSimpleName();


	private TextView mSentimentScore;
	private TextView mSentimentScore2;
	private ProgressDialog mProgressDialog;

	// Network request
	private ServiceRequestClient mRequest;
	private RequestDoc mDocument;
	private LanguageRequest mLanguageRequest;       // request for language detection
	private RequestDocIncludeLanguage mDocIncludeLanguage;
	private TextRequest mTextIncludeLanguageRequest;               // request for key phrases and sentiment analysis

	private ServiceCall mSentimentCall;
	private ServiceCallback mSentimentCallback;
	//Updated Sentimental analysis variables************************************-->

	private static String TAG = IatDemo.class.getSimpleName();
	private SpeechRecognizer mIat;

	private RecognizerDialog mIatDialog;

	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private EditText mResultText;
	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences msharedPre;

	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	ApkInstaller mInstaller;
	private MySQLiteHelper myHelper;
	private TextView tv1;
	private TextView azure;
	String resultText="";
	String resultKeyWord="";
	private ProgressDialog pdialog;
	public static final String textKey = "textKey";
	public static final String sentiment = "sentiment";
	public static final String name = "name";

	public static final String positive="positive";
	public static final String negative="negative";
	public static final String neutral="neutral";

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.iatdemo);
		SDKAppInit.createInstance(this);

		mRequest = new ServiceRequestClient(mSubscriptionKey);

		myHelper = new MySQLiteHelper(this,"my.db",null,1);
		msharedPre=getSharedPreferences("LoadParameter", Context.MODE_PRIVATE);
		String flag=msharedPre.getString("isFirst", "false");
		if(flag.equals("false"))//第一次加载
		{
			insertDataInNegation(myHelper);
			insertDataInNegative(myHelper);
			insertDataInPositive(myHelper);
			System.out.println("I'tried insert");
			SharedPreferences.Editor editor= msharedPre.edit();
			editor.putString("isFirst", "true");
			editor.commit();
		}
		tv1=(TextView)findViewById(R.id.tv1);
		azure=(TextView)findViewById(R.id.azure);
		initLayout();

		mIat = SpeechRecognizer.createRecognizer(IatDemo.this, mInitListener);

		mIatDialog = new RecognizerDialog(IatDemo.this, mInitListener);

		mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME,
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mResultText = ((EditText) findViewById(R.id.iat_text));
		mInstaller = new ApkInstaller(IatDemo.this);
	}
	//*******************************sentimental analysis******************************************
	/* Insert data in negation_table
	*/
	public void insertDataInNegation(MySQLiteHelper myHelper){
		SQLiteDatabase db = myHelper.getWritableDatabase();

		ContentValues values=new ContentValues();
		values.put("id", 1);
		values.put("value", "not");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 2);
		values.put("value", "no");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 3);
		values.put("value", "hardly");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 4);
		values.put("value", "scarcely");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 5);
		values.put("value", "seldom");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 6);
		values.put("value", "don't");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 7);
		values.put("value", "doesn't");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();


		values.put("id", 8);
		values.put("value", "didn't");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();

		values.put("id", 9);
		values.put("value", "haven't");
		values.put("flag", "y");
		db.insert("negation_table", null, values);
		values.clear();
		System.out.println("finish inserting in negate table");
		db.close();

	}

	/**
	 * Insert data in positive_table
	 */
	public void insertDataInPositive(MySQLiteHelper myHelper){
		SQLiteDatabase db = myHelper.getWritableDatabase();


		ContentValues values=new ContentValues();
		values.put("id", 1);
		values.put("value", "relaxed");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 2);
		values.put("value", "happy");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 3);
		values.put("value", "bright");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 4);
		values.put("value", "free");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 5);
		values.put("value", "comfortable");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 6);
		values.put("value", "pleased");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 7);
		values.put("value", "good");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 8);
		values.put("value", "fine");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 9);
		values.put("value", "easy");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 10);
		values.put("value", "wonderful");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 11);
		values.put("value", "frisky");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();


		values.put("id", 12);
		values.put("value", "excited");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 13);
		values.put("value", "thrilled");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 14);
		values.put("value", "fascinated");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 15);
		values.put("value", "enthusiastic");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 16);
		values.put("value", "inspired");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 17);
		values.put("value", "warm");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 18);
		values.put("value", "lucky");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 19);
		values.put("value", "great");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 20);
		values.put("value", "optimistic");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 21);
		values.put("value", "reliable");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();


		values.put("id", 22);
		values.put("value", "confident");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 23);
		values.put("value", "amazing");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 24);
		values.put("value", "satisfied");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 25);
		values.put("value", "satisfying");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 26);
		values.put("value", "interested");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 27);
		values.put("value", "interesting");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 28);
		values.put("value", "joyous");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 29);
		values.put("value", "joy");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 30);
		values.put("value", "attractive");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 31);
		values.put("value", "love");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 32);
		values.put("value", "loved");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 33);
		values.put("value", "tender");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 34);
		values.put("value", "earnest");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 35);
		values.put("value", "brave");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 36);
		values.put("value", "comforted");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 37);
		values.put("value", "thankful");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 38);
		values.put("value", "sunny");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 39);
		values.put("value", "merry");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 40);
		values.put("value", "jubilant");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 41);
		values.put("value", "alive");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 42);
		values.put("value", "playful");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 43);
		values.put("value", "cheerful");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 44);
		values.put("value", "glad");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();

		values.put("id", 45);
		values.put("value", "liberated");
		values.put("flag", "0");
		db.insert("positive_table", null, values);
		values.clear();
		System.out.println("finish inserting in positive table");
		db.close();
	}


	/**
	 * 向数据库的negative_table表插入数据。
	 */
	public void insertDataInNegative(MySQLiteHelper myHelper){
		SQLiteDatabase db = myHelper.getWritableDatabase();

		/*db.execSQL("create table if not exists negative_table("
				+ "id integer,"
				+ "value varchar primary key,"
				+ "flag integer)");*/

		//插入数据
		ContentValues values=new ContentValues();

		//否定词表
		values.put("id", 1);
		values.put("value", "sad");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 2);
		values.put("value", "dull");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 3);
		values.put("value", "hurt");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 4);
		values.put("value", "afraid");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 5);
		values.put("value", "anxious");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 6);
		values.put("value", "bored");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 7);
		values.put("value", "worried");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 8);
		values.put("value", "angry");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 9);
		values.put("value", "unhappy");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 10);
		values.put("value", "lonely");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 11);
		values.put("value", "confused");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 12);
		values.put("value", "helpless");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 13);
		values.put("value", "alone");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 14);
		values.put("value", "annoyed");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 15);
		values.put("value", "dissatisfied");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 16);
		values.put("value", "unpleasant");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 17);
		values.put("value", "miserable");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 18);
		values.put("value", "desperate");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 19);
		values.put("value", "terrible");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 20);
		values.put("value", "bad");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 21);
		values.put("value", "hostile");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 22);
		values.put("value", "discouraged");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 23);
		values.put("value", "useless");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 24);
		values.put("value", "perplexed");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 25);
		values.put("value", "ashamed");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 26);
		values.put("value", "hesitant");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 27);
		values.put("value", "vulnerable");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 28);
		values.put("value", "shy");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 29);
		values.put("value", "offensive");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 30);
		values.put("value", "bitter");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 31);
		values.put("value", "resentful");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 32);
		values.put("value", "uneasy");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 33);
		values.put("value", "infuriated");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 34);
		values.put("value", "tragic");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 35);
		values.put("value", "provoked");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 36);
		values.put("value", "boiling");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 37);
		values.put("value", "misgiving");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 38);
		values.put("value", "sulky");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 39);
		values.put("value", "disgusting");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();

		values.put("id", 40);
		values.put("value", "awful");
		values.put("flag", 1);
		db.insert("negative_table", null, values);
		values.clear();
		db.close();
		System.out.println("finish inserting in negative table");
	}


	/**
	 * Query data in negation_table
	 */
	public String queryNegationData(MySQLiteHelper myHelper, String text){
		String result = "";
		SQLiteDatabase db = myHelper.getReadableDatabase();
		Cursor cursor = db.query("negation_table",null,null,null,null,null,"id asc");
		int valueIndex1 = cursor.getColumnIndex("value");
		int flagIndex1 = cursor.getColumnIndex("flag");


		String[] splitArray=text.split("[ ]");
		for(int i=0;i<splitArray.length;i++){

			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				if(cursor.getString(valueIndex1).equalsIgnoreCase(splitArray[i])){
//				result = result +cursor1.getString(valueIndex1)+"\t\t";
					result = result +cursor.getString(flagIndex1)+i+" ";
				}
			}

		}

		cursor.close();

		db.close();
		System.out.println("this is the result of the negate table");
		System.out.println(result);
		return result;

	}
	/**
	 * query data in negative_table
	 */
	public String queryNegativeData(MySQLiteHelper myHelper, String text){
		String result = "";
		SQLiteDatabase db = myHelper.getReadableDatabase();
		Cursor cursor = db.query("negative_table",null,null,null,null,null,"id asc");
		int valueIndex = cursor.getColumnIndex("value");
		int flagIndex = cursor.getColumnIndex("flag");

		String[] splitArray=text.split("[ ]");

		for(int i=0;i<splitArray.length;i++){

			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				if(cursor.getString(valueIndex).equalsIgnoreCase(splitArray[i])){
//				result = result +cursor1.getString(valueIndex)+"\t\t";
					result = result +cursor.getInt(flagIndex)+i+" ";
				}
			}

		}

		cursor.close();

		db.close();
		System.out.println("this is the result of the negative table");
		System.out.println(result);
		return result;

	}

	/**
	 * query data in positive_table
	 */
	public String queryPositiveData(MySQLiteHelper myHelper, String text){
		String result = "";
		SQLiteDatabase db = myHelper.getReadableDatabase();
		Cursor cursor = db.query("positive_table",null,null,null,null,null,"id asc");
		int valueIndex = cursor.getColumnIndex("value");
		int flagIndex = cursor.getColumnIndex("flag");
		String[] splitArray=text.split("[ ]");
		for(int i=0;i<splitArray.length;i++){

			for(cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()){
				if(cursor.getString(valueIndex).equalsIgnoreCase(splitArray[i])){
//				result = result +cursor1.getString(valueIndex)+"\t\t";
					result = result +cursor.getInt(flagIndex)+i+" ";
				}
			}

		}


		cursor.close();

		db.close();
		System.out.println("this is the result of the positive table");
		System.out.println(result);
		return result;

	}
	//*****************************sentimental analysis****************************************
	/**
	 * 初始化Layout。
	 */
	private void initLayout() {
		findViewById(R.id.iat_recognize).setOnClickListener(IatDemo.this);
		((TextView) findViewById(R.id.get_sentiment_score)).setOnClickListener(this);
		mSentimentScore = (TextView) findViewById(R.id.sentiment_score);
		mSentimentScore2 = (TextView) findViewById(R.id.our_score);
		findViewById(R.id.image_iat_set).setOnClickListener(IatDemo.this);

	}

	int ret = 0;

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

			case R.id.image_iat_set:
				Intent intents = new Intent(IatDemo.this, IatSettings.class);
				startActivity(intents);
				break;

			case R.id.get_sentiment_score:
				getSentimentScore();
				break;
			case R.id.iat_recognize:

				FlowerCollector.onEvent(IatDemo.this, "iat_recognize");

				mResultText.setText(null);
				mIatResults.clear();

				setParam();
				boolean isShowDialog = mSharedPreferences.getBoolean(
						getString(R.string.pref_key_iat_show), true);
				if (isShowDialog) {

					mIatDialog.setListener(mRecognizerDialogListener);
					mIatDialog.show();
					mIatDialog.hide();
					pdialog=new ProgressDialog(IatDemo.this);
					pdialog.setMessage("Please speak to the microphone!");
					pdialog.setCancelable(true);
					pdialog.show();
				} else {

					ret = mIat.startListening(mRecognizerListener);
					if (ret != ErrorCode.SUCCESS) {
						showTip("wrong code：" + ret);
					} else {
						showTip(getString(R.string.text_begin));
					}
				}
				break;

			default:
				break;
		}
	}


	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("wrong code" + code);
			}
		}
	};


	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			showTip("begin speaking");
		}

		@Override
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {

			showTip("end speaking");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());


			if (isLast) {

			}
			printResult(results);
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			showTip("volume：" + volume);
			Log.d(TAG, "return data："+data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	private void printResult(RecognizerResult results) {
		pdialog.dismiss();

		String text = JsonParser.parseIatResult(results.getResultString());
		System.out.println("text" +
				"" +
				":"+text);
		String sn = null;
		MySQLiteHelper myHelper;
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		mResultText.setText(text);
		mResultText.setSelection(text.length());
		resultText=resultBuffer.toString();
		//**************************************set updated sentimentalAnalysis**************
		// Request: text without language
		mDocument = new RequestDoc();
		mDocument.setId("1");
		mDocument.setText(resultText);
		List<RequestDoc> documents = new ArrayList<>();
		documents.add(mDocument);
		mLanguageRequest = new LanguageRequest(documents);

		// Request: text with language hard-coded to "en" for demo purpose, not production quality
		mDocIncludeLanguage = new RequestDocIncludeLanguage();
		mDocIncludeLanguage.setId("1");
		mDocIncludeLanguage.setLanguage("en");
		mDocIncludeLanguage.setText(resultText);
		List<RequestDocIncludeLanguage> textDocs = new ArrayList<>();
		textDocs.add(mDocIncludeLanguage);
		mTextIncludeLanguageRequest =  (TextRequest) new TextRequest(textDocs);
		//***************************************
		myHelper = new MySQLiteHelper(this,"my.db",null,1);
		String result = "";
		String negationResult= queryNegationData(myHelper,resultBuffer.toString());
		String positiveResult=queryPositiveData(myHelper,resultBuffer.toString());
		String negativeResult = queryNegativeData(myHelper,resultBuffer.toString());
		result = negationResult+negativeResult+positiveResult;  //""
		String[] splitResultArray=result.split("[ ]");
//		String[] splitResultArray=result.split(" ");
		String orderedResult="";//排序后的结果如y011
		String processedResult=""; //去掉y之后待数个数比较的结果
		int count0=0,count1=0;//数0和1的个数
		String keywordResult="";//word bag算法最终结果
		myHelper = new MySQLiteHelper(this,"my.db",null,1);
		SQLiteDatabase db = myHelper.getReadableDatabase();
		myHelper = new MySQLiteHelper(this,"my.db",null,1);
		SQLiteDatabase db2 = myHelper.getWritableDatabase();
		ContentValues newValues = new ContentValues();
		Intent intent=getIntent();
		String username = intent.getStringExtra(login);
		Cursor c=getTermValues(username,db);
		int posi=0;
		int nega=0;
		int neut=0;
		int id=0;
		if(c != null)
		{
			while(c.moveToNext()){
				posi = c.getInt(c.getColumnIndex("positive"));
				nega = c.getInt(c.getColumnIndex("negative"));
				neut = c.getInt(c.getColumnIndex("neutral"));
				id = c.getInt(c.getColumnIndex("_id"));
			}
		}
		try{
			//用冒泡算法对分割后的结果排序
			for(int i=0;i<splitResultArray.length;i++)
			{

				for(int j=i;j<splitResultArray.length;j++)
				{
					if(Integer.parseInt(splitResultArray[i].substring(1))>Integer.parseInt(splitResultArray[j].substring(1))){

						String temp = splitResultArray[i];
						splitResultArray[i]=splitResultArray[j];
						splitResultArray[j]=temp;

					}


				}

			}
			//排序后提取flag值，这样就ordered了
			for(int i=0;i<splitResultArray.length;i++){

				orderedResult=orderedResult+splitResultArray[i].substring(0,1);
			}

			for(int i=0;i<orderedResult.length();i++){


				if(orderedResult.charAt(i)=='y'&&orderedResult.charAt(i+1)!='\0')
				{
					if(orderedResult.charAt(i+1)=='1')
					{
						processedResult=processedResult+"0";
						i++;
					}
					else if(orderedResult.charAt(i+1)=='0')
					{
						processedResult=processedResult+"1";
						i++;
					}
				}
				else{

					processedResult=processedResult+String.valueOf(orderedResult.charAt(i));
				}

			}

			for(int i=0;i<processedResult.length();i++){

				if(processedResult.charAt(i)=='0')
				{count0++;}
				else if(processedResult.charAt(i)=='1')
				{count1++;}
			}

			if((count0==0)&&(count1>0)){
				keywordResult="Negative";
				tv1.setTextColor(Color.BLUE);
				mSentimentScore2.setText("0.00");


			}
			else if((count1==0)&&(count0>0)){
				keywordResult="Positive";
				tv1.setTextColor(Color.RED);
				mSentimentScore2.setText("1.00");

			}
			else{
				keywordResult="Neutral";
				tv1.setTextColor(Color.GREEN);
				mSentimentScore2.setText("0.50");


			}

			if(keywordResult.equals("Positive")){
				newValues.put("positive",posi+1);
				db2.update("mood_table", newValues, "_id="+id, null);

			}else if(keywordResult.equals("Negative")){
				newValues.put("negative",nega+1);
				db2.update("mood_table", newValues, "_id="+id, null);

			}else if(keywordResult.equals("Neutral")){
				newValues.put("neutral",neut+1);
				db2.update("mood_table", newValues, "_id="+id, null);

			}
			tv1.setText(keywordResult);
			System.out.println("here");

		}catch(Exception e)
		{
			newValues.put("neutral",neut+1);
			db2.update("mood_table", newValues, "_id="+id, null);
			resultKeyWord="Neutral";


			tv1.setText(resultKeyWord);
			tv1.setTextColor(Color.GREEN);
			mSentimentScore2.setText("0.50");
			System.out.println("exception e:"+e);
		}
	}

	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			if(isLast==true) return;
			printResult(results);
		}

		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	public void setParam() {
		mIat.setParameter(SpeechConstant.PARAMS, null);
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");

		mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

		mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
	}

	public void Submit(View view){
		myHelper = new MySQLiteHelper(this,"my.db",null,1);
		SQLiteDatabase db2 = myHelper.getReadableDatabase();
		Intent intent = getIntent();
		String Username = intent.getStringExtra(login);
		TextView Senti = (TextView)findViewById(R.id.tv1);
		EditText Word = (EditText)findViewById(R.id.iat_text);
		Cursor c=getTermValues(Username,db2);
		int posi1=0;
		int nega1=0;
		int neut1=0;
		int id1=0;
		if(c != null)
		{
			while(c.moveToNext()){
				posi1 = c.getInt(c.getColumnIndex("positive"));
				nega1 = c.getInt(c.getColumnIndex("negative"));
				neut1 = c.getInt(c.getColumnIndex("neutral"));
				id1 = c.getInt(c.getColumnIndex("_id"));
			}
		}
		Intent intent2 = new Intent(this, Diary.class);
		intent2.putExtra(textKey, Word.getText().toString());
		intent2.putExtra(name,Username);
		intent2.putExtra(sentiment,Senti.getText().toString());
		intent2.putExtra(positive,posi1);
		intent2.putExtra(negative,nega1);
		intent2.putExtra(neutral,neut1);
		startActivity(intent2);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mIat.cancel();
		mIat.destroy();
	}

	@Override
	protected void onResume() {
		FlowerCollector.onResume(IatDemo.this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}
	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle(getString(R.string.progress_bar_title));
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
	@Override
	protected void onPause() {
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(IatDemo.this);
		super.onPause();


		if (mSentimentCall != null && !mSentimentCall.isCancelled()) {
			mSentimentCall.cancel();
		}
		// Dismiss dialog
		dismissProgressDialog();

	}
	private void getSentimentScore() {
		showProgressDialog();

		mSentimentCallback = new ServiceCallback(mRequest.getRetrofit()) {
			@Override
			public void onResponse(Call call, Response response) {
				super.onResponse(call, response);
				SentimentResponse sentimentResponse = (SentimentResponse) response.body();
				if (response != null && response.isSuccessful()) {
					String result = sentimentResponse.getDocuments().get(0).getScore().toString();
					mSentimentScore.setText(result);
					if(Float.parseFloat(result)<0.3){
						azure.setTextColor(Color.BLUE);
						azure.setText("Negative");

					}
					else if(Float.parseFloat(result)<0.6){
						azure.setTextColor(Color.GREEN);
						azure.setText("Neutral");
					}
					else{
						azure.setTextColor(Color.RED);
						azure.setText("Positive");
					}
				}
				dismissProgressDialog();
			}

			@Override
			public void onFailure(Call call, Throwable t) {
				super.onFailure(call, t);
				dismissProgressDialog();
			}
		};

		try {
			mSentimentCall = mRequest.getSentimentAsync(mTextIncludeLanguageRequest, mSentimentCallback);
		} catch (IllegalArgumentException e) {
			dismissProgressDialog();
			Toast.makeText(IatDemo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public Cursor getTermValues(String index, SQLiteDatabase db) {

		String from[] = { "_id", "positive", "neutral", "negative" };
		String where = "userName=?";
		String[] whereArgs = new String[]{index};
		Cursor cursor = db.query("mood_table", from, where, whereArgs, null, null, null, null);
		return cursor;
	}
}
