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

import java.util.Calendar;

public class EditTaskFragment extends Fragment {

    private Task task;

    private EditText editTaskTitle;
    private EditText editTaskDescription;
    private EditText editStartDate;
    private EditText editStartTime;
    private EditText editEndTime;

    private RadioButton lowPriorityBtn;
    private RadioButton mediumPriorityBtn;
    private RadioButton highPriorityBtn;

    private Button saveButton;

    private SharedViewModel sharedViewModel;

    public EditTaskFragment(){
        super(R.layout.edit_task_frag);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        init(view);
    }

    private void init(View view){
        initView(view);
        initListeners();
        initObserver();
    }

    private void initView(View view){
        editTaskTitle = view.findViewById(R.id.edit_task_title);
        editTaskDescription = view.findViewById(R.id.edit_task_description);
//        editStartDate = view.findViewById(R.id.edit_start_date);
        editStartTime = view.findViewById(R.id.edit_start_time);
        editEndTime = view.findViewById(R.id.edit_end_time);


        lowPriorityBtn = view.findViewById(R.id.priority_low);
        mediumPriorityBtn = view.findViewById(R.id.priority_medium);
        highPriorityBtn = view.findViewById(R.id.priority_high);

        saveButton = view.findViewById(R.id.saveBtn);
    }

    private void initListeners(){
        editStartTime.setOnClickListener(v -> {
            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);
            int minute = currTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    editStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    task.setHour(hourOfDay);
                    task.setMinute(minute);
                }
            }, hour, minute, false);

            timePickerDialog.show();
        });

        editEndTime.setOnClickListener(v -> {
            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);
            int minute = currTime.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    editEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    task.setEnd_hour(hourOfDay);
                    task.setEnd_minute(minute);
                }
            }, hour, minute, false);

            timePickerDialog.show();
        });

        saveButton.setOnClickListener(v -> {
//            Toast.makeText(getContext(), "To be implemented", Toast.LENGTH_SHORT).show();

            String newTitle = editTaskTitle.getText().toString();
            String newDescription = editTaskDescription.getText().toString();

            Priority priority = null;

            if(lowPriorityBtn.isChecked()){
                priority = Priority.MINOR;
            }

            if(mediumPriorityBtn.isChecked()){
                priority = Priority.MID;
            }

            if(highPriorityBtn.isChecked()){
                priority = Priority.HIGH;
            }

            task.setTitle(newTitle);
            task.setDescription(newDescription);
            task.setPriority(priority);

            System.out.println("New task: \n" + task.getTitle() + "\n" + task.getDescription() + "\n" + task.getHour() + "\n" + task.getMinute() + "\n" + task.getEnd_hour() + "\n" + task.getEnd_minute() + "\n" + task.getPriority());
            int idx = this.sharedViewModel.getDayToShow().getValue().getTasks().indexOf(this.sharedViewModel.getTask().getValue());

            this.sharedViewModel.getDayToShow().getValue().getTasks().set(idx, this.task);
            this.sharedViewModel.getTask().setValue(task);

            clearFields();
        });
    }

    private void clearFields(){
        this.editTaskTitle.setText("");
        this.editTaskDescription.setText("");
        this.editStartTime.setText("");
        this.editEndTime.setText("");
        this.lowPriorityBtn.setChecked(true);

        MainActivity mainActivity = (MainActivity) this.getActivity();
        mainActivity.switchPage(1);
    }

    private void initObserver(){
        sharedViewModel.getTask().observe(getViewLifecycleOwner(), task -> {
            if(task == null){
                this.task = null;
                clearFields();
            }else {
                this.task = task;
                this.editTaskTitle.setText(this.task.getTitle());
                this.editTaskDescription.setText(this.task.getDescription());
                this.editStartTime.setText(String.format("%02d:%02d", this.task.getHour(), this.task.getMinute()));
                this.editEndTime.setText(String.format("%02d:%02d", this.task.getEnd_hour(), this.task.getEnd_minute()));

                if(this.task.getPriority() == Priority.HIGH)
                    this.highPriorityBtn.setChecked(true);
                if(this.task.getPriority() == Priority.MID)
                    this.mediumPriorityBtn.setChecked(true);
                if(this.task.getPriority() == Priority.MINOR)
                    this.lowPriorityBtn.setChecked(true);
            }
        });
    }
}
