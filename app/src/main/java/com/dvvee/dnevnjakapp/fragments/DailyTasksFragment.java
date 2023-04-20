package com.dvvee.dnevnjakapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.activities.DetailsActivity;
import com.dvvee.dnevnjakapp.model.CalendarDay;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.recycler.adapter.TaskAdapter;
import com.dvvee.dnevnjakapp.recycler.differ.TaskDiffItemCallback;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;
import com.dvvee.dnevnjakapp.viewpager.PagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DailyTasksFragment extends Fragment {

    private RecyclerView obligationsRecycler;
    private TaskAdapter taskAdapter;
    private SharedViewModel sharedViewModel;
    private List<Task> calendarTasks;

    private Button midButton;
    private Button highButton;
    private Button minorButton;
    private FloatingActionButton floatingActionButton;
    private boolean checked = false;

    private CheckBox pastObligations;

    private EditText searchTasks;

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
        initListeners();
        initObservers();
        calendarTasks = new ArrayList<>();
    }

    private void initView(View view){
        obligationsRecycler = view.findViewById(R.id.dailyRecyclerView);
        midButton = (Button) view.findViewById(R.id.midButton);
        minorButton = (Button) view.findViewById(R.id.minorButton);
        highButton = (Button) view.findViewById(R.id.highButton);
        floatingActionButton = view.findViewById(R.id.floatingActionAdd);
        searchTasks = (EditText) view.findViewById(R.id.searchTasksEditText);

        pastObligations = (CheckBox) view.findViewById(R.id.pastObligationsBtn);
        pastObligations.setChecked(true);
    }

    private void initRecyclers(View view){
        taskAdapter = new TaskAdapter(new TaskDiffItemCallback(), task -> {
            Toast.makeText(view.getContext(), task.getTitle() + "", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent(this.getContext(), DetailsActivity.class);
//            intent.putExtra(DetailsActivity.TASK_KEY, task);
//            startActivity(intent);
            sharedViewModel.getTask().setValue(task);
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.switchPage(3);
        }, sharedViewModel);

        obligationsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));

        obligationsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        obligationsRecycler.setAdapter(taskAdapter);
    }

    private void initListeners(){
        initFilterButtons();
        initSearch();
        initFloating();
//        initShowPastObligations();
    }

    private void initFilterButtons(){
        minorButton.setOnClickListener(v -> {
            this.taskAdapter.submitList(filterList(Priority.MINOR));
        });

        midButton.setOnClickListener(v -> {
            this.taskAdapter.submitList(filterList(Priority.MID));
        });

        highButton.setOnClickListener(v -> {
            this.taskAdapter.submitList(filterList(Priority.HIGH));
        });
    }

    private void initFloating(){
        floatingActionButton.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) this.getActivity();
            mainActivity.switchPage(PagerAdapter.ADD_TASK_FRAGMENT);
        });
    }

//    private void initShowPastObligations(){
//        pastObligations.setOnClickListener(v -> {
//            if(pastObligations.isChecked()){
//                Toast.makeText(getContext(), "Heeey", Toast.LENGTH_SHORT).show();
//                List<Task> curr = taskAdapter.getCurrentList();
//                List<Task> copy = new ArrayList<Task>(curr);
//                List<Task> past = getPastObligations();
//                past.addAll(copy);
//
//                List<Task> sorted = past.stream().sorted(Comparator.comparingInt(Task::getHour)).collect(Collectors.toList());
//
//                taskAdapter.submitList(sorted);
//            }else{
//                Toast.makeText(getContext(), "Heeeya", Toast.LENGTH_SHORT).show();
//                List<Task> curr = taskAdapter.getCurrentList();
//                List<Task> copy = new ArrayList<Task>(curr);
//                List<Task> past = getPastObligations();
//
//                copy.removeAll(past);
//
//                List<Task> sorted = copy.stream()
//                        .sorted(Comparator.comparingInt(Task::getHour).thenComparing(Task::getMinute))
//                        .collect(Collectors.toList());
//
//                taskAdapter.submitList(sorted);
//            }
//        });
//    }

    private List<Task> getPastObligations(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        System.out.println("CALENDAR HOUR " + hour);

        List<Task> pastObligations = new ArrayList<>();
        if(sharedViewModel.getDayToShow().getValue() != null){
            List<Task> tasks = sharedViewModel.getDayToShow().getValue().getTasks();

            for(int i=0;i<tasks.size();i++){
                Task task = tasks.get(i);
                if(task.getEnd_hour() < hour){
                    pastObligations.add(task);
                }
            }
        }

        return pastObligations;
    }

    private void initSearch(){
        searchTasks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taskAdapter.submitList(filterTasks(s.toString()));
            }
        });
    }

    private List<Task> filterTasks(String s){
        if(sharedViewModel.getDayToShow().getValue() == null)
            return null;

        List<Task> filtered = sharedViewModel.getDayToShow().getValue().getTasks().stream().filter(task -> task.getTitle().toLowerCase().contains(s.toLowerCase())).collect(Collectors.toList());
        return filtered;
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
