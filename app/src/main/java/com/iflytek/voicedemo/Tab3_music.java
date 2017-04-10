package com.iflytek.voicedemo;

/**
 * Created by Kathy.Tian on 17/4/5.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.iflytek.voicedemo.Diary.threshold;

public class Tab3_music extends Fragment {
    private Button startButton;
    private Button stopButton;
    private Button pauseButton;
    private MediaPlayer soundPlayer;
    private SeekBar soundSeekBar;
    private Thread soundThread;
    runnableThread threadMonitor;
    private TextView tv1;
    private ImageView im1;
    private int num;
    private int setThred=5;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_music, container, false);
        Bundle bundle =new Bundle();
        bundle=getArguments();
        num=bundle.getInt(threshold,0);
        startButton=(Button) rootView.findViewById(R.id.playButton);
        pauseButton=(Button) rootView.findViewById(R.id.pauseButton);
        stopButton=(Button) rootView.findViewById(R.id.stopButton);
        soundSeekBar=(SeekBar) rootView.findViewById(R.id.soundSeekBar);
        tv1=(TextView) rootView.findViewById(R.id.songName);
        im1=(ImageView) rootView.findViewById(R.id.songView);
        if(num>setThred) {
            soundPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.happy);
            tv1.setText("SongName:Swan Lake");
            im1.setImageResource(R.drawable.swanlake);
        }else if(num<-setThred){
            soundPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.happy);
            tv1.setText("SongName:Sugar");
            im1.setImageResource(R.drawable.sugar);
        }else{
            soundPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.happy);
            tv1.setText("SongName:Something Just Like This");
            im1.setImageResource(R.drawable.something);
        }

        setupListeners();
        threadMonitor=new runnableThread();
        soundThread=new Thread(threadMonitor);
        soundThread.start();

        return rootView;
    }
    private void setupListeners(){
        startButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                soundPlayer.start();
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                soundPlayer.pause();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                soundPlayer.stop();
                if(num>setThred) {
                    soundPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.happy);
                }else if(num<-setThred){
                    soundPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.happy);
                }else{
                    soundPlayer = MediaPlayer.create(getActivity().getBaseContext(), R.raw.happy);
                }
            }
        });
        soundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){

            }

            @Override
            public void onProgressChanged(SeekBar seekBar,int progress, boolean fromUser){
                if(fromUser){
                    soundPlayer.seekTo(progress);
                }

            }
        });
        soundPlayer.setOnCompletionListener(new OnCompletionListener() {

            public void onCompletion(MediaPlayer soundPlayer) {
                soundPlayer.release();

            };
        });

    }

    private class runnableThread implements Runnable{
        public void run(){
            int currentPosition=0;
            int soundTotal=soundPlayer.getDuration();
            soundSeekBar.setMax(soundTotal);

            while(soundPlayer!=null && currentPosition<soundTotal){
                try{
                    Thread.sleep(300);
                    currentPosition=soundPlayer.getCurrentPosition();

                }catch (InterruptedException soundException){
                    return;
                }
                catch (Exception otherException){
                    return;
                }
                soundSeekBar.setProgress(currentPosition);
            }
        }

    }
}