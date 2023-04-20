package com.dvvee.dnevnjakapp.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class AddTaskFragment extends Fragment {

    private EditText newTitle;
    private EditText newDescription;
    private EditText newStartTime;
    private EditText newEndTime;

    private RadioButton newMinorRadio;
    private RadioButton newMediumRadio;
    private RadioButton newHighRadio;

    private Button addButton;

    private SharedViewModel sharedViewModel;

    public AddTaskFragment(){
        super(R.layout.add_task_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        init(view);
    }

    private void init(View view){
        initView(view);
        initListeners();
    }

    private void initView(View view){
        newTitle = view.findViewById(R.id.new_task_title);
        newDescription = view.findViewById(R.id.new_task_description);
        newStartTime = view.findViewById(R.id.new_start_time);
        newEndTime = view.findViewById(R.id.new_end_time);
        newMinorRadio = view.findViewById(R.id.new_priority_low);
        newMediumRadio = view.findViewById(R.id.new_priority_medium);
        newHighRadio = view.findViewById(R.id.new_priority_high);

        addButton = view.findViewById(R.id.addBtn);
    }

    private void initListeners(){
        if(this.sharedViewModel.getDayToShow().getValue() == null)
            return;

        Task task = new Task(2, "", "", this.sharedViewModel.getDayToShow().getValue().getDate(), 0, 0,0,0, Priority.HIGH, 1);

        newStartTime.setOnClickListener(v -> {
            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);
            int minute = currTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    newStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    task.setHour(hourOfDay);
                    task.setMinute(minute);
                }
            }, hour, minute, false);

            timePickerDialog.show();
        });

        newEndTime.setOnClickListener(v -> {
            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);
            int minute = currTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    newEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    task.setEnd_hour(hourOfDay);
                    task.setEnd_minute(minute);
                }
            }, hour, minute, false);

            timePickerDialog.show();
        });

        addButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "To be implemented", Toast.LENGTH_SHORT).show();

            String title = newTitle.getText().toString();
            String desc = newDescription.getText().toString();

            task.setTitle(title);
            task.setDescription(desc);

            Priority priority = null;

            if(newMinorRadio.isChecked()){
                priority = Priority.MINOR;
            }

            if(newMediumRadio.isChecked()){
                priority = Priority.MID;
            }

            if(newHighRadio.isChecked()){
                priority = Priority.HIGH;
            }

            task.setPriority(priority);

            if(!checkTaskTimes(task)) {
                this.sharedViewModel.getDayToShow().getValue().getTasks().add(task);
                Toast.makeText(getContext(), "Task has been added", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "Task with selected time already exists", Toast.LENGTH_LONG).show();

            newTitle.setText("");
            newDescription.setText("");
            newStartTime.setText("");
            newEndTime.setText("");
            newMinorRadio.setChecked(true);

            MainActivity mainActivity = (MainActivity) this.getActivity();
            mainActivity.switchPage(1);
        });
    }

    private boolean checkTaskTimes(Task task){
        List<Task> availableTasks = this.sharedViewModel.getDayToShow().getValue().getTasks();

        System.out.println("Tasks -> " + availableTasks.size());

        for(int i=0;i<availableTasks.size();i++){
            Task curr = (Task) availableTasks.get(i);
            System.out.println("Matching task " + task.getHour() + ":" + task.getMinute() + " with list task " + curr.getHour() + ":" + curr.getMinute());
            if(curr.getHour() == task.getHour() && curr.getMinute() == task.getMinute()){
                return true;
            }
        }

        return false;
    }
}
