package com.rahuljanagouda.popularmoviestwo.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by rahuljanagouda on 16/08/16.
 */
public class CustomTabLayout extends TabLayout {

    Context mContext;
    public CustomTabLayout(Context context) {
        super(context);
        mContext = context;
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);


    }

    public void setupWithViewPager(@Nullable ViewPager viewPager, Context mContext) {
        setupWithViewPager(viewPager);

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto-slab-bold.ttf");

        if (tf != null)
        {
            this.removeAllTabs();

            ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

            PagerAdapter adapter = viewPager.getAdapter();

            for (int i = 0, count = adapter.getCount(); i < count; i++)
            {
                Tab tab = this.newTab();
                this.addTab(tab.setText(adapter.getPageTitle(i)));
                AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                view.setTypeface(tf, Typeface.NORMAL);
            }
        }
    }
}
