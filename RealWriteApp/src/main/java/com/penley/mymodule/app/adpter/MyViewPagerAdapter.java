package com.penley.mymodule.app.adpter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by wugian on 15/8/30.
 */
public class MyViewPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
