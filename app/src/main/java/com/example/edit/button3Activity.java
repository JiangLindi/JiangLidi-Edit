package com.example.edit;
//import android.media.session.MediaController;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

import static android.os.Build.VERSION_CODES.M;

public class  button3Activity extends MainActivity {
    private  static String VIDEOPATH;
   private VideoView mVideoView1;
    MediaController myController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button3);
        mVideoView1=findViewById(R.id.mVedioView1);
        myController=new MediaController(this);
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 66);//调用系统相册
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            //获取视频本地路径
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
           VIDEOPATH = cursor.getString(columnIndex);
            cursor.close();
            File video=new File(VIDEOPATH);
            if (video.exists()) {
                mVideoView1.setVideoPath(VIDEOPATH);
                mVideoView1.setMediaController(myController);
                mVideoView1.requestFocus();
            }
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }






}

