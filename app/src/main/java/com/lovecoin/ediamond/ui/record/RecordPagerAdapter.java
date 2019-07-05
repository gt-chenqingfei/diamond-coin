package com.lovecoin.ediamond.ui.record;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created on 2017/11/8 0008.
 */

public class RecordPagerAdapter extends FragmentPagerAdapter {

    public String[] titles;

    public RecordPagerAdapter(String[] titles, FragmentManager fm) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return RecordListFragmentStarter.newInstance(position);
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
