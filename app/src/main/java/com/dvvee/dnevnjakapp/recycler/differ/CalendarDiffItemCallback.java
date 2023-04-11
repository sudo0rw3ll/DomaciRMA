package com.dvvee.dnevnjakapp.recycler.differ;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dvvee.dnevnjakapp.model.CalendarDay;
import com.dvvee.dnevnjakapp.model.Task;

public class CalendarDiffItemCallback extends DiffUtil.ItemCallback<CalendarDay> {
    @Override
    public boolean areItemsTheSame(@NonNull CalendarDay oldItem, @NonNull CalendarDay newItem) {
        return oldItem.getDate().equals(newItem.getDate());
    }

    @Override
    public boolean areContentsTheSame(@NonNull CalendarDay oldItem, @NonNull CalendarDay newItem) {
        return oldItem.getDay().equalsIgnoreCase(newItem.getDay()) && oldItem.getTasks().equals(newItem.getTasks());
    }
}
