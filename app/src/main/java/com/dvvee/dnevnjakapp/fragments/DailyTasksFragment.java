package com.dvvee.dnevnjakapp.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dvvee.dnevnjakapp.R;

public class DailyTasksFragment extends Fragment {

    public DailyTasksFragment(){
        super(R.layout.daily_tasks_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        initListeners(view);
    }

    private void initListeners(View view){

    }
}
