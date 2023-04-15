package com.dvvee.dnevnjakapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.recycler.adapter.TaskAdapter;
import com.dvvee.dnevnjakapp.recycler.differ.TaskDiffItemCallback;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class DailyTasksFragment extends Fragment {

    private RecyclerView obligationsRecycler;
    private TaskAdapter taskAdapter;
    private SharedViewModel sharedViewModel;
    private List<Task> calendarTasks;

    public DailyTasksFragment(){
        super(R.layout.daily_tasks_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        init(view);
    }

    private void init(View view){
        initView(view);
        initRecyclers(view);
        initListeners(view);
        initObservers();
        calendarTasks = new ArrayList<>();
    }

    private void initView(View view){
        obligationsRecycler = view.findViewById(R.id.dailyRecyclerView);
    }

    private void initRecyclers(View view){
        taskAdapter = new TaskAdapter(new TaskDiffItemCallback(), task -> {
            Toast.makeText(view.getContext(), task.getTitle() + "", Toast.LENGTH_SHORT).show();
        });

        obligationsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        obligationsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        obligationsRecycler.setAdapter(taskAdapter);
    }

    private void initListeners(View view){
        view.findViewById(R.id.minorButton).setOnClickListener(v -> {
            this.taskAdapter.submitList(filterList(Priority.MINOR));
        });

        view.findViewById(R.id.midButton).setOnClickListener(v -> {
            this.taskAdapter.submitList(filterList(Priority.MID));
        });

        view.findViewById(R.id.highButton).setOnClickListener(v -> {
            this.taskAdapter.submitList(filterList(Priority.HIGH));
        });
    }

    private List<Task> filterList(Priority priority){
        List<Task> tasksFiltered = new ArrayList<Task>();
        List<Task> tasks = sharedViewModel.getDayToShow().getValue().getTasks();

        for(int i=0;i<tasks.size();i++){
            if(tasks.get(i).getPriority() == priority)
                tasksFiltered.add(tasks.get(i));
        }

        return tasksFiltered;
    }

    private void initObservers(){
        sharedViewModel.getDayToShow().observe(getViewLifecycleOwner(), day -> {
            System.out.println("Date: " + day.getDate().toString() + " " + day.getTasks().size());
            ((TextView) getView().findViewById(R.id.dailyDateTextView)).setText(day.getDate().toString());
            this.taskAdapter.submitList(day.getTasks());
        });
    }
}
