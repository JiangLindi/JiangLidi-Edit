package com.example.edit;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class button4Activity extends AppCompatActivity {


    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Button play;
    private Button pause;
    private Button stop;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button4);
        play = findViewById(R.id.play);//播放按钮
        pause = findViewById(R.id.pause);//暂停按钮
        stop = findViewById(R.id.stop);//停止按钮

        initMediaPlayer(); // 初始化MediaPlayer
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });//监听播放按钮

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });//监听暂停按钮

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopvoice();
            }
        });//监听停止按钮
    }
        private void initMediaPlayer() {

            try {

                mediaPlayer= MediaPlayer.create(button4Activity.this,R.raw.history);


                mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态

            } catch (Exception e) {

                e.printStackTrace();

            }

        }


        public void stopvoice() {



            if (mediaPlayer != null) {

                mediaPlayer.stop();

                mediaPlayer.release();

            }

        }

}


