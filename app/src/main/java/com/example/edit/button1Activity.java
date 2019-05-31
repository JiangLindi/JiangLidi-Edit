package com.example.edit;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
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
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class button1Activity extends AppCompatActivity {

    private Button Button1_1;
    private Button Button1_4;
    private Button Button1_choose;
    private Button Button_save;
    private Button Button1_5;
    private Button Button1_6;
    private Button Button1_7;
    private Button Button1_8;
    private ImageView iconIv;
    private static Bitmap bit;
    private Bitmap tempphoto;
    protected static String picturePath;
    private MediaStore mScanner;

    private Bitmap dstBitmap = null;
    private SeekBar SaturationseekBar = null;
    private SeekBar BrightnessseekBar = null;
    private SeekBar ContrastseekBar = null;
    private static int imgHeight = 0, imgWidth = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button1);
        iconIv = findViewById(R.id.Imageview);//绑定图片
        iconIv.setOnTouchListener(new TouchListener());
        initView();
    }
    private final class TouchListener implements View.OnTouchListener {
            /** 记录是拖拉照片模式还是放大缩小照片模式 */
            private int mode = 0;// 初始状态
            /** 拖拉照片模式 */
            private static final int MODE_DRAG = 1;
            /** 放大缩小照片模式 */
            private static final int MODE_ZOOM = 2;
            /** 用于记录开始时候的坐标位置 */
            private PointF startPoint = new PointF();
            /** 用于记录拖拉图片移动的坐标位置 */
            private Matrix matrix = new Matrix();
            /** 用于记录图片要进行拖拉时候的坐标位置 */
            private Matrix currentMatrix = new Matrix();
            /** 两个手指的开始距离 */
            private float startDis;
            /** 两个手指的中间点 */
           private PointF midPoint;
            /** 计算两个手指间的距离 */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    // 手指压下屏幕
                    case MotionEvent.ACTION_DOWN:
                        mode = MODE_DRAG;
                        // 记录ImageView当前的移动位置
                        currentMatrix.set(iconIv.getImageMatrix());
                        startPoint.set(event.getX(), event.getY());
                        break;
                    // 手指在屏幕上移动，改事件会被不断触发
                    case MotionEvent.ACTION_MOVE:
                        // 拖拉图片
                        if (mode == MODE_DRAG) {
                            float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                            float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                            // 在没有移动之前的位置上进行移动
                            matrix.set(currentMatrix);
                            matrix.postTranslate(dx, dy);
                        }
                        // 放大缩小图片
                        else if (mode == MODE_ZOOM) {
                            float endDis = distance(event);// 结束距离
                            if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                                float scale = endDis / startDis;// 得到缩放倍数
                                matrix.set(currentMatrix);
                                matrix.postScale(scale, scale,midPoint.x,midPoint.y);
                            }
                        }
                        break;
                    // 手指离开屏幕
                    case MotionEvent.ACTION_UP:
                        // 当触点离开屏幕，但是屏幕上还有触点(手指)
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = 0;
                        break;
                    // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = MODE_ZOOM;
                        /** 计算两个手指间的距离 */
                        startDis = distance(event);
                        /** 计算两个手指间的中间点 */
                        if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            midPoint = mid(event);
                            //记录当前ImageView的缩放倍数
                            currentMatrix.set(iconIv.getImageMatrix());
                        }
                        break;
                }
                iconIv.setImageMatrix(matrix);
                return true;
            }
            private float distance(MotionEvent event) {
                float dx = event.getX(1) - event.getX(0);
                float dy = event.getY(1) - event.getY(0);
                /** 使用勾股定理返回两点之间的距离 */
                float t=dx*dx+dy*dy;
                return t*t;
            }
            /** 计算两个手指间的中间点 */
            private PointF mid(MotionEvent event) {
                float midX = (event.getX(1) + event.getX(0)) / 2;
                float midY = (event.getY(1) + event.getY(0)) / 2;
                return new PointF(midX, midY);
            }
    }

    private void initView() {
        Button1_1 = findViewById(R.id.button1_1);//灰度按钮
        Button1_choose = findViewById(R.id.button1_choose);//选择图片按钮
        Button_save = findViewById(R.id.button_save);//保存图片按钮
        Button1_4=findViewById(R.id.button1_4);//线性灰度化
        Button1_5=findViewById(R.id.button1_5);//高斯模糊
        Button1_6=findViewById(R.id.button1_6);//素描
        Button1_7=findViewById(R.id.button1_7);//取消
        Button1_8=findViewById(R.id.button1_8);//怀旧
        SaturationseekBar = findViewById(R.id.Saturationseekbar);//饱和度滑动条
        BrightnessseekBar = findViewById(R.id.Brightnessseekbar);//亮度滑动条
        ContrastseekBar = findViewById(R.id.Contrastseekbar);//对比度滑动条
        /*
        打开图库，选择图片
         */
        Button1_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                open_photo.setType("image/*");
                startActivityForResult(open_photo, 10);

            }
        });


