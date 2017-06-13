package com.example.lenovo.hutu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by molly on 2017/5/9.
 */

public class Bill_mainActivity extends Activity {
    private Button bn,deduct,tran_chart;
    private final String Tag="检查";
    public Sqliteopenhelper mDatabaseHelper;
    public List<ItemBean> mItemBean;
    public MyAdapter myAdapter;
    public double sum;
    private String name;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_main);

        mDatabaseHelper = new Sqliteopenhelper(this);
        mItemBean = new ArrayList<>();
        final ListView listView = (ListView) findViewById(R.id.lv_main);
        final EditText allcost=(EditText) findViewById(R.id.sumcost);
        final TextView usertitle=(TextView)findViewById(R.id.textView2);
        RelativeLayout main= (RelativeLayout) findViewById(R.id.bill_main);
        String s=allcost.getText().toString();
        sum = Double.parseDouble(s);


        //关闭edittext的软键盘（有焦点时）
        allcost.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                allcost.setInputType(InputType.TYPE_NULL); // 关闭软键盘

                return false;

            }
        });
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(allcost.getWindowToken(), 0);


        //读取从sec中传递过来的新增条目
        final Bundle bundle = getIntent().getBundleExtra("bundle");
        String key = bundle.getString("key");
        if(key.equals("main")){
            name = getIntent().getStringExtra("username");
            usertitle.setText(name);
        }

        //Log.v(Tag,name);
        if (key.equals("sec")) {

            name = bundle.getString("username");
            usertitle.setText(name);
            String cost = bundle.getString("cost");
            String date = bundle.getString("date");
            String title = bundle.getString("title");
            String Cate=bundle.getString("Cate");

            if ((cost != null)||(date!=null)||(title!=null)){
                ItemBean itemBean1 = new ItemBean();
                itemBean1.ItemCost = cost;
                itemBean1.ItemDate = date;
                itemBean1.ItemTitle = title;
                itemBean1.ItemCate=Cate;
                mDatabaseHelper.insertbill(itemBean1,name);
                // mItemBean.add(itemBean1);
                //myAdapter.notifyDataSetChanged();
            }
            else{
                if(cost==null){
                    Toast.makeText(getApplicationContext(),"金额不能为空,保存失败",Toast.LENGTH_SHORT).show();
                }
                else if(date==null){
                    Toast.makeText(getApplicationContext(),"时间不能为空,保存失败",Toast.LENGTH_SHORT).show();
                }
                else if(title==null){
                    Toast.makeText(getApplicationContext(),"分类不能为空,保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        }

        //初始化itemlist

        InitBilldata(name);
        DecimalFormat df1 = new DecimalFormat("0.00");
        //设置总体金额

        allcost.setText(df1.format(sum));
        myAdapter = new MyAdapter(this, mItemBean);
        listView.setAdapter(myAdapter);
        bn = (Button) findViewById(R.id.add);
        bn.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                Intent inten = new Intent(Bill_mainActivity.this, Bill_secActivity.class);
                Bundle main_bundle = new Bundle();
                main_bundle.putString("key", "add");
                main_bundle.putString("username", name);
                inten.putExtra("bundle", main_bundle);
                startActivity(inten);
            }
        });
        deduct = (Button) findViewById(R.id.deduct);
        deduct.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                Intent inten1 = new Intent(Bill_mainActivity.this, Bill_secActivity.class);
                Bundle main_bundle = new Bundle();
                main_bundle.putString("key", "deduct");
                main_bundle.putString("username", name);
                inten1.putExtra("bundle", main_bundle);
                startActivity(inten1);
            }
        });
        tran_chart=(Button)findViewById(R.id.trans_chart);
        tran_chart.setOnClickListener(new View.OnClickListener() {//创建监听
            public void onClick(View v) {
                Intent inten6= new Intent(Bill_mainActivity.this, personalActivity.class);
                inten6.putExtra("username",name);
                startActivity(inten6);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Bill_mainActivity.this,Bill_secActivity.class);
                ItemBean itemBean = mItemBean.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("Cate",itemBean.ItemCate);
                bundle.putString("title",itemBean.ItemTitle);
                bundle.putString("date",itemBean.ItemDate);
                bundle.putString("cost",itemBean.ItemCost);
                bundle.putString("username",name);
                bundle.putString("key","item");
                intent.putExtra("bundle",bundle);
                mDatabaseHelper.delete(itemBean.ItemCost,itemBean.ItemDate,itemBean.ItemTitle,itemBean.ItemCate,name);
                startActivity(intent);
            }
        });

    }



    private void InitBilldata(String thename) {

        Cursor cursor=mDatabaseHelper.getallBilldata();
        if(cursor!=null){
            while (cursor.moveToNext()){
                ItemBean itemBean=new ItemBean();
                String username1=cursor.getString(cursor.getColumnIndex("username"));
                Log.v(Tag,username1);
                if (username1.equals(thename)){
                    itemBean.ItemTitle=cursor.getString(cursor.getColumnIndex("title"));
                    itemBean.ItemDate=cursor.getString(cursor.getColumnIndex("Date"));
                    itemBean.ItemCost=cursor.getString(cursor.getColumnIndex("Money"));
                    itemBean.ItemCate=cursor.getString(cursor.getColumnIndex("category"));
                    mItemBean.add(itemBean);
                    if( itemBean.ItemCate.equals("add")){
                        sum=sum+Double.parseDouble(itemBean.ItemCost);
                    }
                    else{
                        sum=sum-Double.parseDouble(itemBean.ItemCost);
                    }}

            }
            cursor.close();
        }
    }
}
