package com.penley.mymodule.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.penley.mymodule.app.model.Student;


public class MainActivity extends Activity {
    private static final String TAG = "MyContentProvider";
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        insert();//插入数据
//        insert();//插入数据
//        delete(1);//删除第一行
//        update(4);
        query();//查询
    }

    //插入数据
    public void insert() {
        Uri uri = Student.CONTENT_URI;
        //获得ContentResolver对象
        resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        //添加学生信息
        values.put(Student.NMAE, "Jack1");
        values.put(Student.GENDER, "男");
        values.put(Student.AGE, 30);
        //将信息插入
        resolver.insert(uri, values);
        Log.i(TAG, uri.toString());
    }

    //删除数据
    //删除第id行
    public void delete(Integer id) {
        //指定uri
        Uri uri = ContentUris.withAppendedId(Student.CONTENT_URI, id);
        resolver = this.getContentResolver();
        resolver.delete(uri, null, null);
    }

    //更新
    public void update(Integer id) {
        Uri uri = ContentUris.withAppendedId(Student.CONTENT_URI, id);
        resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Student.NMAE, "xiaofang");
        values.put(Student.GENDER, "女");
        values.put(Student.AGE, 25);
        resolver.update(uri, values, null, null);
    }

    //查询
    public void query() {
        Uri uri = Student.CONTENT_URI;
        String[] PROJECTION = new String[]{
                Student._ID,
                Student.NMAE,
                Student.GENDER,
                Student.AGE
        };
        resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, PROJECTION, null, null, null);
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                int id = cursor.getInt(0);//获得id
                String name = cursor.getString(1);//取得姓名
                String gender = cursor.getString(2);//取得性别
                int age = cursor.getInt(3);//取得年龄
                //输出日记
                Log.i(TAG, "id:" + id + " name:" + name + " gender:" + gender + " age:" + age);
            }
        }

    }
}
