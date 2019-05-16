package com.example.edit;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class button1Activity extends AppCompatActivity {
    protected static final int RESULT_LOAD_image = 1;
    private Button Button1_1;
    private Button Button1_2;
    private Button Button1_3;
    private Button Button1_choose;
    private Button Button_save;
    private ImageView iconIv;
    private static Bitmap bit;
    protected static String picturePath;
    private MediaStore mScanner;

    private Bitmap dstBitmap = null;
    private SeekBar SaturationseekBar = null;
    private SeekBar BrightnessseekBar = null;
    private SeekBar ContrastseekBar = null;
    private static int imgHeight = 0, imgWidth = 0;
    public static final int PICTURE = 0;
    public static final int MAX_WIDTH = 240;
    public static final int MAX_HEIGHT = 240;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button1);
        initView();
    }


    private void initView() {
        Button1_1 = findViewById(R.id.button1_1);//灰度按钮
        Button1_2 = findViewById(R.id.button1_2);
        Button1_3 = findViewById(R.id.button1_3);
        Button1_choose = findViewById(R.id.button1_choose);
        Button_save = findViewById(R.id.button_save);
        iconIv = findViewById(R.id.Imageview);

        SaturationseekBar = findViewById(R.id.Saturationseekbar);
        BrightnessseekBar = findViewById(R.id.Brightnessseekbar);
        ContrastseekBar = findViewById(R.id.Contrastseekbar);

        Button1_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                open_photo.setType("image/*");
                startActivityForResult(open_photo, 10);

            }
        });

        Button1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconIv.setImageBitmap(bitmap2Gray(bit));
            }
        });//监听

        // Button1_2.setOnClickListener(new Button2Listener());
        //  Button1_3.setOnClickListener(new Button3Listener());
        Button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImageToGallery(button1Activity.this, bit);
            }
        });


        SaturationseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 当拖动条的滑块位置发生改变时触发该方法
            @Override
            public void onProgressChanged(SeekBar arg0, int progress,
                                          boolean fromUser) {
                // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
                dstBitmap = Bitmap.createBitmap(imgWidth, imgHeight,
                        Bitmap.Config.ARGB_8888);
                ColorMatrix cMatrix = new ColorMatrix();
                // 设置饱和度
                cMatrix.setSaturation((float) (progress / 100.0));

                Paint paint = new Paint();
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

                Canvas canvas = new Canvas(dstBitmap);
                // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
                canvas.drawBitmap(bit, 0, 0, paint);

                iconIv.setImageBitmap(dstBitmap);

            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });

        BrightnessseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 当拖动条的滑块位置发生改变时触发该方法
            @Override
            public void onProgressChanged(SeekBar arg0, int progress,
                                          boolean fromUser) {
                Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
                        Bitmap.Config.ARGB_8888);
                int brightness = progress - 127;
                ColorMatrix cMatrix = new ColorMatrix();
                cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1,
                        0, 0, brightness,// 改变亮度
                        0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});

                Paint paint = new Paint();
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

                Canvas canvas = new Canvas(bmp);
                // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
                canvas.drawBitmap(bit, 0, 0, paint);
                iconIv.setImageBitmap(bmp);

            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });
        ContrastseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 当拖动条的滑块位置发生改变时触发该方法
            @Override
            public void onProgressChanged(SeekBar arg0, int progress,
                                          boolean fromUser) {
                dstBitmap = Bitmap.createBitmap(imgWidth, imgHeight,
                        Bitmap.Config.ARGB_8888);
                // int brightness = progress - 127;
                float contrast = (float) ((progress + 64) / 128.0);
                ColorMatrix cMatrix = new ColorMatrix();
                cMatrix.set(new float[]{contrast, 0, 0, 0, 0, 0,
                        contrast, 0, 0, 0,// 改变对比度
                        0, 0, contrast, 0, 0, 0, 0, 0, 1, 0});

                Paint paint = new Paint();
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

                Canvas canvas = new Canvas(dstBitmap);
                // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
                canvas.drawBitmap(bit, 0, 0, paint);

                iconIv.setImageBitmap(dstBitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_image && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
           picturePath = cursor.getString(columnIndex);
            cursor.close();
           bit = BitmapFactory.decodeFile(picturePath);
            iconIv.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }

    }
    */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            bit = BitmapFactory.decodeFile(picturePath);
            iconIv.setImageBitmap(bit);
            imgHeight = bit.getHeight();
            imgWidth = bit.getWidth();

        }

    }


    /**
     * 图片灰度化处理
     *
     * @param bmSrc
     */

    public static Bitmap bitmap2Gray(Bitmap bmSrc) {

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


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Edit相册");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String saveImage(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}




