package com.dvvee.dnevnjakapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.dvvee.dnevnjakapp.db.SQLiteManager;
import com.dvvee.dnevnjakapp.fragments.HomeFragment;
import com.dvvee.dnevnjakapp.fragments.LoginFragment;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.model.User;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;
import com.dvvee.dnevnjakapp.viewpager.PagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SharedViewModel sharedViewModel;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        this.users = new ArrayList<User>();
//        initFrag();
        init();
        loadFromDb();
    }

    private void loadFromDb(){
//        SQLiteManager manager = SQLiteManager.instanceOfDatabase(this);
////        manager.deleteUser("vnikolic7821rn@raf.rs");
////        manager.addUser(new User(1, "vnikolic7821rn@raf.rs", "vnikolic", "ovojegluppass", "https://i.pinimg.com/originals/b1/78/36/b1783629540e8d1306109a5673211caf.jpg"));
//        this.users = manager.getUserList();
//        System.out.println("USERS -> " + this.users);
//        this.sharedViewModel.getUser().setValue(this.users.get(0));
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        System.out.println("USERID -> " + sharedPreferences.getInt("userId", -1));
        SQLiteManager manager = SQLiteManager.instanceOfDatabase(this);
        User user = manager.getById(sharedPreferences.getInt("userId", -1));

        this.sharedViewModel.getUser().setValue(user);
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

    public void switchPage(int idx){
        this.viewPager.setCurrentItem(idx);
    }

    private void initFrag(){
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragmentContainer, new HomeFragment());
//        transaction.commit();
    }
}