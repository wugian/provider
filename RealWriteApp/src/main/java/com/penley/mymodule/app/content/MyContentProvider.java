package com.penley.mymodule.app.content;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import com.penley.mymodule.app.db.DbHelper;
import com.penley.mymodule.app.model.Student;

public class MyContentProvider extends ContentProvider {
    //声明数据库帮助类对象
    private DbHelper dbHelper;
    //定义ContentResolver 对象
    private ContentResolver resolver;
    //定义Uri工具类
    private static final UriMatcher Urimatcher;
    //匹配码
    private static final int STUDENT = 1;
    private static final int STUDENT_ID = 2;

    static {
        Urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //添加需要匹配的Uri，匹配则返回相应的匹配码
        Urimatcher.addURI(Student.AUTHORITY, "student", STUDENT);
        Urimatcher.addURI(Student.AUTHORITY, "student/#", STUDENT_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        resolver = this.getContext().getContentResolver();
        int count;
        //根据返回的匹配码进行相应的删除动作
        switch (Urimatcher.match(uri)) {
            case STUDENT:
                count = db.delete(DbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENT_ID: //只删除对于的id
                //getPathSegments()方法得到一个String的List
                String stuId = uri.getPathSegments().get(1);
                count = db.delete(DbHelper.TABLE_NAME, Student._ID + "=" + stuId + (!TextUtils.isEmpty(selection) ?
                        " and (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                //如果传进来的Uri不是我们需要的类型
                throw new IllegalArgumentException("this is Unknown Uri：" + uri);
        }
        resolver.notifyChange(uri, null);
        return count;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //获得可写入的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentResolver resolver = this.getContext().getContentResolver();
        //插入数据，返回行号ID
        long rowId = db.insert(DbHelper.TABLE_NAME, Student.NMAE, values);
        //如果插入成功，返回Uri
        if (rowId > 0) {
            Uri stuUri = ContentUris.withAppendedId(uri, rowId);
            resolver.notifyChange(stuUri, null);//数据发送变化时候，发出通知给注册了相应uri的
            return stuUri;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (Urimatcher.match(uri)) {
            case STUDENT:
                cursor = db.query(DbHelper.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case STUDENT_ID:
                String stuId = uri.getPathSegments().get(1);
                String where = Student._ID + "=" + stuId;
                if (TextUtils.isEmpty(selection)) {
                    where += " and " + selection;
                }
                cursor = db.query(DbHelper.TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
                break;
            default:
                //如果传进来的Uri不是我们需要的类型
                throw new IllegalArgumentException("this is Unknown Uri：" + uri);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();//创建一个可读的数据库
        resolver = this.getContext().getContentResolver();
        int count;
        switch (Urimatcher.match(uri)) {
            case STUDENT:
                count = db.update(DbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case STUDENT_ID:
                String stuId = uri.getPathSegments().get(1);//获得id
                count = db.update(DbHelper.TABLE_NAME, values, Student._ID + "=" + stuId + (!TextUtils.isEmpty(selection) ?
                        " and (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                //如果传进来的Uri不是我们需要的类型
                throw new IllegalArgumentException("this is Unknown Uri：" + uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }
}
