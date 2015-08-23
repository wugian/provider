package com.penley.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
    public static final String TAG = "lovely";
    ProgressDialog progressDialog;
    private ContentResolver resolver;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
//        initProgressDialog();
        delete(6);
        delete(8);
        delete(7);
//        update(1);
//        insert();

        query();
    }
    //插入数据
    public void insert() {
        Uri uri = Student.CONTENT_URI;
        //获得ContentResolver对象
        resolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        //添加学生信息
        values.put(Student.NMAE, "杰克");
        values.put(Student.GENDER, "男");
        values.put(Student.AGE, 20);
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
        values.put(Student.NMAE, "xiaohone");
        values.put(Student.GENDER, "zhong");
        values.put(Student.AGE, 18);
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

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View inflate = layoutInflater.inflate(R.layout.i_progress, null);
        Button btn = (Button) inflate.findViewById(R.id.cancel_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        ImageView image = (ImageView) inflate.findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.a_r);
        image.startAnimation(animation);
//        animation.start();

        progressDialog.show();
        progressDialog.setContentView(inflate);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
