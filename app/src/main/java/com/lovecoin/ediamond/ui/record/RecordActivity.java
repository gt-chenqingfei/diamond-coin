package com.lovecoin.ediamond.ui.record;

import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;

/**
 * 我的记录
 */
@MakeActivityStarter
public class RecordActivity extends BaseActivity {

    @BindView(R.id.segment_tab_layout) SegmentTabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;
    private RecordPagerAdapter adapter;
    public String[] titles;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        titles = getResources().getStringArray(R.array.my_records_titles);

        initViewPager();
        initMagicIndicator();
    }

    private void initViewPager() {
        adapter = new RecordPagerAdapter(titles, getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

        });
    }

    @Override
    protected void initData() {

    }

    private void initMagicIndicator() {
        tabLayout.setTabData(titles);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }
}
