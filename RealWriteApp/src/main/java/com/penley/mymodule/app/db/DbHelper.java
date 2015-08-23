package com.penley.mymodule.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.penley.mymodule.app.model.Student;

/**
 * Created by wugian on 15/8/23.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "student.db";
    private static int VERSION_CODE = 1;
    public static String TABLE_NAME = "student";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION_CODE);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建学生信息表，包括四个字段
        //包括id、姓名、性别、年龄
        String sql = "create table " + TABLE_NAME + " ("
                + Student._ID + " integer primary key,"
                + Student.NMAE + " text,"
                + Student.GENDER + " text,"
                + Student.AGE + " integer " + ");";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exits student";

        if (i < i1) {
            sqLiteDatabase.execSQL(sql);
        }
    }
}
