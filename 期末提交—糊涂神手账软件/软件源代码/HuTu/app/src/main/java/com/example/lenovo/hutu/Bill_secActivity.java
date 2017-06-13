package com.example.lenovo.hutu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import java.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.icu.util.Calendar.getInstance;

/**
 * Created by molly on 2017/5/9.
 */

public class Bill_secActivity extends Activity {
    private Button complete;
    private Button one;
    private Button two;
    private Button three,return1,deletedata;
    private Button four, five, six, seven, eight, nine, zero, delete;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_sce);
        RelativeLayout sec = (RelativeLayout) findViewById(R.id.bill_sec);
        final EditText date = (EditText) findViewById(R.id.sce_date);
        final EditText title = (EditText) findViewById(R.id.sce_category);
        final EditText cost = (EditText) findViewById(R.id.sce_cost);
        deletedata= (Button) findViewById(R.id.deletedata);
        final Calendar c = Calendar.getInstance();
        final String category;
        final Bundle bundle = getIntent().getBundleExtra("bundle");
        final String key = bundle.getString("key");
        final String olddate=bundle.getString("date");
        final String oldtitle=bundle.getString("title");
        final String oldcost=bundle.getString("cost");
        final String name=bundle.getString("username");
        //判断是收入还是支出条目
        if (key.equals("add")) {
            sec.setBackgroundResource(R.drawable.zhichu);
            category = "add";
            deletedata.setBackgroundColor(Color.TRANSPARENT);

        }
        else {
            if (key.equals("deduct")) {
                sec.setBackgroundResource(R.drawable.shouru);
                category = "deduct";
                deletedata.setBackgroundColor(Color.TRANSPARENT);

            }
            else{
                deletedata.setText("delete");
                date.setText(bundle.getString("date"));
                title.setText(bundle.getString("title"));
                cost.setText(bundle.getString("cost"));
                if (bundle.getString("Cate").equals("add")) {
                    sec.setBackgroundResource(R.drawable.zhichu);
                    category = "add";
                }
                else {
                    sec.setBackgroundResource(R.drawable.shouru);
                    category = "deduct";
                }
            }
        }
        deletedata.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                if(key.equals("item")){
                    new AlertDialog.Builder(Bill_secActivity.this).setTitle("确定删除？")//设置对话框标题

                            .setMessage("您确定删除这条记录？")//设置显示的内容

                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                    // TODO Auto-generated method stub
                                    Intent inten1 = new Intent(Bill_secActivity.this, Bill_mainActivity.class);
                                    Bundle sec_bundle = new Bundle();
                                    sec_bundle.putString("date", null);
                                    sec_bundle.putString("cost", null);
                                    sec_bundle.putString("title",null);
                                    sec_bundle.putString("Cate", category);
                                    sec_bundle.putString("username", name);
                                    sec_bundle.putString("key", "sec");
                                    inten1.putExtra("bundle", sec_bundle);
                                    startActivity(inten1);


                                }

                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件

                            // TODO Auto-generated method stub


                        }

                    }).show();//在按键响应事件中显示此对话框

                }

            }
        });
        date.setOnTouchListener(new View.OnTouchListener() {

            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            public boolean onTouch(View v, MotionEvent event) {

                date.setInputType(InputType.TYPE_NULL); // 关闭软键盘
                return false;

            }

        });

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Bill_secActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                        Date d1 = new Date(c.getTimeInMillis());
                        date.setText(dateformat.format(d1));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        return1 = (Button) findViewById(R.id.return1);
        return1.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                if(key.equals("item")) {
                    if ((olddate.equals(date.getText().toString())) && (oldcost.equals(cost.getText().toString())) && (oldtitle.equals(title.getText().toString()))) {
                        Intent inten2 = new Intent(Bill_secActivity.this, Bill_mainActivity.class);
                        Bundle sec_bundle = new Bundle();
                        sec_bundle.putString("date", olddate);
                        sec_bundle.putString("cost", oldcost);
                        sec_bundle.putString("title", oldtitle);
                        sec_bundle.putString("Cate", category);
                        sec_bundle.putString("username", name);
                        sec_bundle.putString("key", "sec");
                        inten2.putExtra("bundle", sec_bundle);
                        startActivity(inten2);
                    } else {
                        new AlertDialog.Builder(Bill_secActivity.this).setTitle("放弃正在进行的更改？")//设置对话框标题

                                .setMessage("您确定放弃更改此操作？")//设置显示的内容

                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                    @Override

                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                        // TODO Auto-generated method stub
                                        Intent inten3 = new Intent(Bill_secActivity.this, Bill_mainActivity.class);
                                        Bundle sec_bundle = new Bundle();
                                        sec_bundle.putString("date", olddate);
                                        sec_bundle.putString("cost", oldcost);
                                        sec_bundle.putString("title", oldtitle);
                                        sec_bundle.putString("Cate", category);
                                        sec_bundle.putString("username", name);
                                        sec_bundle.putString("key", "sec");
                                        inten3.putExtra("bundle", sec_bundle);
                                        startActivity(inten3);


                                    }

                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件

                                // TODO Auto-generated method stub


                            }

                        }).show();//在按键响应事件中显示此对话框

                    }
                }
                else{
                    if ((cost.getText().toString().equals("0.00"))||(date.getText().toString().isEmpty())||(title.getText().toString().isEmpty())){
                        Toast.makeText(getApplicationContext(),"编辑框不能为空,保存失败",Toast.LENGTH_SHORT).show();
                        Intent inten4 = new Intent(Bill_secActivity.this, Bill_mainActivity.class);
                        Bundle sec_bundle = new Bundle();
                        sec_bundle.putString("date", null);
                        sec_bundle.putString("cost", null);
                        sec_bundle.putString("title", null);
                        sec_bundle.putString("Cate", category);
                        sec_bundle.putString("username", name);
                        sec_bundle.putString("key", "sec");
                        inten4.putExtra("bundle", sec_bundle);
                        startActivity(inten4);//
                    }
                    else{
                        new AlertDialog.Builder(Bill_secActivity.this).setTitle("放弃要添加的内容？")//设置对话框标题

                                .setMessage(date.getText().toString()+"hhh")//设置显示的内容

                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                    @Override

                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                        // TODO Auto-generated method stub
                                        Intent inten3 = new Intent(Bill_secActivity.this, Bill_mainActivity.class);
                                        Bundle sec_bundle = new Bundle();
                                        sec_bundle.putString("date", null);
                                        sec_bundle.putString("cost", null);
                                        sec_bundle.putString("title", null);
                                        sec_bundle.putString("Cate", category);
                                        sec_bundle.putString("username", name);
                                        sec_bundle.putString("key", "sec");
                                        inten3.putExtra("bundle", sec_bundle);
                                        startActivity(inten3);


                                    }

                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件

                                // TODO Auto-generated method stub


                            }

                        }).show();//在按键响应事件中显示此对话框
                    }
                }

            }
        });
        complete = (Button) findViewById(R.id.compelet);
        complete.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                Intent inten1 = new Intent(Bill_secActivity.this, Bill_mainActivity.class);
                Bundle sec_bundle = new Bundle();
                sec_bundle.putString("date", date.getText().toString());
                sec_bundle.putString("cost", cost.getText().toString());
                sec_bundle.putString("title", title.getText().toString());
                sec_bundle.putString("Cate", category);
                sec_bundle.putString("username", name);
                sec_bundle.putString("key", "sec");
                inten1.putExtra("bundle", sec_bundle);
                startActivity(inten1);

            }
        });
        one = (Button) findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.01;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        two = (Button) findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.02;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        three = (Button) findViewById(R.id.three);
        three.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.03;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        four = (Button) findViewById(R.id.four);
        four.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.04;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        five = (Button) findViewById(R.id.five);
        five.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.05;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        six = (Button) findViewById(R.id.six);
        six.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.06;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        seven = (Button) findViewById(R.id.seven);
        seven.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.07;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        eight = (Button) findViewById(R.id.eight);
        eight.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.08;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        nine = (Button) findViewById(R.id.nine);
        nine.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10 + 0.09;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        zero = (Button) findViewById(R.id.zero);
        zero.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = money * 10;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                String s = cost.getText().toString();
                double money = Double.parseDouble(s);
                money = (money * 100 - (money * 100) % 10) / 1000;
                DecimalFormat df1 = new DecimalFormat("0.00");
                cost.setText(df1.format(money));
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   /* public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Bill_sec Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }*/
}

