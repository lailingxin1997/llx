package com.example.lenovo.hutu;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2017/5/4.
 */

public class Sqliteopenhelper extends SQLiteOpenHelper {
    private static final String DBname="person.db";
    private static final String TABLEnameuser="userinfo";
    private static final String TABLEnamelogItem="diary";
    private static final int Version=1;

    public Sqliteopenhelper(Context context){
        super(context,DBname,null,Version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql1="create table"+" "+TABLEnameuser+"(name varchar,key varchar)";
        db.execSQL(sql1);
        db.execSQL("create table diary(title varchar(20),photopath varchar,content varchar(1000),weather varchar(12)," +
                "created,username varchar,_id integer,constraint pk_id primary key (created,username))");
        db.execSQL("create table if not exists bill(_billID integer primary key autoincrement," +
                "Money varchar[20]," +
                "Date varchar[20]," +
                "title varchar[20]," +
                "category varchar[20],"+
                "username varchar[20])");


    }
    //创建失败则删除后重建

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        if (newVersion > oldVersion) {
            String sql1 = "drop table if exists" + TABLEnameuser;
            String sql2 = "drop table if exists" + TABLEnamelogItem;
            db.execSQL(sql1);
            db.execSQL(sql2);
            this.onCreate(db);
        }
    }
        @Override
        public void onOpen(SQLiteDatabase db){

        }
    public void insertbill(ItemBean itemBean,String name){
        SQLiteDatabase Database=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("Money",itemBean.ItemCost);
        cv.put("Date",itemBean.ItemDate);
        cv.put("title",itemBean.ItemTitle);
        cv.put("category",itemBean.ItemCate);
        cv.put("username",name);
        Database.insert("bill",null,cv);
    }
    public Cursor getallBilldata(){
        SQLiteDatabase Database=getWritableDatabase();
        return Database.query("bill",null,null,null,null,null,"Date");
    }
    public void deleteAlldata(){
        SQLiteDatabase Database=getWritableDatabase();
        Database.delete("bill",null,null);
    }


    public void delete(String Money,String Date,String title,String category,String name ) {
        SQLiteDatabase Database=getWritableDatabase();
        Database.execSQL("delete from bill where Money=? and Date=? and title=? and category=? and username=?", new Object[]{Money,Date,title,category,name});

    }


}
