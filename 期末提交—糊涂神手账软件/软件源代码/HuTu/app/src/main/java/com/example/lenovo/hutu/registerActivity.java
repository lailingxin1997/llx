package com.example.lenovo.hutu;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/5/4.
 */

public class registerActivity extends Activity {
    private EditText username,userkey;
    private final String Tag="判断";
    private boolean rightnamesize,rightkeysize,nameformat,keyformat,ifexist=true;
    private Button sureBotton,backButton;
    SQLiteOpenHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        helper=new Sqliteopenhelper(this);
        helper.getWritableDatabase();
        username=(EditText)findViewById(R.id.username);
        userkey=(EditText)findViewById(R.id.userkey);
        backButton=(Button)findViewById(R.id.backlog);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(registerActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        sureBotton=(Button)findViewById(R.id.regist);
        sureBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkkeyformat();
                checknameformat();
                checkkeysize();
                checknamesize();
                checkifexist();
                Log.v(Tag,"rightnamesize "+rightnamesize+" rightkeyszie "+rightkeysize+" nameformat "+nameformat+" keyformat "+keyformat+"ifexist"+ifexist);
                if (!ifexist)
                {
                    Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                }
                else if (rightnamesize&&rightkeysize&&nameformat&&keyformat&&ifexist)
                {
                    try{
                        SQLiteDatabase personalDB=helper.getWritableDatabase();
                        ContentValues values=new ContentValues();
                        values.put("name",username.getText().toString());
                        values.put("key",userkey.getText().toString());
                        personalDB.insert("userinfo",null,values);
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(registerActivity.this,MainActivity.class);
                        //Bundle bundle=new Bundle();
                        //bundle.putString("name",username.getText().toString());
                      //  intent.putExtras(bundle);//activity之间共享数据，这里把数据共享出去。
                        startActivity(intent);
                    }
                    catch (SQLiteException e)
                    {
                        Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                    }
                }
                else if((!rightkeysize)||(!rightnamesize))
                {
                    Toast.makeText(getApplicationContext(),"密码位数错误",Toast.LENGTH_SHORT).show();
                    if ((!keyformat)||(!nameformat))
                    {
                        Toast.makeText(getApplicationContext(),"格式错误",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void checknamesize()
    {
        String name;
        name=username.getText().toString();
        if ((name.length()>8)||(name.length()<4))
        {
            rightnamesize=false;
        }
        else {
            rightnamesize=true;
        }
        Log.v(Tag," namesize "+name.length());

    }
    private void checkkeysize()
    {
        String key;
        key=userkey.getText().toString();
        if ((key.length()>12)||(key.length()<8))
        {
            rightkeysize=false;
        }
        else {
            rightkeysize=true;
        }
        Log.v(Tag," keysize "+key.length());
    }
    private void checkkeyformat()
    {
        String key;
        key=userkey.getText().toString();
        for (int i=0;i<key.length();i++)
        {
            char item=key.charAt(i);
            if ((item>='a'&&item<='z')||(item>='A'&&item<='Z')||(item>='0'&&item<='9'))
            {
                keyformat=true;
            }
            else
            {
                keyformat=false;
            }
        }

    }
    private void checknameformat()
    {
        String name;
        name=username.getText().toString();
        for (int i=0;i<name.length();i++)
        {
            char item=name.charAt(i);
            if ((item>='a'&&item<='z')||(item>='A'&&item<='Z')||(item>='0'&&item<='9'))
            {
             nameformat=true;
            }
            else
            {
                nameformat=false;
            }
        }

    }
    private Boolean checkifexist()
    {
        String name;
        name=username.getText().toString();
        SQLiteDatabase personalDB=helper.getReadableDatabase();

        Cursor cursor=personalDB.rawQuery("select * from userinfo where name=?",new String[]{name});
        cursor.moveToFirst();
        int count = cursor.getCount();
        Log.v(Tag,count+"");
        if (count!=0)
        {
            ifexist=false;
        }
        else {
            ifexist=true;
        }
        return ifexist;

    }
}
