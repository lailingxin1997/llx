package com.example.lenovo.hutu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private Button logBotton;
    private Button regBotton;
    private String _key;
    private String _name;
    private EditText name,key;
    SQLiteOpenHelper helper;
    private final String Tag="检查";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        helper=new Sqliteopenhelper(this);
        helper.getWritableDatabase();
        name=(EditText)findViewById(R.id.accountEt);
        key=(EditText)findViewById(R.id.pwdEt);
        regBotton=(Button)findViewById(R.id.regbotton);
        regBotton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(MainActivity.this,registerActivity.class);
                startActivity(i);
            }
        });
        logBotton = (Button) findViewById(R.id.subBtn);
        logBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                _name=name.getText().toString();
                _key=key.getText().toString();
                if(_name.equals("")||key.equals("")){
                    Toast.makeText(getApplicationContext(),"输入为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    findUser(_name,_key);
                }
            }
        });
    }
    private void findUser(String name,String thekey){
        SQLiteDatabase personalDB=helper.getReadableDatabase();
        try{
            String sql="select * from userinfo where name=? ";
            Cursor cursor=personalDB.rawQuery(sql,new String[]{name});
            if (cursor.getCount()>0){
                while(cursor.moveToNext()) {
                    String rightkey = cursor.getString(cursor.getColumnIndex("key"));
                    if (rightkey.equals(thekey)) {
                        Intent i = new Intent(MainActivity.this, personalActivity.class);
                        // Bundle bundle=new Bundle();
                        // bundle.putString("thename",name);
                        // Log.i(Tag,name);
                        //getIntent().putExtras(bundle);
                        i.putExtra("username", name);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                        key.setText("");
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"尚未注册",Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            personalDB.close();
        }catch (SQLiteException e)
        {
            Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
        }
    }
}