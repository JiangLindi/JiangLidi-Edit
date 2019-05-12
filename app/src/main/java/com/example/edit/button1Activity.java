package com.example.edit;

import android.net.Uri;
import android.provider.MediaStore;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class button1Activity extends AppCompatActivity {
    protected static final int RESULT_LOAD_image = 1;
    private Button Button1_1;
    private Button Button1_2;
    private Button Button1_3;
    private Button Button1_choose;
    private ImageView iconIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button1);
        Button1_1 = findViewById(R.id.button1_1);//灰度按钮
        Button1_2 = findViewById(R.id.button1_2);
        Button1_3 = findViewById(R.id.button1_3);
        Button1_choose = findViewById(R.id.button1_choose);
        iconIv = findViewById(R.id.Imageview);

        Button1_1.setOnClickListener(new Button1Listener());//监听
        // Button1_2.setOnClickListener(new Button2Listener());
        //  Button1_3.setOnClickListener(new Button3Listener());
        Button1_choose.setOnClickListener(new Button4Listener());
    }
    class Button1Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Bitmap  bmSrc =BitmapFactory.decodeResource(getResources(), R.id.Imageview);

            iconIv.setImageBitmap(bitmap2Gray(bmSrc));
        }
    }
    class Button4Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent_gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent_gallery.setType("image/*");
            startActivityForResult(intent_gallery, RESULT_LOAD_image);

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_image && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            iconIv.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }



    /**

     * 图片灰度化处理

     *

     * @param bmSrc

     * */

    public Bitmap bitmap2Gray(Bitmap bmSrc) {

        // 得到图片的长和宽

        int width = bmSrc.getWidth();

        int height = bmSrc.getHeight();

        // 创建目标灰度图像

        Bitmap bmpGray = null;

        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        // 创建画布

        Canvas c = new Canvas(bmpGray);

        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();

        cm.setSaturation(0);

        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);

        paint.setColorFilter(f);

        c.drawBitmap(bmSrc, 0, 0, paint);

        return bmpGray;

    }

}





