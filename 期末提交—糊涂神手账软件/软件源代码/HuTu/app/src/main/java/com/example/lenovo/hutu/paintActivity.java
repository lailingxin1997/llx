package com.example.lenovo.hutu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static java.lang.System.out;

/**
 * Created by lenovo on 2017/4/22.
 */

public class paintActivity extends Activity {

    private ImageView iv;
    private Button pickColor, paintSize, eraser, paintbut, backbut, cleanbut, savebut;
    private Bitmap baseBitmap;
    private Canvas canvas;
    private Paint paint;
    private colorDialog colordialog;
    private DisplayMetrics dm;
    Context context;
    private String name;
    private int hight, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint);
        name = getIntent().getStringExtra("username");
        iv = (ImageView) findViewById(R.id.imageView6);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        hight = dm.heightPixels;
        width = dm.widthPixels;
        baseBitmap = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.WHITE);
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        // 创建一个可以被修改的bitmap


        // 知道用户手指在屏幕上移动的轨迹
        iv.setOnTouchListener(new View.OnTouchListener() {
            // 设置手指开始的坐标
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // 手指第一次接触屏幕
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指在屏幕上滑动
                        int newX = (int) event.getX();
                        int newY = (int) event.getY();

                        canvas.drawLine(startX, startY, newX, newY, paint);
                        // 重新更新画笔的开始位置
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        iv.setImageBitmap(baseBitmap);
                        break;
                    case MotionEvent.ACTION_UP: // 手指离开屏幕
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        pickColor = (Button) findViewById(R.id.pickColor);
        pickColor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                colordialog = new colorDialog(context, Color.BLUE, new colorDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(int color) {
                        paint.setColor(color);

                    }

                });

                colordialog.show();
            }
        });
        paintSize = (Button) findViewById(R.id.paintStroke);
        paintSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] choices = {"3", "5", "8", "12", "15", "18", "20"};
                new AlertDialog.Builder(paintActivity.this).setTitle("画笔大小").setSingleChoiceItems(choices, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(getApplicationContext(), choices[i], Toast.LENGTH_SHORT).show();

                        int Stroke = Integer.parseInt(choices[i]);
                        paint.setStrokeWidth((float) Stroke);
                        dialog.cancel();
                    }
                }).show();

            }
        });
        eraser = (Button) findViewById(R.id.eraser);
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] choices = {"3", "5", "8", "12", "15", "18", "20"};
                new AlertDialog.Builder(paintActivity.this).setTitle("橡皮擦大小").setSingleChoiceItems(choices, 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        paint.setColor(Color.WHITE);
                        int Stroke = Integer.parseInt(choices[i]);
                        paint.setStrokeWidth((float) Stroke);
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });
        paintbut = (Button) findViewById(R.id.paint);
        paintbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(5);
            }
        });
        backbut = (Button) findViewById(R.id.paintback);
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(paintActivity.this, personalActivity.class);
                i.putExtra("username", name);
                startActivity(i);
            }
        });
        cleanbut = (Button) findViewById(R.id.clean);
        cleanbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        savebut = (Button) findViewById(R.id.savePaint);
        savebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepaint(v);
            }
        });

    }

    public void refresh() {
        onCreate(null);


    }

    public void savepaint(View view) //throws IOException
    {
        String sdstatus= Environment.getExternalStorageState();
        if(!sdstatus.equals(Environment.MEDIA_MOUNTED))
        {
            Log.i("TestFile","SD card is ot" +
                    "avaiable");
            return;

        }
        String name = System.currentTimeMillis()
                + ".jpg";
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
        FileOutputStream fos = null;
        File file = new File("/sdcard/myImage/");
        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/myImage/" + name;// 保存路径

        try {// 写入SD card
            fos = new FileOutputStream(fileName);
           baseBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
}}
}