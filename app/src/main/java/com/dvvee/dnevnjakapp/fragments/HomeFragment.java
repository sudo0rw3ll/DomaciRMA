package com.dvvee.dnevnjakapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.model.CalendarDay;
import com.dvvee.dnevnjakapp.recycler.adapter.CalendarAdapter;
import com.dvvee.dnevnjakapp.recycler.adapter.TaskAdapter;
import com.dvvee.dnevnjakapp.recycler.differ.CalendarDiffItemCallback;
import com.dvvee.dnevnjakapp.recycler.differ.TaskDiffItemCallback;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;
import com.dvvee.dnevnjakapp.viewpager.PagerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout mainLayout;
    private SharedViewModel sharedViewModel;
    private CalendarAdapter calendarAdapter;

    private LocalDate selectedDate;

    public HomeFragment(){
        super(R.layout.home_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        selectedDate = LocalDate.now();
        init(view);
    }

    private void init(View view){
        initView(view);
        initRecycler(view);
        initObservers();
    }

    private void initView(View view){
        mainLayout = view.findViewById(R.id.calendarLayout);
        recyclerView = view.findViewById(R.id.tasksRecycler);
    }

    private void initRecycler(View view){

        calendarAdapter = new CalendarAdapter(new CalendarDiffItemCallback(), calendarDay -> {
            Toast.makeText(view.getContext(), calendarDay.getDay() + "", Toast.LENGTH_SHORT).show();
            System.out.println(calendarDay.getDate() + "\n" + calendarDay.getTasks().size() + "\n" +calendarDay.getPriority());

            sharedViewModel.updateDayToShow(calendarDay);
            MainActivity mainActivity = ((MainActivity) this.getActivity());
            mainActivity.switchPage(1);

        });

        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 7));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                System.out.println("Poslednji kog vidim " + ((GridLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition());

                if(!recyclerView.canScrollVertically(1)){
                    sharedViewModel.updateDown();
                }

                if(!recyclerView.canScrollVertically(-1)){
                    sharedViewModel.updateUp();
                }
            }
        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//
//                int firstVisible = gridLayoutManager.findFirstVisibleItemPosition();
//                int lastVisible = gridLayoutManager.findLastVisibleItemPosition();
//
//                if(firstVisible >= 0 && lastVisible >= 0){
//                    CalendarDay calendarDayF = calendarAdapter.getCurrentList().get(firstVisible);
//                    CalendarDay calendarDayL = calendarAdapter.getCurrentList().get(lastVisible);
//
//                    System.out.println("CF " + calendarDayF.getDate());
//                    System.out.println("CL " + calendarDayL.getDate());
//
//                    if(calendarDayF.getDate().getDayOfMonth() < 10){
//                        sharedViewModel.updateUp();
//                    }
//
//                    if(calendarDayL.getDate().getDayOfMonth() > 25){
//                        sharedViewModel.updateDown();
//                    }
//                }
//            }
//        });

        recyclerView.setAdapter(calendarAdapter);
    }

    private void initObservers(){
        sharedViewModel.getCalendarDays().observe(getViewLifecycleOwner(), calendarDays -> {
            calendarAdapter.submitList(calendarDays);
        });

        sharedViewModel.getMonth().observe(getViewLifecycleOwner(), month -> {
            ((TextView) getView().findViewById(R.id.monthBanner)).setText(month);
        });
    }
}
