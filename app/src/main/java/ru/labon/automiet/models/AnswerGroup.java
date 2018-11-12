package ru.labon.automiet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.labon.automiet.helpers.CursorDataConverter;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.TypeDataConverter;

/**
 * Created by Admin on 20.02.2018.
 */

public class AnswerGroup extends DbModel {

    private static class TABLE_ANSWER {
        private static String TABLE_NAME = "answer_group";
        private static String ID = "id";
        private static String THEME_NUM = "theme_num";
        private static String RIGHT_ANSWER_COUNT = "right_answer_count";
        private static String DATE = "date";
    }


    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_ANSWER.TABLE_NAME + " ("
                    + TABLE_ANSWER.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TABLE_ANSWER.THEME_NUM + " INTEGER DEFAULT -1, "
                    + TABLE_ANSWER.RIGHT_ANSWER_COUNT + " INTEGER DEFAULT -1, "
                    + TABLE_ANSWER.DATE + " INTEGER DEFAULT -1 "
                    + ");";


    public int id = -1;
    public int themeNum = -1;
    public int rightAnswerCount = -1;
    public int date = -1;


    @Override
    public boolean save(MainDbHelper dbHelper) {
        ContentValues cv = new ContentValues();
        cv.put(TABLE_ANSWER.THEME_NUM, this.themeNum);
        cv.put(TABLE_ANSWER.RIGHT_ANSWER_COUNT, this.rightAnswerCount);
        cv.put(TABLE_ANSWER.DATE, this.date);
        return dbHelper.getWritableDatabase().insert(TABLE_ANSWER.TABLE_NAME, null, cv) != -1;
    }



    public static ArrayList<AnswerGroup> getAll(MainDbHelper dbHelper) {
        ArrayList<AnswerGroup> result = new ArrayList<>();
        Cursor c = readAll(dbHelper);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            AnswerGroup answerGroup = new AnswerGroup();
            answerGroup.setFromCursorRow(c);
            result.add(answerGroup);
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public static Cursor readAll(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + AnswerGroup.TABLE_ANSWER.TABLE_NAME
                , null
        );
    }

    public AnswerGroup setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public AnswerGroup setFromSource(Object source, TypeDataConverter converter) {
        this.id = converter.getInteger(source, TABLE_ANSWER.ID, this.id);
        this.themeNum = converter.getInteger(source, TABLE_ANSWER.THEME_NUM, this.themeNum);
        this.rightAnswerCount = converter.getInteger(source, TABLE_ANSWER.RIGHT_ANSWER_COUNT, this.rightAnswerCount);
        this.date = converter.getInteger(source, TABLE_ANSWER.DATE, this.date);
        return this;
    }

    public static void deleteAll(MainDbHelper dbHelper) {
        dbHelper.getWritableDatabase().execSQL(
                "DELETE FROM " + AnswerGroup.TABLE_ANSWER.TABLE_NAME
        );
    }

    public static void recreateTable(MainDbHelper dbHelper) {
        recreateTable(dbHelper.getWritableDatabase());
    }

    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + AnswerGroup.TABLE_ANSWER.TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }


    public static void checkTableExist(MainDbHelper dbHelper) {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_ANSWER.TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                recreateTable(dbHelper);
            }
            cursor.close();
        }
    }


}
