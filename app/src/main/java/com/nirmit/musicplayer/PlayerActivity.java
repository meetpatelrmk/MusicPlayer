package com.nirmit.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button pause,back,next;
    TextView songname;
    SeekBar seekBar;
    String sname;

    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pause = (Button) findViewById(R.id.btn_pause);
        back = (Button) findViewById(R.id.btn_back);
        next = (Button) findViewById(R.id.btn_next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        songname = (TextView) findViewById(R.id.songname);

        updateseekBar = new Thread(){

            @Override
            public void run() {
                super.run();

                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){
                    try {

                        sleep(500);
                        currentPosition=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if (mediaPlayer!=null){

            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        sname = mySongs.get(position).toString();
        String songName = i.getStringExtra("songname");
        songname.setText(songName);

        songname.setSelected(true);

        position = bundle.getInt("pos",0);
        Uri u = Uri.parse(mySongs.get(position).toString());

        mediaPlayer= MediaPlayer.create(getApplicationContext(),u);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        updateseekBar.start();

        //seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.design_default_color_primary), PorterDuff.Mode.MULTIPLY);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setMax(mediaPlayer.getDuration());

                if(mediaPlayer.isPlaying()){
                    pause.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else {
                    pause.setBackgroundResource(R.drawable.icon_pause);
                    mediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1)%mySongs.size();

                Uri u = Uri.parse(mySongs.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songname.setText(sname);

                mediaPlayer.start();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName().toString();
                songname.setText(sname);

                mediaPlayer.start();


            }
        });
    }
}