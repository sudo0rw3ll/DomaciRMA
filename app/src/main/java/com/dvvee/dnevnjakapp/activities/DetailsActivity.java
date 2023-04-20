package com.dvvee.dnevnjakapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.fragments.HomeFragment;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;

public class DetailsActivity extends AppCompatActivity {
    private Task taskToShow;

    private TextView taskDate;
    private TextView taskTitle;
    private TextView taskDuration;
    private TextView taskDescription;

    private Button editBtn;
    private Button deleteBtn;

    private SharedViewModel sharedViewModel;

    public static final String TASK_KEY = "incomingTask";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        init();
    }

    private void init(){
        parseIntent();
        initView();
        initListeners();
    }

    private void initView(){
        taskDate = findViewById(R.id.taskDateTextView);
        taskTitle = findViewById(R.id.taskTitleTv);
        taskDuration = findViewById(R.id.taskDurationTv);
        taskDescription = findViewById(R.id.taskDescriptionTv);
        editBtn = findViewById(R.id.editCurrTaskBtn);
        deleteBtn = findViewById(R.id.deleteCurrTaskBtn);

        taskDate.setText(taskToShow.getDate().toString());
        taskTitle.setText(taskToShow.getTitle());
        taskDuration.setText(taskToShow.getHour() + ":" + taskToShow.getMinute() + "-" + taskToShow.getEnd_hour() + ":" + taskToShow.getEnd_minute());
        taskDescription.setText(taskToShow.getDescription());
    }

    private void initListeners(){
        deleteBtn.setOnClickListener(v -> {
            sharedViewModel.deleteTask(taskToShow);
            finish();
        });
    }

    private void parseIntent(){
        Intent intent = getIntent();

        if(intent.getExtras() != null){
            this.taskToShow = (Task) intent.getExtras().getSerializable(TASK_KEY);
            System.out.println(taskToShow.getTitle());
        }
    }
}
