package com.dvvee.dnevnjakapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;
import com.google.android.material.snackbar.Snackbar;

public class DetailedTaskFragment extends Fragment {

    private Task taskToShow;
    private TextView taskDate;
    private TextView taskTitle;
    private TextView taskDuration;
    private TextView taskDescription;

    private Button editBtn;
    private Button deleteBtn;

    private SharedViewModel sharedViewModel;

    private Snackbar snackbar;

    public DetailedTaskFragment(){
        super(R.layout.detail_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        init(view);
    }

    private void init(View view){
        initView(view);
        initListeners(view);
        initObserver();
    }

    private void initView(View view){
        taskDate = view.findViewById(R.id.taskDateTextView);
        taskTitle = view.findViewById(R.id.taskTitleTv);
        taskDuration = view.findViewById(R.id.taskDurationTv);
        taskDescription = view.findViewById(R.id.taskDescriptionTv);

        editBtn = view.findViewById(R.id.editCurrTaskBtn);
        deleteBtn = view.findViewById(R.id.deleteCurrTaskBtn);
    }

    private void initListeners(View view){
        deleteBtn.setOnClickListener(v -> {
//            Task tempTask = taskToShow;
//            this.sharedViewModel.getDayToShow().getValue().getTasks().remove(taskToShow);
//            this.sharedViewModel.getTask().setValue(null);

            snackbar = Snackbar.make(v, "Item " + taskToShow.getTitle() + " removed from list. Are you sure you want it removed?", Snackbar.LENGTH_SHORT);
            snackbar.setAction("Yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedViewModel.getDayToShow().getValue().getTasks().remove(taskToShow);
                    sharedViewModel.getTask().setValue(null);
                }
            });

            snackbar.show();
        });

        editBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Needs to be implemented", Toast.LENGTH_SHORT).show();
            MainActivity mainActivity = (MainActivity) this.getActivity();
            mainActivity.switchPage(4);
        });
    }

    private void initObserver(){
        sharedViewModel.getTask().observe(getViewLifecycleOwner(), task -> {
            if(task == null){
                this.taskToShow = null;
                this.taskDate.setText("");
                this.taskDuration.setText("");
                this.taskTitle.setText("");
                this.taskDescription.setText("");
            }else{
                this.taskToShow = task;
                this.taskDate.setText(this.taskToShow.getDate().toString());
                this.taskDuration.setText(this.taskToShow.getHour() + ":" + this.taskToShow.getMinute() + " - " + this.taskToShow.getEnd_hour() + ":" + this.taskToShow.getEnd_minute());
                this.taskTitle.setText(this.taskToShow.getTitle());
                this.taskDescription.setText(this.taskToShow.getDescription());
            }
        });
    }
}
