package com.example.lenovo.hutu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by lenovo on 2017/5/6.
 */

public class DiaryDao {
    private Sqliteopenhelper sqliteopenhelper;
    private SQLiteDatabase sqLiteDatabase;
    private final String Tag="检查";

    public DiaryDao(Context context){
        sqliteopenhelper=new Sqliteopenhelper(context);
    }
    public boolean save(day day,String name){
        SQLiteDatabase personalDB=sqliteopenhelper.getWritableDatabase();
        int id=0;
        id=getid(name);
       // day theday=getDayByCreated(time,name);
        ContentValues values=new ContentValues();
        values.put("username",day.getUsername());
        values.put("title",day.getTitle());
        values.put("weather",day.getWeather());
        values.put("created",day.getCreated());
        values.put("content",day.getContent());
        values.put("_id",id);
        values.put("photopath",day.getPhotopath());
        personalDB.insert("diary",null,values);
        return true;

    }
    public boolean delete(day day,String time)
    {
        SQLiteDatabase personalDB=sqliteopenhelper.getWritableDatabase();
       personalDB.execSQL("delete from diary where created=? and username=?",new Object[]{time,day.getUsername()});
        return true;
    }
    public boolean update(day day,String time,String name,int id){
        sqLiteDatabase =sqliteopenhelper.getWritableDatabase();// 得到的是同一个数据库实例
        sqLiteDatabase.execSQL(
                "update diary set title=?,_id=?,weather=?,content=?,photopath=? where created=? and username=?",
                new Object[] {day.getTitle(),id+"",day.getWeather(),day.getContent(),day.getPhotopath(),
                       time,name });
        return  true;
        
    }
    public int count(String name){
        int count=0;
        sqLiteDatabase=sqliteopenhelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select count(*) from diary where username=?",new String[]{name}
        );
        cursor.moveToFirst();
        count=cursor.getInt(0);
        return count;
    }
    public Cursor getAllDairies(String name){
        sqLiteDatabase = sqliteopenhelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from diary where username=? ",new String[]{name});
        return cursor;
    }
    public int getid(String name){
        int id=0;
        id=count(name)+1;
        return id;

    }
    public Cursor findCursor(String name)
    {
        sqLiteDatabase=sqliteopenhelper.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery("select _id as username,title,weather,content,created,username,photopath from diary where username=?",new String[]{name});
        return c;
    }
    public day getDayByCreated(String time,String name)
    {
        sqLiteDatabase=sqliteopenhelper.getWritableDatabase();
        day day=null;
        Cursor cursor=sqLiteDatabase.rawQuery("select * " +
                "from diary where created=? and username=?",new String[]{time,name});
        if (cursor.moveToFirst()){

            String title=cursor.getString(cursor.getColumnIndex("title"));
            String created=time;
            String weather=cursor.getString(cursor.getColumnIndex("weather"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            String filepath=cursor.getString(cursor.getColumnIndex("photopath"));
            String username=name;
            day=new day(created,title,weather,username,content,filepath);
            Log.v(Tag,"seccess");
        }
        return  day;
    }

}
