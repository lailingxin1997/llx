package com.example.lenovo.hutu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CaptureRequest;

import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.sql.Time;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.DataFormatException;


/**
 * Created by lenovo on 2017/4/18.
 */

public class diaryActivity extends Activity {
    DiaryDao diaryDao = new DiaryDao(diaryActivity.this);
    private EditText content,title,weather,created;
    private String contentstr,titlestr,weatherstr,createdstr,usernamestr,newcreatedstr,fileName;
    private Button save,back,camera,deletephoto;
    private String edit;
    private int oldid;
    private final String Tag="检查";
    private String oldtime;
    private ImageView imageview;
    private float scaleWidth,scaleHeight;
    private boolean num=false;
    private  Bitmap bp=null;
    private int w,h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);
        Intent i=getIntent();
        usernamestr=getIntent().getStringExtra("username");
        SimpleDateFormat theformat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date current=new Date(System.currentTimeMillis());
        String str=theformat.format(current);
        content=(EditText)findViewById(R.id.fullcontent);
        title=(EditText)findViewById(R.id.fulltitle);
        weather=(EditText)findViewById(R.id.fullweather);
        created=(EditText)findViewById(R.id.created);
        created.setEnabled(false);
        edit=i.getStringExtra("isEdit");
        fileName=null;
        Log.v(Tag,edit);

        if (edit.equals("yes")==true){
            oldid=i.getIntExtra("id",-1);
            createdstr=i.getStringExtra("created");

            Log.v(Tag,oldid+"");
            Log.v(Tag,createdstr+"");

            day day=diaryDao.getDayByCreated(createdstr,usernamestr);
            content.setText(day.getContent());
            title.setText(day.getTitle());
            weather.setText(day.getWeather());
            //created.setEnabled(true);
            created.setText(createdstr);
            fileName=day.getPhotopath();
            if(fileName!=null) {
                //  created.setEnabled(false);
                setmimage(fileName);
            }
        }
        else if (edit.equals("no")==true)
        {
            created.setText(str);
        }
        back=(Button)findViewById(R.id.backdiary);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(diaryActivity.this,diarylistActivity.class);
                i.putExtra("username",usernamestr);
                startActivity(i);
            }
        });
        camera=(Button)findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(diaryActivity.this).setTitle("选择").setPositiveButton("打开相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(i.resolveActivity(getPackageManager())!=null)
                        {
                            startActivityForResult(i,1);
                        }
                    }
                }).setNegativeButton("打开相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                 startActivityForResult(i,2);


                    }
                }).create().show();

            }
        });
        deletephoto=(Button)findViewById(R.id.deletephoto);
        deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileName!=null) {
                    fileName = null;
                    imageview.setImageBitmap(null);
                    bp = null;
                }
            }
        });
        save=(Button)findViewById(R.id.savebut);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     getText();
                    if(titlestr.equals("")||titlestr.length()>20){
                        Toast.makeText(getApplicationContext(),"题目不能为空或者题目超过20个字",Toast.LENGTH_SHORT).show();
                    }
                else {
                        day day = new day(newcreatedstr, titlestr, weatherstr, usernamestr, contentstr,fileName);

                        Log.v(Tag, edit);
                        if (edit.equals("yes") == true) {
                            Log.v(Tag, "进入更新");
                            oldtime = createdstr;
                            diaryDao.update(day, oldtime, usernamestr, oldid);


                        } else {
                            diaryDao.save(day, usernamestr);
                        }
                        Intent i = new Intent(diaryActivity.this, diarylistActivity.class);
                        i.putExtra("username", usernamestr);
                        startActivity(i);
                    }

                 }
        });

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        SimpleDateFormat theformat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date time = new Date(System.currentTimeMillis());
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String sdstatus = Environment.getExternalStorageState();
                    if (!sdstatus.equals(Environment.MEDIA_MOUNTED)) {
                        Log.i("TestFile", "SD card is ot" +
                                "avaiable");
                        return;

                    }
                    String name = theformat.format(time) + ".jpg";
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    FileOutputStream fos = null;

                    File file = new File("/sdcard/myImage/");
                    file.mkdirs();// 创建文件夹

                   fileName = "/sdcard/myImage/" + name;// 保存路径

                    try {// 写入SD card
                        fos = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    setmimage(fileName);
                }
                break;
            case 2: {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                fileName=picturePath;
                cursor.close();
// 将图片显示到界面上
                setmimage(picturePath);
                break;
            }
            default:
                break;
        }
    }


    private void getText(){

        Log.v(Tag,usernamestr);
        contentstr=content.getText().toString();
        Log.v(Tag,contentstr);
        titlestr=title.getText().toString();
        Log.v(Tag,titlestr);
        weatherstr=weather.getText().toString();
        Log.v(Tag,weatherstr);
        newcreatedstr=created.getText().toString();
        Log.v(Tag,newcreatedstr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
          if (bp!=null) {
              switch (event.getAction()) {

                  case MotionEvent.ACTION_DOWN:  //当屏幕检测到第一个触点按下之后就会触发到这个事件。
                      if (num == true) {
                          Matrix matrix = new Matrix();
                          matrix.postScale(scaleWidth, scaleHeight);

                          Bitmap newBitmap = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
                          imageview.setImageBitmap(newBitmap);
                          num = false;
                      } else {
                          Matrix matrix = new Matrix();
                          matrix.postScale(1.0f, 1.0f);
                          Bitmap newBitmap = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
                          imageview.setImageBitmap(newBitmap);
                          num = true;
                      }
                      break;
              }
          }


        return super.onTouchEvent(event);
    }
    public void setmimage(String name)
    {
        DisplayMetrics dm=new DisplayMetrics();//创建矩阵
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        imageview=(ImageView)findViewById(R.id.imageView3);
        bp=BitmapFactory.decodeFile(name);
        int width=bp.getWidth();
        int height=bp.getHeight();
         w=dm.widthPixels; //得到屏幕的宽度
         h=dm.heightPixels; //得到屏幕的高度
        scaleWidth=((float)w)/width;
        scaleHeight=((float)h)/height;
        imageview.setImageBitmap(bp);
    }
}
