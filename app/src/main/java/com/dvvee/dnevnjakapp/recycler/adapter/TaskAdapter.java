package com.dvvee.dnevnjakapp.recycler.adapter;

import android.content.Context;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666);
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
            ((TextView) itemView.findViewById(R.id.taskText)).setText(task.getTitle());
        }
    }
}
