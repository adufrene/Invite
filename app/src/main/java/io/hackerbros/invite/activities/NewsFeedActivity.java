package io.hackerbros.invite.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;

import io.hackerbros.invite.R;
import io.hackerbros.invite.fragments.TitledFragment;
import io.hackerbros.invite.fragments.ProfileFragment;
import io.hackerbros.invite.fragments.MapFragment;
import io.hackerbros.invite.feed.NewsFeedFragment;

public class NewsFeedActivity extends FragmentActivity {
    NewsFeedPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        findViewById(R.id.add_event_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this, EventActivity.class));
            }
        });

        mPagerAdapter = new NewsFeedPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });
        mViewPager.setAdapter(mPagerAdapter);

        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        for (int i = 0; i < 3; ++i) {
            actionBar.addTab(
                    actionBar.newTab()
                        .setText(mPagerAdapter.getPageTitle(i))
                        .setTabListener(tabListener));
        }
    }

    private static class NewsFeedPagerAdapter extends FragmentPagerAdapter {
        TitledFragment[] fragments;
        public NewsFeedPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new TitledFragment[3];

            fragments[0] = new NewsFeedFragment();
            fragments[1] = new ProfileFragment();
            fragments[2] = new MapFragment();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position].getTitle();
        }
    }
}
