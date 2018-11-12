package ru.labon.automiet.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.helpers.CursorDataConverter;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.TypeDataConverter;

/**
 * Created by Admin on 03.10.2017.
 */

public class ThemeAb extends DbModel {

    private static class TABLE_THEME {
        private static String TABLE_NAME = "themes_ab";
        private static String THEME_ID = "theme_id";
        private static String THEME_NUM = "theme_num";
        private static String THEME_NAME = "name";
        private static String THEME_QUEST_COUNT = "quest_count";
        private static String THEME_STATE = "theme_state";

    }

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_THEME.TABLE_NAME + " ("
                    + TABLE_THEME.THEME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TABLE_THEME.THEME_NUM + " INTEGER DEFAULT -1, "
                    + TABLE_THEME.THEME_NAME + " TEXT DEFAULT \"\", "
                    + TABLE_THEME.THEME_QUEST_COUNT + " INTEGER DEFAULT -1,"
                    + TABLE_THEME.THEME_STATE + " INTEGER DEFAULT 0"
                    + ");";

    private int id = -1;

    @SerializedName("theme_num")
    private int num = -1;

    @SerializedName("name")
    private String name = "";

    @SerializedName("quest_count")
    private int questCount = -1;

    private int themeState = 0;


    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public int getQuestCount() {
        return questCount;
    }

    public int getThemeState() {
        return themeState;
    }

    public ThemeAb setThemeState(int themeState) {
        this.themeState = themeState;
        return this;
    }

    public ThemeAb setId(int id) {
        this.id = id;
        return this;
    }

    public ThemeAb setNum(int num) {
        this.num = num;
        return this;
    }

    public ThemeAb setName(String name) {
        this.name = name;
        return this;
    }

    public ThemeAb setQuestCount(int questCount) {
        this.questCount = questCount;
        return this;
    }

    @Override
    public boolean save(MainDbHelper dbHelper) {

        ContentValues cv = new ContentValues();
        cv.put(TABLE_THEME.THEME_NUM, this.getNum());
        cv.put(TABLE_THEME.THEME_NAME, this.getName());
        cv.put(TABLE_THEME.THEME_QUEST_COUNT, this.getQuestCount());
        cv.put(TABLE_THEME.THEME_STATE, this.getThemeState());
        if (this.id != -1) {
            cv.put(TABLE_THEME.THEME_ID, this.id);
            return dbHelper.getWritableDatabase().update(TABLE_THEME.TABLE_NAME, cv, TABLE_THEME.THEME_ID + "=" + this.getId(), null) > 0;
        }
        return dbHelper.getWritableDatabase().insert(TABLE_THEME.TABLE_NAME, null, cv) != -1;
    }

    public static void deleteAll(MainDbHelper dbHelper) {
        dbHelper.getWritableDatabase().execSQL("DELETE FROM " + TABLE_THEME.TABLE_NAME);
    }

    public static ThemeAb getById(MainDbHelper dbHelper, int id) {
        ThemeAb themeAb = new ThemeAb();
        Cursor c = readById(dbHelper, id);
        c.moveToFirst();
        if (c.getCount() > 0) {
            themeAb.setFromCursorRow(c);
            c.close();
            return themeAb;
        }
        c.close();
        return null;
    }

    public static ArrayList<ThemeAb> getAll(MainDbHelper dbHelper) {
        ArrayList<ThemeAb> result = new ArrayList<>();
        Cursor c = readAll(dbHelper);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            ThemeAb themeAb = new ThemeAb();
            themeAb.setFromCursorRow(c);
            result.add(themeAb);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    public static String[] getNames(MainDbHelper dbHelper) {
        ArrayList<ThemeAb> themes = getAll(dbHelper);
        String[] names = new String[themes.size()];

        for (int i = 0; i < themes.size(); i++) {
            names[i] = themes.get(i).getName();
        }
        return names;
    }


    public static ArrayList<ThemeAb> getAll(MainDbHelper dbHelper, String condition) {
        ArrayList<ThemeAb> result = new ArrayList<>();
        Cursor c = readAllByCondition(dbHelper, condition);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            ThemeAb themeAb = new ThemeAb();
            themeAb.setFromCursorRow(c);
            result.add(themeAb);
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
                "SELECT * FROM " + TABLE_THEME.TABLE_NAME
                , null
        );
    }

    public static Cursor readAllByCondition(MainDbHelper dbHelper, String condition) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_THEME.TABLE_NAME + " WHERE " + condition
                , null
        );
    }

    public static Cursor readIds(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT " + TABLE_THEME.THEME_ID + " FROM " + TABLE_THEME.TABLE_NAME
                , null
        );
    }

    /**
     * @param id id сущности
     * @return Курсор выборки сущности по id
     */
    public static Cursor readById(MainDbHelper dbHelper, int id) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_THEME.TABLE_NAME
                        + " WHERE " + TABLE_THEME.THEME_ID + "=" + id
                , null
        );
    }


    public ThemeAb setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public ThemeAb setFromSource(Object source, TypeDataConverter converter) {
        this.id = converter.getInteger(source, TABLE_THEME.THEME_ID, this.getId());
        this.num = converter.getInteger(source, TABLE_THEME.THEME_NUM, this.getNum());
        this.name = converter.getString(source, TABLE_THEME.THEME_NAME, this.getName());
        this.questCount = converter.getInteger(source, TABLE_THEME.THEME_QUEST_COUNT, this.getQuestCount());
        this.themeState = converter.getInteger(source, TABLE_THEME.THEME_STATE, this.getThemeState());
        return this;
    }

    public static void recreateTable(MainDbHelper dbHelper) {
        recreateTable(dbHelper.getWritableDatabase());
    }

    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEME.TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }


    public static void checkTableExist(MainDbHelper dbHelper) {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_THEME.TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                recreateTable(dbHelper);
            }
            cursor.close();
        }
    }

    public static boolean columnsExist(MainDbHelper dbHelper){

        return FullHelper.isColumnExists(dbHelper, TABLE_THEME.TABLE_NAME, TABLE_THEME.THEME_STATE);
    }

}
