package com.dvvee.dnevnjakapp.recycler.adapter;

import android.content.Context;
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
import com.dvvee.dnevnjakapp.model.Task;

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

        System.out.println("Pos -> " + position + " task " + calendarDay.getDay());
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
