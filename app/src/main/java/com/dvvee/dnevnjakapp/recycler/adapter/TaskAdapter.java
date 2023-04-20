package com.dvvee.dnevnjakapp.recycler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.fragments.EditTaskFragment;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.function.Consumer;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.ViewHolder> {

    private final Consumer<Task> onTaskClicked;
    private SharedViewModel sharedViewModel;

    public TaskAdapter(@NonNull DiffUtil.ItemCallback<Task> diffCallback, Consumer<Task> onTaskClicked, SharedViewModel sharedViewModel){
        super(diffCallback);
        this.onTaskClicked = onTaskClicked;
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_obligation, parent, false);
        return new ViewHolder(view, parent.getContext(), position -> {
            Task task = getItem(position);
            onTaskClicked.accept(task);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = getItem(position);
        holder.bind(task);
        holder.itemView.findViewById(R.id.deleteTaskIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, "Item " + task.getTitle() + " removed from list. Are you sure you want it removed?", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedViewModel.deleteTaskForCurr(task);
                        notifyDataSetChanged();
                    }
                });

                snackbar.show();
            }
        });

        holder.itemView.findViewById(R.id.editTaskIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "HEJ EDIT", Toast.LENGTH_SHORT).show();
                sharedViewModel.getTask().setValue(task);
                MainActivity mainActivity = (MainActivity) v.getContext();
                mainActivity.switchPage(4);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context context;

        public ViewHolder(@NonNull View itemView, Context context, Consumer<Integer> onItemClicked) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(v -> {
                if(getBindingAdapterPosition() != RecyclerView.NO_POSITION){
                    onItemClicked.accept(getBindingAdapterPosition());
                }
            });
        }

        public void bind(Task task){
            colorPastTasks(task);

            ((TextView) itemView.findViewById(R.id.taskTitleTextView)).setText(task.getTitle());
            String startEndHours = String.valueOf(task.getHour()) + ":" + String.valueOf(task.getMinute()) + " - " + String.valueOf(task.getEnd_hour()) + ":" + String.valueOf(task.getEnd_minute());
            ((TextView) itemView.findViewById(R.id.startEndTextView)).setText(startEndHours);

            if(task.getPriority() == Priority.HIGH)
                ((ImageView) itemView.findViewById(R.id.taskPictureIv)).setBackgroundColor(Color.RED);
            if(task.getPriority() == Priority.MID)
                ((ImageView) itemView.findViewById(R.id.taskPictureIv)).setBackgroundColor(Color.YELLOW);
            if(task.getPriority() == Priority.MINOR)
                ((ImageView) itemView.findViewById(R.id.taskPictureIv)).setBackgroundColor(Color.GREEN);
        }

        private void colorPastTasks(Task task){
            Calendar currTime = Calendar.getInstance();
            int hour = currTime.get(Calendar.HOUR_OF_DAY);

            if(task.getEnd_hour() < hour){
                itemView.setBackgroundColor(Color.rgb(83,84,84));
            }
        }
    }
}
