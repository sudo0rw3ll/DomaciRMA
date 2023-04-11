package com.dvvee.dnevnjakapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarDay {

    private LocalDate date;
    private String day;
    private List<Task> tasks;
    private Priority priority;

    public CalendarDay(){

    }

    public CalendarDay(LocalDate date){
        this.date = date;
        this.day = String.valueOf(date.getDayOfMonth());
        this.tasks = new ArrayList<>();
        this.priority = Priority.HIGH;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
