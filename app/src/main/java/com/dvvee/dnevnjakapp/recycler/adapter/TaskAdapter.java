package com.dvvee.dnevnjakapp.recycler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;

import java.util.function.Consumer;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.ViewHolder> {

    private final Consumer<Task> onTaskClicked;

    public TaskAdapter(@NonNull DiffUtil.ItemCallback<Task> diffCallback, Consumer<Task> onTaskClicked){
        super(diffCallback);
        this.onTaskClicked = onTaskClicked;
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
    }
}
