package ru.labon.automiet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ru.labon.automiet.helpers.CursorDataConverter;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.TypeDataConverter;

/**
 * Created by HP on 06.03.2018.
 */

public class UserAnswer extends DbModel {

    private static class TABLE_ANSWER {
        private static String TABLE_NAME = "user_answer";
        private static String ID = "id";
        private static String THEME_NUM = "theme_num";
        private static String IN_THEME_NUM = "in_theme_num";
        private static String SUCCESS_COUNT = "success_count";
        private static String ERROR_COUNT = "error_count";
    }


    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_ANSWER.TABLE_NAME + " ("
                    + TABLE_ANSWER.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TABLE_ANSWER.THEME_NUM + " INTEGER DEFAULT -1, "
                    + TABLE_ANSWER.IN_THEME_NUM + " INTEGER DEFAULT -1, "
                    + TABLE_ANSWER.SUCCESS_COUNT + " INTEGER DEFAULT -1, "
                    + TABLE_ANSWER.ERROR_COUNT + " INTEGER DEFAULT -1 "
                    + ");";


    public int id = -1;
    public int theme_num = -1;
    public int in_theme_num = -1;
    public int success_count = -1;
    public int error_count = -1;


    @Override
    public boolean save(MainDbHelper dbHelper) {
        ContentValues cv = new ContentValues();
        cv.put(TABLE_ANSWER.THEME_NUM, this.theme_num);
        cv.put(TABLE_ANSWER.IN_THEME_NUM, this.in_theme_num);
        cv.put(TABLE_ANSWER.SUCCESS_COUNT, this.success_count);
        cv.put(TABLE_ANSWER.ERROR_COUNT, this.error_count);
        if (this.id != -1) {
            cv.put(TABLE_ANSWER.ID, this.id);
            return dbHelper.getWritableDatabase().update(TABLE_ANSWER.TABLE_NAME, cv, TABLE_ANSWER.ID + "=" + this.id, null) > 0;
        }
        return dbHelper.getWritableDatabase().insert(TABLE_ANSWER.TABLE_NAME, null, cv) != -1;

    }




    public static ArrayList<UserAnswer> getAll(MainDbHelper dbHelper) {
        ArrayList<UserAnswer> result = new ArrayList<>();
        Cursor c = readAll(dbHelper);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setFromCursorRow(c);
            result.add(userAnswer);
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public static UserAnswer getByCondition(MainDbHelper dbHelper, String condition) {
        Cursor c = readByCondition(dbHelper, condition);

        c.moveToFirst();
        if( c.getCount() > 0 ) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setFromCursorRow(c);
            return userAnswer;
        }
        return null;
    }

    public static Cursor readAll(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + UserAnswer.TABLE_ANSWER.TABLE_NAME
                , null
        );
    }

    public static Cursor readByCondition(MainDbHelper dbHelper, String condition) {
        Log.d("DB_QUERY","SELECT * FROM " + TABLE_ANSWER.TABLE_NAME + " WHERE " + condition);
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_ANSWER.TABLE_NAME + " WHERE " + condition
                , null
        );
    }

    public UserAnswer setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public UserAnswer setFromSource(Object source, TypeDataConverter converter) {
        this.id = converter.getInteger(source, TABLE_ANSWER.ID, this.id);
        this.theme_num = converter.getInteger(source, TABLE_ANSWER.THEME_NUM, this.theme_num);
        this.in_theme_num = converter.getInteger(source, TABLE_ANSWER.IN_THEME_NUM, this.in_theme_num);
        this.success_count = converter.getInteger(source, TABLE_ANSWER.SUCCESS_COUNT, this.success_count);
        this.error_count = converter.getInteger(source, TABLE_ANSWER.ERROR_COUNT, this.error_count);
        return this;
    }

    public static void deleteAll(MainDbHelper dbHelper) {
        dbHelper.getWritableDatabase().execSQL(
                "DELETE FROM " + UserAnswer.TABLE_ANSWER.TABLE_NAME
        );
    }

    public static void recreateTable(MainDbHelper dbHelper) {
        recreateTable(dbHelper.getWritableDatabase());
    }

    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + UserAnswer.TABLE_ANSWER.TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }

    public static void checkTableExist(MainDbHelper dbHelper) {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ TABLE_ANSWER.TABLE_NAME+"'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                recreateTable(dbHelper);
            }
            cursor.close();
        }
    }


}
