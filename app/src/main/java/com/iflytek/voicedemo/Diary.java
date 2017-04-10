package com.iflytek.voicedemo;

/**
 * Created by Kathy.Tian on 17/4/5.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static com.iflytek.voicedemo.IatDemo.name;
import static com.iflytek.voicedemo.IatDemo.negative;
import static com.iflytek.voicedemo.IatDemo.neutral;
import static com.iflytek.voicedemo.IatDemo.positive;
import static com.iflytek.voicedemo.IatDemo.sentiment;
import static com.iflytek.voicedemo.IatDemo.textKey;

public class Diary extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String sentim = "test";

    public static final String username = "username";

    public static final String word = "word";

    String Senti;

    String UserName;

    String Word;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    int positive1 = 0;
    int negative1 = 0;
    int neutral1= 0;
    public static final String positive2="positive";
    public static final String negative2="negative";
    public static final String neutral2="neutral";
    public static final String threshold="threshold";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Senti = intent.getStringExtra(sentiment);
        UserName = intent.getStringExtra(name);
        Word = intent.getStringExtra(textKey);
        positive1=intent.getIntExtra(positive,0);
        negative1 = intent.getIntExtra(negative,0);
        neutral1= intent.getIntExtra(neutral,0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        String sentiment;
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_public_cycle:
                Intent intent1 = new Intent(this, PublicCycle.class);
                startActivity(intent1);
                return true;
            case R.id.action_sign_out:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
        }
        return false;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Tab1_statistics tab1=new Tab1_statistics();
                    Bundle bundle = new Bundle();
                    bundle.putInt(positive2, positive1);
                    bundle.putInt(negative2, negative1);
                    bundle.putInt(neutral2, neutral1);
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    Tab2_diary tab2=new Tab2_diary();
                    Bundle arguments = new Bundle();
                    arguments.putString(sentim, Senti);
                    arguments.putString(username, UserName);
                    arguments.putString(word,Word);
                    tab2.setArguments(arguments);
                    //getFragmentManager().beginTransaction().add(R.id.container, tab2).commit();
                    return tab2;
                case 2:
                    Tab3_music tab3=new Tab3_music();
                    Bundle bundles=new Bundle();
                    int sum=positive1-negative1+neutral1;
                    bundles.putInt(threshold,sum);
                    tab3.setArguments(bundles);
                    return tab3;
                default:
                    Tab1_statistics tab0=new Tab1_statistics();
                    return tab0;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "STATISTICS";
                case 1:
                    return "DIARY";
                case 2:
                    return "MUSIC";
            }
            return null;
        }
    }
}