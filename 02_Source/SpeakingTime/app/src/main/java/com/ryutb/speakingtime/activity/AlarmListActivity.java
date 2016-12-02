package com.ryutb.speakingtime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ryutb.speakingtime.R;
import com.ryutb.speakingtime.bean.AlarmObject;
import com.ryutb.speakingtime.fragment.AlarmForDriverFragment;
import com.ryutb.speakingtime.fragment.AlarmListTabFragment;
import com.ryutb.speakingtime.sql.VCDatabaseOpenHelper;
import com.ryutb.speakingtime.util.Define;

import java.util.ArrayList;
import java.util.List;


public class AlarmListActivity extends VLBaseActivity {

    private static final String TAG = Define.createTAG("AlarmListActivity");
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private VCDatabaseOpenHelper mDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list_activity);

        mDbHelper = new VCDatabaseOpenHelper(getApplicationContext());
        mDbHelper.openDatabase();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AlarmListTabFragment(), "Alarm List");
        adapter.addFragment(new AlarmForDriverFragment(), "Alarm For Driver");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
