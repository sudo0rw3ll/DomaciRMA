package com.dvvee.dnevnjakapp.recycler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.model.CalendarDay;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;

import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

public class CalendarAdapter extends ListAdapter<CalendarDay, CalendarAdapter.ViewHolder> {
    private final Consumer<CalendarDay> onTaskClicked;

    public CalendarAdapter(@NonNull DiffUtil.ItemCallback<CalendarDay> diffCallback, Consumer<CalendarDay> onTaskClicked){
        super(diffCallback);
        this.onTaskClicked = onTaskClicked;
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.1666666);
        return new CalendarAdapter.ViewHolder(view, parent.getContext(), position -> {
            CalendarDay calendarDay = getItem(position);
            onTaskClicked.accept(calendarDay);
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        CalendarDay calendarDay = getItem(position);
        holder.bind(calendarDay);

        Priority priority = getPriorityForCard(calendarDay);

        if(priority == Priority.HIGH)
            holder.itemView.setBackgroundColor(Color.RED);
        if(priority == Priority.MID)
            holder.itemView.setBackgroundColor(Color.YELLOW);
        if(priority == Priority.MINOR)
            holder.itemView.setBackgroundColor(Color.GREEN);

        System.out.println("Pos -> " + position + " task " + calendarDay.getDay());
    }

    private Priority getPriorityForCard(CalendarDay calendarDay){
        List<Task> dayTasks = calendarDay.getTasks();

        int minorTasks = 0;
        int midTasks = 0;
        int highTasks = 0;

        for(int i=0;i<dayTasks.size();i++){
            if(dayTasks.get(i).getPriority() == Priority.HIGH)
                highTasks++;
            if(dayTasks.get(i).getPriority() == Priority.MID)
                midTasks++;
            if(dayTasks.get(i).getPriority() == Priority.MINOR)
                minorTasks++;
        }

        int max = minorTasks;
        if(midTasks > max)
            max = midTasks;
        if(highTasks > max)
            max = highTasks;

        System.out.println("Max " + max);

        if(max == minorTasks)
            return Priority.MINOR;
        if(max == midTasks)
            return Priority.MID;

        return Priority.HIGH;
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

        public void bind(CalendarDay calendarDay){
            ((TextView) itemView.findViewById(R.id.taskText)).setText(calendarDay.getDay());
        }
    }
}
