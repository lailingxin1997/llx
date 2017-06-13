package com.example.lenovo.hutu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lenovo on 2017/4/18.
 */

public class personalActivity extends Activity {
    private Button diaryButton;
    private Button paintButton,checkButton,backButton;
    private TextView userinfo;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);
        Intent i=getIntent();
        name=i.getStringExtra("username");

        diaryButton=(Button)findViewById(R.id.diarybutton);
        paintButton=(Button)findViewById(R.id.paintBut);
        checkButton=(Button)findViewById(R.id.checkBut);
        userinfo=(TextView)findViewById(R.id.userInfo);

        userinfo.setText(name+"欢迎来到糊涂神");
        backButton=(Button)findViewById(R.id.backtoLog);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(personalActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diary = new Intent(personalActivity.this,diarylistActivity.class);
                diary.putExtra("username",name);
                startActivity(diary);

            }
        }

        );
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten=new Intent(personalActivity.this,Bill_mainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("key","main");
                inten.putExtra("username",name);
                inten.putExtra("bundle",bundle);


                startActivity(inten);
            }
        });
        paintButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent paint=new Intent(personalActivity.this,paintActivity.class);
                paint.putExtra("username",name);
                startActivity(paint);
            }
        });
    }
}
