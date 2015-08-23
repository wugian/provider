package com.penley.mymodule.app.model;

import android.net.Uri;

/**
 * Created by wugian on 15/8/23.
 */
public class Student {
    public static final String _ID = "id";
    public static final String NMAE = "name";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String AUTHORITY = "com.penley.mymodule.app";

    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/student");
}
