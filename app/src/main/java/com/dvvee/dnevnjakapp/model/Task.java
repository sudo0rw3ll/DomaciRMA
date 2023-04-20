package com.dvvee.dnevnjakapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Task implements Serializable {

    public static ArrayList<Task> taskArrayList = new ArrayList<>();

    private int id;
    private String title;
    private String description;
    private LocalDate date;
    private Priority priority;
    private int start_hour;
    private int start_minute;
    private int end_hour;
    private int end_minute;
    private int userID;

    public Task(int id,String title, String description, LocalDate date, int start_hour, int start_minute, int end_hour, int end_minute, Priority priority, int userID){
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.start_hour = start_hour;
        this.start_minute = start_minute;
        this.priority = priority;
        this.end_hour = end_hour;
        this.end_minute = end_minute;
        this.userID = userID;
    }

    public static Task getTaskForId(int taskId){
        for(Task task : taskArrayList){
            if(task.getId() == taskId)
                return task;
        }

        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getHour() {
        return start_hour;
    }

    public void setHour(int hour) {
        this.start_hour = hour;
    }

    public int getMinute() {
        return start_minute;
    }

    public void setMinute(int minute) {
        this.start_minute = minute;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public int getEnd_minute() {
        return end_minute;
    }

    public void setEnd_minute(int end_minute) {
        this.end_minute = end_minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
