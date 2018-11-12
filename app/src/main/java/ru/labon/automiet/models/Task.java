package ru.labon.automiet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ru.labon.automiet.helpers.CursorDataConverter;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.TypeDataConverter;

public class Task extends DbModel {

    private static class TABLE_TASK {
        private static String TABLE_NAME = "task";
        private static String TASK_ID = "task_id";
        private static String TASK_NUMBER = "task_number";
        private static String IN_TASK_NUMBER = "in_task_number";
        private static String THEME_NUMBER = "theme_number";
        private static String IN_THEME_NUMBER = "in_theme_number";

    }

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_TASK.TABLE_NAME + " ("
                    + TABLE_TASK.TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TABLE_TASK.TASK_NUMBER + " INTEGER DEFAULT -1, "
                    + TABLE_TASK.IN_TASK_NUMBER + " INTEGER DEFAULT -1, "
                    + TABLE_TASK.THEME_NUMBER + " INTEGER DEFAULT -1, "
                    + TABLE_TASK.IN_THEME_NUMBER + " INTEGER DEFAULT -1"
                    + ");";

    private int taskId = -1;

    @SerializedName("task_number")
    private int taskNumber = -1;

    @SerializedName("in_task_number")
    private int inTaskNumber = -1;

    @SerializedName("theme_number")
    private int themeNumber = -1;

    @SerializedName("in_theme_number")
    private int inThemeNumber = -1;



    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public void setInTaskNumber(int inTaskNumber) {
        this.inTaskNumber = inTaskNumber;
    }

    public void setThemeNumber(int themeNumber) {
        this.themeNumber = themeNumber;
    }

    public void setInThemeNumber(int inThemeNumber) {
        this.inThemeNumber = inThemeNumber;
    }

    public int getInThemeNumber() {
        return inThemeNumber;
    }
    public int getTaskId() {
        return taskId;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public int getInTaskNumber() {
        return inTaskNumber;
    }

    public int getThemeNumber() {
        return themeNumber;
    }




    @Override
    public boolean save(MainDbHelper dbHelper) {

        ContentValues cv = new ContentValues();
        cv.put(TABLE_TASK.TASK_NUMBER, this.getTaskNumber());
        cv.put(TABLE_TASK.IN_TASK_NUMBER, this.getInTaskNumber());
        cv.put(TABLE_TASK.THEME_NUMBER, this.getThemeNumber());
        cv.put(TABLE_TASK.IN_THEME_NUMBER, this.getInThemeNumber());
        if (this.getTaskId() != -1) {
            cv.put(TABLE_TASK.TASK_ID, this.getTaskId());
            if (isNewRecord()) {
                return dbHelper.getWritableDatabase().insert(TABLE_TASK.TABLE_NAME, null, cv) != -1;
            }
            return dbHelper.getWritableDatabase().update(TABLE_TASK.TABLE_NAME, cv, TABLE_TASK.TASK_ID + "=" + this.getTaskId(), null) > 0;
        }
        return dbHelper.getWritableDatabase().insert(TABLE_TASK.TABLE_NAME, null, cv) != -1;
    }


    public static void deleteAll(MainDbHelper dbHelper) {
        dbHelper.getWritableDatabase().execSQL("DELETE FROM " + TABLE_TASK.TABLE_NAME);
    }

    public static Task getById(MainDbHelper dbHelper, int id) {
        Task task = new Task();
        Cursor c = readById(dbHelper, id);
        c.moveToFirst();
        if (c.getCount() > 0) {
            task.setFromCursorRow(c);
            c.close();
            return task;
        }
        c.close();
        return null;
    }

    public static ArrayList<Task> getAll(MainDbHelper dbHelper) {
        ArrayList<Task> result = new ArrayList<>();
        Cursor c = readAll(dbHelper);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Task task = new Task();
            task.setFromCursorRow(c);
            result.add(task);
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public static ArrayList<Task> getNames(MainDbHelper dbHelper){
        ArrayList<Task> result = new ArrayList<>();
        Cursor c = readAllByCondition(dbHelper, "1=1 GROUP BY task_number");
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Task task = new Task();
            task.setFromCursorRow(c);
            result.add(task);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    public static ArrayList<Task> getAll(MainDbHelper dbHelper, String condition) {
        ArrayList<Task> result = new ArrayList<>();
        Cursor c = readAllByCondition(dbHelper, condition);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Task task = new Task();
            task.setFromCursorRow(c);
            result.add(task);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    /**
     * @return Курсор выборки ВСЕХ сущностей
     */
    public static Cursor readAll(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_TASK.TABLE_NAME
                , null
        );
    }

    public static Cursor readAllByCondition(MainDbHelper dbHelper, String condition) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_TASK.TABLE_NAME + " WHERE " + condition
                , null
        );
    }

    public static Cursor readIds(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT " + TABLE_TASK.TASK_ID + " FROM " + TABLE_TASK.TABLE_NAME
                , null
        );
    }

    /**
     * @param id id сущности
     * @return Курсор выборки сущности по id
     */
    public static Cursor readById(MainDbHelper dbHelper, int id) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_TASK.TABLE_NAME
                        + " WHERE " + TABLE_TASK.TASK_ID + "=" + id
                , null
        );
    }


    public Task setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public Task setFromSource(Object source, TypeDataConverter converter) {
        this.taskNumber = converter.getInteger(source, TABLE_TASK.TASK_NUMBER, this.getTaskNumber());
        this.inTaskNumber = converter.getInteger(source, TABLE_TASK.IN_TASK_NUMBER, this.getInTaskNumber());
        this.themeNumber = converter.getInteger(source, TABLE_TASK.THEME_NUMBER, this.getThemeNumber());
        this.inThemeNumber = converter.getInteger(source, TABLE_TASK.IN_THEME_NUMBER, this.getInThemeNumber());
        return this;
    }

    public static void recreateTable(MainDbHelper dbHelper) {
        recreateTable(dbHelper.getWritableDatabase());
    }

    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK.TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }

    public static void checkTableExist(MainDbHelper dbHelper) {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_TASK.TABLE_NAME+"'", null);
        if(cursor!=null) {
            if(cursor.getCount() == 0) {
                recreateTable(dbHelper);
            }
            cursor.close();
        }
    }
}



