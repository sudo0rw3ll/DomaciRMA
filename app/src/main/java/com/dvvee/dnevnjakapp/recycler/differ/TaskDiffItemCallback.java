package com.dvvee.dnevnjakapp.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dvvee.dnevnjakapp.model.Task;

public class TaskDiffItemCallback extends DiffUtil.ItemCallback<Task> {
    @Override
    public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.getTitle().equalsIgnoreCase(newItem.getTitle());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
        return oldItem.getDescription().equalsIgnoreCase(newItem.getDescription()) &&
                oldItem.getHour() == newItem.getHour() && oldItem.getMinute() == newItem.getMinute()
                && oldItem.getPriority().equals(newItem.getPriority());
    }
}
