package com.penley.mymodule.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.penley.mymodule.app.model.Student;
import com.penley.mymodule.app.trans.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String TAG = "MyContentProvider";
    private ContentResolver resolver;
    private ViewPager viewPager;
    private List<ImageView> imageViews;
    private int[] pics = {R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d};

    private android.os.Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentNext = (viewPager.getCurrentItem() + 1);
            int count = viewPager.getChildCount();
            Log.d("lovely", currentNext + "%" + count);
            viewPager.setCurrentItem(currentNext % 5);
        }
    };

    private Handler mHandler = new Handler();
    private int page_id = 1;
    private Runnable switchTask = new Runnable() {
        public void run() {
            viewPager.setCurrentItem(page_id);
            page_id++;
            page_id = page_id % 6;
            Log.d("lovely", page_id + "");
            mHandler.postDelayed(switchTask, 3000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imageViews = new ArrayList<ImageView>();
        int length = pics.length + 2;
        for (int i = 0; i < length; i++) {
            ImageView imageView = new ImageView(this);
            ViewGroup.LayoutParams viewPagerImageViewParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(viewPagerImageViewParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
        }

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageIndex = position;
                if (position == 0) {
                    pageIndex = pics.length;
                } else if (position == imageViews.size() - 1) {
                    pageIndex = 1;
                }

                if (position != pageIndex) {
                    viewPager.setCurrentItem(pageIndex, false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.setOnPageChangeListener(listener);
        viewPager.setCurrentItem(1);
        switchTask.run();


//        insert();//插入数据
//        insert();//插入数据
//        delete(1);//删除第一行
//        update(4);
//        query();//查询

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = imageViews.get(position);
            container.removeView(view);
            view.setImageBitmap(null);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                imageViews.get(position).setImageResource(pics[pics.length - 1]);
            } else if (position == imageViews.size() - 1) {
                imageViews.get(position).setImageResource(pics[0]);
            } else {
                imageViews.get(position).setImageResource(pics[position - 1]);
            }
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


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
