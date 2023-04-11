package com.dvvee.dnevnjakapp.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dvvee.dnevnjakapp.fragments.DailyTasksFragment;
import com.dvvee.dnevnjakapp.fragments.HomeFragment;
import com.dvvee.dnevnjakapp.fragments.UserProfileFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int ITEM_COUNT = 3;

    public static final int CALENDAR_FRAGMENT = 0;
    public static final int DAILY_TASKS_FRAGMENT = 1;
    public static final int USER_PROFILE_FRAGMENT = 2;

    public PagerAdapter(@NonNull FragmentManager fm){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch(position){
            case CALENDAR_FRAGMENT: fragment = new HomeFragment(); break;
            case DAILY_TASKS_FRAGMENT: fragment = new DailyTasksFragment(); break;
            default: fragment = new UserProfileFragment(); break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
