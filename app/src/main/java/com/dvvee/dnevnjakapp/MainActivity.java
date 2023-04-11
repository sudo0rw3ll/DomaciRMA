package com.dvvee.dnevnjakapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.dvvee.dnevnjakapp.fragments.HomeFragment;
import com.dvvee.dnevnjakapp.fragments.LoginFragment;
import com.dvvee.dnevnjakapp.viewpager.PagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initFrag();
        init();
    }

    private void init(){
        initViewPager();
        initNavigation();
    }

    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    private void initNavigation(){
        ((BottomNavigationView) findViewById(R.id.bottomNavigation)).setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.calendarFrag: viewPager.setCurrentItem(PagerAdapter.CALENDAR_FRAGMENT, false); break;
                case R.id.dailyTasksFrag: viewPager.setCurrentItem(PagerAdapter.DAILY_TASKS_FRAGMENT, false); break;
                case R.id.userProfileFrag: viewPager.setCurrentItem(PagerAdapter.USER_PROFILE_FRAGMENT, false); break;
            }
            return true;
        });
    }

    private void initFrag(){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragmentContainer, new HomeFragment());
//        transaction.commit();
    }
}