package com.dvvee.dnevnjakapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dvvee.dnevnjakapp.activities.DetailsActivity;
import com.dvvee.dnevnjakapp.db.SQLiteManager;
import com.dvvee.dnevnjakapp.model.CalendarDay;
import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SharedViewModel extends ViewModel {

    /**
     * MutableLiveData moze da se menja, live data jer sadrzaj moze da se menja
     * u toku izvrsavanja
     * */

    private final MutableLiveData<String> month = new MutableLiveData<>(); // wrapper za string
    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private final MutableLiveData<List<CalendarDay>> calendarDays = new MutableLiveData<>();
    private final MutableLiveData<CalendarDay> dayToShow = new MutableLiveData<>();
    private final MutableLiveData<Task> task = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();

    private LocalDate selectedDate;

    public SharedViewModel(){
        this.selectedDate = LocalDate.now();
        initVals();
    }

    private void initVals(){
//        ArrayList<CalendarDay> listCalendar = new ArrayList<>(generateCalendar(LocalDate.now()));
        ArrayList<CalendarDay> list = new ArrayList<>();
        list.addAll(getDaysToFillFirst(LocalDate.now()));
        list.addAll(getDaysForMonth(LocalDate.now()));
        list.addAll(getDaysToFillLast(LocalDate.now()));
        this.month.setValue(YearMonth.from(selectedDate).toString());
        ArrayList<CalendarDay> listCalendar = list;
        this.calendarDays.setValue(listCalendar);
    }

    public List<CalendarDay> getDaysForMonth(LocalDate currMonth){
        List<CalendarDay> dates = new ArrayList<CalendarDay>();

        LocalDate firstOfMonth = currMonth.with(TemporalAdjusters.firstDayOfMonth());

        for(int i=0;i<currMonth.lengthOfMonth();i++){
            List<Task> tasks = new ArrayList<>();
            CalendarDay calendarDay = new CalendarDay();

            LocalDate localDate = firstOfMonth.plusDays(i);
            String date = String.valueOf(localDate.getDayOfMonth());
            Priority priority = Priority.MID;

            if(i % 5 == 0) {
                Task task = new Task(i + localDate.getMonthValue() + localDate.getDayOfMonth(), "task " + i, "description of task " + i, localDate, 20, 20, 21, 0, Priority.MID, 1);
                tasks.add(task);
            }
//            List<Task> res = sqLiteManager.getTasksForDate(localDate);

            calendarDay.setDate(localDate);
            calendarDay.setDay(date);
            calendarDay.setPriority(priority);
            calendarDay.setTasks(tasks);

            dates.add(calendarDay);
        }

        // System.out.println(dates);

        return dates;
    }

    public List<CalendarDay> getDaysForPastMonth(LocalDate lastVisible){
        List<CalendarDay> dates = new ArrayList<CalendarDay>();

        LocalDate firstOfMonth = lastVisible.with(TemporalAdjusters.firstDayOfMonth());
        int diff = lastVisible.getDayOfMonth() - firstOfMonth.getDayOfMonth();

        dates.addAll(getDaysToFillFirst(firstOfMonth));

        for(int i=0;i<diff;i++){
            // System.out.println(firstOfMonth.plusDays(i));
            dates.add(new CalendarDay(firstOfMonth.plusDays(i)));
        }

        return dates;
    }

    public List<CalendarDay> getDaysToFillFirst(LocalDate currMonth){
        List<CalendarDay> datesFromPast = new ArrayList<CalendarDay>();

        LocalDate firstDayOfMonth = currMonth.with(TemporalAdjusters.firstDayOfMonth());
        DayOfWeek dayOfWeek = DayOfWeek.from(firstDayOfMonth);
        int val = dayOfWeek.getValue() - 1;

        LocalDate pastMonth = currMonth.plusMonths(-1);

        LocalDate lastDayOfPastMonth = pastMonth.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate startFromPast = lastDayOfPastMonth.plusDays(-val);

        for(int i=1;i<=val;i++){
            System.out.println(startFromPast.plusDays(i));
            datesFromPast.add(new CalendarDay(startFromPast.plusDays(i)));
        }

        return datesFromPast;
    }

    public List<CalendarDay> getDaysToFillLast(LocalDate currMonth){
        List<CalendarDay> datesFromNext = new ArrayList<CalendarDay>();

        System.out.println("usao");

        LocalDate lastDayOfCurrentMonth = currMonth.with(TemporalAdjusters.lastDayOfMonth());
        DayOfWeek dayOfWeek = DayOfWeek.from(lastDayOfCurrentMonth);

        System.out.println(dayOfWeek.getValue());

        int val = 7 - dayOfWeek.getValue();

        LocalDate nextMonth = currMonth.plusMonths(1);

        LocalDate firstDayOfNextMonth = nextMonth.with(TemporalAdjusters.firstDayOfMonth());

        for(int i=0;i<val;i++){
            System.out.println("Last " + firstDayOfNextMonth.plusDays(i)+"\n");
            datesFromNext.add(new CalendarDay(firstDayOfNextMonth.plusDays(i)));
        }

        return datesFromNext;
    }

    public void updateList(){
        ArrayList<CalendarDay> newList = new ArrayList<>();
        ArrayList<CalendarDay> curr = (ArrayList<CalendarDay>) this.calendarDays.getValue();
        newList.addAll(curr);
        newList.addAll(getDaysForMonth(this.selectedDate.plusMonths(1)));
        this.calendarDays.setValue(newList);
    }

    public void updatePastList(){
        System.out.println("Usao sam ovde");
        ArrayList<CalendarDay> newList = new ArrayList<>();
        System.out.println("Ovaj datum -> " + this.calendarDays.getValue().get(0).getDate());
        newList.addAll(getDaysForPastMonth(this.calendarDays.getValue().get(0).getDate()));
        ArrayList<CalendarDay> curr = (ArrayList<CalendarDay>) this.calendarDays.getValue();
        newList.addAll(curr);
        this.calendarDays.setValue(newList);
    }

    public void updateDown(){
        this.selectedDate = this.selectedDate.plusMonths(1);
        this.month.setValue(YearMonth.from(selectedDate).toString());
        updateList();
    }

    public void updateUp(){
        this.selectedDate = this.selectedDate.minusMonths(1);
        this.month.setValue(YearMonth.from(selectedDate).toString());
        updatePastList();
    }

    public void updateDayToShow(CalendarDay calendarDay){
        this.getDayToShow().setValue(calendarDay);
    }

    public void deleteTask(Task task){
        for(int i=0;i<this.calendarDays.getValue().size();i++){
            if(this.calendarDays.getValue().get(i).getDate().equals(task.getDate())){
                this.calendarDays.getValue().get(i).getTasks().remove(task);
            }
        }

        if(this.dayToShow != null){
            dayToShow.getValue().getTasks().remove(task);
        }
    }

    public void deleteTaskForCurr(Task task){
        this.dayToShow.getValue().getTasks().remove(task);
    }

    public MutableLiveData<List<CalendarDay>> getCalendarDays() {
        return calendarDays;
    }

    public MutableLiveData<List<Task>> getTasks() {
        return tasks;
    }

    public MutableLiveData<String> getMonth() {
        return month;
    }

    public MutableLiveData<CalendarDay> getDayToShow() {
        return dayToShow;
    }

    public MutableLiveData<Task> getTask() {
        return task;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }
}