/*
一键灰度化
 */
        Button1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempphoto=bitmap2Gray(bit);
                iconIv.setImageBitmap(tempphoto);
            }
        });//监听

       /*
       保存图片
        */
        Button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bit=tempphoto;
                saveImageToGallery(button1Activity.this, bit);
            }
        });
//线性灰度化
        Button1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               tempphoto=lineGrey(bit);
                iconIv.setImageBitmap(tempphoto);
            }
        });
//高斯模糊
        Button1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempphoto=convertToBlur(bit);
                iconIv.setImageBitmap(tempphoto);

            }
        });
 //素描
       Button1_6.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tempphoto=convertToSketch(bit);
               iconIv.setImageBitmap(tempphoto);
           }
       });
//取消
       Button1_7.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tempphoto=bit;
               iconIv.setImageBitmap(tempphoto);
           }
       });
 //怀旧
       Button1_8.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               tempphoto=OldRemeberImage(bit);
               iconIv.setImageBitmap(tempphoto);
           }
       });
/*
饱和度滑动条的监听
 */

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
                tempphoto=dstBitmap;
                iconIv.setImageBitmap(tempphoto);

            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });
/*
亮度滑动条监听
 */
        BrightnessseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 当拖动条的滑块位置发生改变时触发该方法
            @Override
            public void onProgressChanged(SeekBar arg0, int progress,
                                          boolean fromUser) {
                Bitmap dstBitmap = Bitmap.createBitmap(imgWidth, imgHeight,
                        Bitmap.Config.ARGB_8888);
                int brightness = progress - 127;
                ColorMatrix cMatrix = new ColorMatrix();
                cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1,
                        0, 0, brightness,// 改变亮度
                        0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});

                Paint paint = new Paint();
                paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

                Canvas canvas = new Canvas(dstBitmap);
                // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
                canvas.drawBitmap(bit, 0, 0, paint);
                tempphoto=dstBitmap;
                iconIv.setImageBitmap(tempphoto);

            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });
        /*
        对比度滑动条
         */
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
                tempphoto=dstBitmap;
                iconIv.setImageBitmap(tempphoto);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10 && resultCode == RESULT_OK&& null != data) {
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
    /**
     * 图片线性灰度处理
     *
     * @param image
     * */
    public static Bitmap lineGrey(Bitmap image) {
        // 得到图像的宽度和长度
        int width = image.getWidth();
        int height = image.getHeight();
        // 创建线性拉升灰度图像
        Bitmap linegray = null;
        linegray = image.copy(Bitmap.Config.ARGB_8888, true);
        // 依次循环对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 得到每点的像素值
                int col = image.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 增加了图像的亮度
                red = (int) (1.1 * red + 30);
                green = (int) (1.1 * green + 30);
                blue = (int) (1.1 * blue + 30);
                // 对图像像素越界进行处理
                if (red >= 255) {
                    red = 255;
                }

                if (green >= 255) {
                    green = 255;
                }

                if (blue >= 255) {
                    blue = 255;
                }
                // 新的ARGB
                int newColor = alpha | (red << 16) | (green << 8) | blue;
                // 设置新图像的RGB值
                linegray.setPixel(i, j, newColor);
            }
        }
        return linegray;
    }

    /**
     * 高斯模糊
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToBlur(Bitmap bmp) {
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }
    /**
     * 怀旧
     *
     * @param bmp
     * @return
     */
    private Bitmap OldRemeberImage(Bitmap bmp)
    {
        /*
         * 怀旧处理算法即设置新的RGB
         * R=0.393r+0.769g+0.189b
         * G=0.349r+0.686g+0.168b
         * B=0.272r+0.534g+0.131b
         */
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++)
        {
            for (int k = 0; k < width; k++)
            {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    /**
     * 素描效果
     *
     * @param bmp
     * @return
     */
    public static Bitmap convertToSketch(Bitmap bmp) {
        int pos, row, col, clr;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixSrc = new int[width * height];
        int[] pixNvt = new int[width * height];
        // 先对图象的像素处理成灰度颜色后再取反
        Bitmap newBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        bmp.getPixels(pixSrc, 0, width, 0, 0, width, height);
        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                pos = row * width + col;
                pixSrc[pos] = (Color.red(pixSrc[pos])
                        + Color.green(pixSrc[pos]) + Color.blue(pixSrc[pos])) / 3;
                pixNvt[pos] = 255 - pixSrc[pos];
            }
        }
        // 对取反的像素进行高斯模糊, 强度可以设置，暂定为5.0
        gaussGray(pixNvt, 5.0, 5.0, width, height);
        // 灰度颜色和模糊后像素进行差值运算
        for (row = 0; row < height; row++) {
            for (col = 0; col < width; col++) {
                pos = row * width + col;
                clr = pixSrc[pos] << 8;
                clr /= 256 - pixNvt[pos];
                clr = Math.min(clr, 255);
                pixSrc[pos] = Color.rgb(clr, clr, clr);
            }
        }
        newBmp.setPixels(pixSrc, 0, width, 0, 0, width, height);
        return newBmp;
    }
    public static int gaussGray(int[] psrc, double horz, double vert,
                                 int width, int height) {
        int[] dst, src;
        double[] n_p, n_m, d_p, d_m, bd_p, bd_m;
        double[] val_p, val_m;
        int i, j, t, k, row, col, terms;
        int[] initial_p, initial_m;
        double std_dev;
        int row_stride = width;
        int max_len = Math.max(width, height);
        int sp_p_idx, sp_m_idx, vp_idx, vm_idx;
        val_p = new double[max_len];
        val_m = new double[max_len];
        n_p = new double[5];
        n_m = new double[5];
        d_p = new double[5];
        d_m = new double[5];
        bd_p = new double[5];
        bd_m = new double[5];
        src = new int[max_len];
        dst = new int[max_len];
        initial_p = new int[4];
        initial_m = new int[4];
        // 垂直方向
        if (vert > 0.0) {
            vert = Math.abs(vert) + 1.0;
            std_dev = Math.sqrt(-(vert * vert) / (2 * Math.log(1.0 / 255.0)));
            // 初试化常量
            findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            for (col = 0; col < width; col++) {
                for (k = 0; k < max_len; k++) {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < height; t++) {
                    src[t] = psrc[t * row_stride + col];
                }
                sp_p_idx = 0;
                sp_m_idx = height - 1;
                vp_idx = 0;
                vm_idx = height - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[height - 1];
                for (row = 0; row < height; row++) {
                    terms = (row < 4) ? row : 4;
                    for (i = 0; i <= terms; i++) {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++) {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, height);
                for (t = 0; t < height; t++) {
                    psrc[t * row_stride + col] = dst[t];
                }
            }
        }
        // 水平方向
        if (horz > 0.0) {
            horz = Math.abs(horz) + 1.0;
            if (horz != vert) {
                std_dev = Math.sqrt(-(horz * horz)
                        / (2 * Math.log(1.0 / 255.0)));
                // 初试化常量
                findConstants(n_p, n_m, d_p, d_m, bd_p, bd_m, std_dev);
            }
            for (row = 0; row < height; row++) {
                for (k = 0; k < max_len; k++) {
                    val_m[k] = val_p[k] = 0;
                }
                for (t = 0; t < width; t++) {
                    src[t] = psrc[row * row_stride + t];
                }
                sp_p_idx = 0;
                sp_m_idx = width - 1;
                vp_idx = 0;
                vm_idx = width - 1;
                initial_p[0] = src[0];
                initial_m[0] = src[width - 1];
                for (col = 0; col < width; col++) {
                    terms = (col < 4) ? col : 4;
                    for (i = 0; i <= terms; i++) {
                        val_p[vp_idx] += n_p[i] * src[sp_p_idx - i] - d_p[i]
                                * val_p[vp_idx - i];
                        val_m[vm_idx] += n_m[i] * src[sp_m_idx + i] - d_m[i]
                                * val_m[vm_idx + i];
                    }
                    for (j = i; j <= 4; j++) {
                        val_p[vp_idx] += (n_p[j] - bd_p[j]) * initial_p[0];
                        val_m[vm_idx] += (n_m[j] - bd_m[j]) * initial_m[0];
                    }
                    sp_p_idx++;
                    sp_m_idx--;
                    vp_idx++;
                    vm_idx--;
                }
                transferGaussPixels(val_p, val_m, dst, 1, width);
                for (t = 0; t < width; t++) {
                    psrc[row * row_stride + t] = dst[t];
                }
            }
        }
        return 0;
    }
    public static void transferGaussPixels(double[] src1, double[] src2,
                                            int[] dest, int bytes, int width) {
        int i, j, k, b;
        int bend = bytes * width;
        double sum;
        i = j = k = 0;
        for (b = 0; b < bend; b++) {
            sum = src1[i++] + src2[j++];
            if (sum > 255)
                sum = 255;
            else if (sum < 0)
                sum = 0;
            dest[k++] = (int) sum;
        }
    }
    public static void findConstants(double[] n_p, double[] n_m, double[] d_p,
                                      double[] d_m, double[] bd_p, double[] bd_m, double std_dev) {
        double div = Math.sqrt(2 * 3.141593) * std_dev;
        double x0 = -1.783 / std_dev;
        double x1 = -1.723 / std_dev;
        double x2 = 0.6318 / std_dev;
        double x3 = 1.997 / std_dev;
        double x4 = 1.6803 / div;
        double x5 = 3.735 / div;
        double x6 = -0.6803 / div;
        double x7 = -0.2598 / div;
        int i;
        n_p[0] = x4 + x6;
        n_p[1] = (Math.exp(x1)
                * (x7 * Math.sin(x3) - (x6 + 2 * x4) * Math.cos(x3)) + Math
                .exp(x0) * (x5 * Math.sin(x2) - (2 * x6 + x4) * Math.cos(x2)));
        n_p[2] = (2
                * Math.exp(x0 + x1)
                * ((x4 + x6) * Math.cos(x3) * Math.cos(x2) - x5 * Math.cos(x3)
                * Math.sin(x2) - x7 * Math.cos(x2) * Math.sin(x3)) + x6
                * Math.exp(2 * x0) + x4 * Math.exp(2 * x1));
        n_p[3] = (Math.exp(x1 + 2 * x0)
                * (x7 * Math.sin(x3) - x6 * Math.cos(x3)) + Math.exp(x0 + 2
                * x1)
                * (x5 * Math.sin(x2) - x4 * Math.cos(x2)));
        n_p[4] = 0.0;
        d_p[0] = 0.0;
        d_p[1] = -2 * Math.exp(x1) * Math.cos(x3) - 2 * Math.exp(x0)
                * Math.cos(x2);
        d_p[2] = 4 * Math.cos(x3) * Math.cos(x2) * Math.exp(x0 + x1)
                + Math.exp(2 * x1) + Math.exp(2 * x0);
        d_p[3] = -2 * Math.cos(x2) * Math.exp(x0 + 2 * x1) - 2 * Math.cos(x3)
                * Math.exp(x1 + 2 * x0);
        d_p[4] = Math.exp(2 * x0 + 2 * x1);
        for (i = 0; i <= 4; i++) {
            d_m[i] = d_p[i];
        }
        n_m[0] = 0.0;
        for (i = 1; i <= 4; i++) {
            n_m[i] = n_p[i] - d_p[i] * n_p[0];
        }
        double sum_n_p, sum_n_m, sum_d;
        double a, b;
        sum_n_p = 0.0;
        sum_n_m = 0.0;
        sum_d = 0.0;
        for (i = 0; i <= 4; i++) {
            sum_n_p += n_p[i];
            sum_n_m += n_m[i];
            sum_d += d_p[i];
        }
        a = sum_n_p / (1.0 + sum_d);
        b = sum_n_m / (1.0 + sum_d);
        for (i = 0; i <= 4; i++) {
            bd_p[i] = d_p[i] * a;
            bd_m[i] = d_m[i] * b;
        }
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

        // 最后通知图库更新

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4

            String[] paths = new String[]{file.getAbsolutePath()};

            MediaScannerConnection.scanFile(context, paths, null, null);

        } else {

            final Intent intent;

            if (file.isDirectory()) {

                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);

                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");

                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));

            } else {

                intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                intent.setData(Uri.fromFile(file));

            }

            context.sendBroadcast(intent);

        }

    }

}




