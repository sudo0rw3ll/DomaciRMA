package com.dvvee.dnevnjakapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.dvvee.dnevnjakapp.model.Priority;
import com.dvvee.dnevnjakapp.model.Task;
import com.dvvee.dnevnjakapp.model.User;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "TASKS_DB";
    private static final int DATABASE_VERSION = 1;

    private static final String TASKS_TABLE = "tasks";
    private static final String COUNTER = "Counter";
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String PRIORITY_FIELD = "priority";
    private static final String startHour = "startHour";
    private static final String endHour = "endHour";
    private static final String startMinute = "startMinute";
    private static final String endMinute = "endMinute";
    private static final String date = "taskDate";
    private static final String userid = "userID";

    private static final String USERS_TABLE = "users";
    private static final String USER_ID_FIELD = "id";
    private static final String USER_USERNAME_FIELD = "username";
    private static final String USER_EMAIL_FIELD = "email";
    private static final String USER_PASSWORD_FIELD = "password";
    private static final String USER_IMAGE_LINK = "image";

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);
        return sqLiteManager;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable());
        db.execSQL(createTaskTable());
    }

    public void deleteUser(String email){
        try{
            String whereClause = "email = ?";
            String[] whereArgs = {email};

            SQLiteDatabase db = sqLiteManager.getWritableDatabase();
            db.delete(USERS_TABLE, whereClause, whereArgs);
            db.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    private String createTaskTable(){
        StringBuilder query;

        query = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TASKS_TABLE)
                .append(" ( ")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(DESCRIPTION_FIELD)
                .append(" TEXT, ")
                .append(PRIORITY_FIELD)
                .append(" INT, ")
                .append(startHour)
                .append(" INT, ")
                .append(endHour)
                .append(" INT, ")
                .append(startMinute)
                .append(" INT, ")
                .append(endMinute)
                .append(" INT, ")
                .append(date)
                .append(" TEXT, ")
                .append(userid)
                .append(" INT)");

        return query.toString();
    }

    private String createUserTable(){
        StringBuilder query;

        query = new StringBuilder()
                .append("CREATE TABLE ")
                .append(USERS_TABLE)
                .append(" ( ")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(USER_ID_FIELD)
                .append(" INT, ")
                .append(USER_USERNAME_FIELD)
                .append(" TEXT, ")
                .append(USER_EMAIL_FIELD)
                .append(" TEXT, ")
                .append(USER_PASSWORD_FIELD)
                .append(" TEXT, ")
                .append(USER_IMAGE_LINK)
                .append(" TEXT) ");

        return query.toString();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUser(User user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID_FIELD, user.getId());
        contentValues.put(USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(USER_PASSWORD_FIELD, user.getPassword());
        contentValues.put(USER_IMAGE_LINK, user.getImageLink());

        sqLiteDatabase.insert(USERS_TABLE, null, contentValues);
    }

    public void addTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, task.getId());
        contentValues.put(TITLE_FIELD, task.getTitle());
        contentValues.put(DESCRIPTION_FIELD, task.getDescription());
        contentValues.put(PRIORITY_FIELD, task.getPriority().ordinal());
        contentValues.put(startHour, task.getHour());
        contentValues.put(endHour, task.getEnd_hour());
        contentValues.put(startMinute, task.getMinute());
        contentValues.put(endMinute, task.getEnd_minute());
        contentValues.put(date, getStringFromDate(task.getDate()));
        contentValues.put(userid, task.getUserID());

        sqLiteDatabase.insert(TASKS_TABLE, null, contentValues);
    }

    public List<Task> getTaskList(){
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor result = db.rawQuery("SELECT * FROM " + TASKS_TABLE, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String title = result.getString(2);
                    String desc = result.getString(3);
                    Priority priority = Priority.values()[result.getInt(4)];
                    int startHour = result.getInt(5);
                    int endHour = result.getInt(6);
                    int startMinute = result.getInt(7);
                    int endMinute = result.getInt(8);
                    LocalDate date = this.getDateFromString(result.getString(9));
                    int userID= result.getInt(10);

                    tasks.add(new Task(id, title, desc, date, startHour, startMinute, endHour, endMinute, priority, userID));
                }
            }
        }

        return tasks;
    }

    public List<Task> getTasksForDate(LocalDate inDate){
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor result = db.rawQuery("SELECT * FROM " + TASKS_TABLE, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    LocalDate date = this.getDateFromString(result.getString(9));
                    if(date.equals(inDate)){
                        int id = result.getInt(1);
                        String title = result.getString(2);
                        String desc = result.getString(3);
                        Priority priority = Priority.values()[result.getInt(4)];
                        int startHour = result.getInt(5);
                        int endHour = result.getInt(6);
                        int startMinute = result.getInt(7);
                        int endMinute = result.getInt(8);
                        int userID= result.getInt(10);

                        tasks.add(new Task(id, title, desc, date, startHour, startMinute, endHour, endMinute, priority, userID));
                    }
                }
            }
        }

        return tasks;
    }

    public List<User> getUserList(){
        List<User> users = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        try(Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String username = result.getString(2);
                    String email = result.getString(3);
                    String password = result.getString(4);
                    String image = result.getString(5);

                    users.add(new User(id, email, username, password, image));
                }
            }
        }

        return users;
    }

    public int checkUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE email = ? AND password = ?", new String[]{email, password});

        int result = -1;

        System.out.println(cursor.getCount());

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            result = cursor.getInt(1);
            System.out.println("RES -> " + result);
        }

        cursor.close();
        db.close();
        return result;
    }

    public User getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE id = ?", new String[]{String.valueOf(id)});

        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            int uid = cursor.getInt(1);
            String username = cursor.getString(2);
            String email = cursor.getString(3);
            String password = cursor.getString(4);
            String image = cursor.getString(5);

            return new User(id, email, username, password, image);
        }

        return null;
    }

    public void updateTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, task.getId());
        contentValues.put(TITLE_FIELD, task.getTitle());
        contentValues.put(DESCRIPTION_FIELD, task.getDescription());
        contentValues.put(PRIORITY_FIELD, task.getPriority().ordinal());
        contentValues.put(startHour, task.getHour());
        contentValues.put(endHour, task.getEnd_hour());
        contentValues.put(startMinute, task.getMinute());
        contentValues.put(endMinute, task.getEnd_minute());
        contentValues.put(date, getStringFromDate(task.getDate()));

        sqLiteDatabase.update(TASKS_TABLE, contentValues, ID_FIELD + "=?",new String[]{String.valueOf(task.getId())});
    }

    public void updateUser(User user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID_FIELD, user.getId());
        contentValues.put(USER_USERNAME_FIELD, user.getUsername());
        contentValues.put(USER_EMAIL_FIELD, user.getEmail());
        contentValues.put(USER_PASSWORD_FIELD, user.getPassword());
        contentValues.put(USER_IMAGE_LINK, user.getImageLink());

        sqLiteDatabase.update(USERS_TABLE, contentValues, ID_FIELD + "=?", new String[]{String.valueOf(user.getId())});
    }

    private String getStringFromDate(LocalDate localDate){
        if(localDate == null)
            return null;
        return localDate.toString();
    }

    private LocalDate getDateFromString(String date){
        try{
            return LocalDate.parse(date);
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }
}
