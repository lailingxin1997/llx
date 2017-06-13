package com.example.lenovo.hutu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;
import static android.R.attr.positiveButtonText;

/**
 * Created by lenovo on 2017/5/5.
 */

public class diarylistActivity extends Activity implements AdapterView.OnItemClickListener{
    private Button adddiary,backbut;
    Cursor diaries;
    TextView textView=null;
    ListView list;
    private  Cursor cursor;
    SimpleCursorAdapter simpleCursorAdapter=null;
    private  DiaryDao diaryDao ;
    private final String Tag="断点";
    private String username;
    private String time = null;
    @Override
    protected void onCreate(Bundle SavedInstanceState)
    {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.loglist);
        list=(ListView)findViewById(R.id.diarylist);
        username=getIntent().getStringExtra("username");
        Log.i(Tag,"chenggong");
        refreshList();
        cursor=diaryDao.findCursor(username);
        list.setOnItemClickListener(this);
        //this.registerForContextMenu(diarylist);
        adddiary=(Button)findViewById(R.id.additem);
        adddiary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent diary = new Intent(diarylistActivity.this,diaryActivity.class);
                diary.putExtra("isEdit","no");
                diary.putExtra("username",username);
                startActivity(diary);
            }
        });
        backbut=(Button)findViewById(R.id.backinterface);
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(diarylistActivity.this,personalActivity.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });
    }

    public void refreshList(){
        diaryDao = new DiaryDao(this);
        diaries =diaryDao.getAllDairies(username);
        simpleCursorAdapter=new SimpleCursorAdapter(diarylistActivity.this,
                R.layout.listitem,diaries,new String[]{"title","created"},new int[]{R.id.content,R.id.created});
        list.setAdapter(simpleCursorAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshList();
    }
    @Override
    public  void onItemClick(final AdapterView<?> parent, View view,
                             int position, long args){
        final Intent i = new Intent(diarylistActivity.this,diaryActivity.class);
        Cursor c = (Cursor) parent.getItemAtPosition(position);
        //if (c.moveToNext()) {
          time= c.getString(c.getColumnIndex("created"));
            Log.v(Tag,time);
            Toast.makeText(getApplicationContext(),time,Toast.LENGTH_SHORT);
   //     }
        diaryDao = new DiaryDao(this);
        new AlertDialog.Builder(diarylistActivity.this).setTitle("提示").
                setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        day theday=diaryDao.getDayByCreated(time,username);
                        // day day=diaryDao.getDayById(id);
                        i.putExtra("isEdit","yes");
                        i.putExtra("created",time);
                        i.putExtra("username",username);
                        startActivity(i);
                    }
                }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                i.putExtra("id",id);
                i.putExtra("username",username);
                day deleteday=diaryDao.getDayByCreated(time,username);
                diaryDao.delete(deleteday,time);

                refreshList();
            }
        }).create().show();


    }
}
