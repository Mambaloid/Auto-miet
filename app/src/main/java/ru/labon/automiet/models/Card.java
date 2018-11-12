package ru.labon.automiet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.labon.automiet.helpers.CursorDataConverter;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.TypeDataConverter;

/**
 * Created by Admin on 10.10.2017.
 */

public class Card extends DbModel{


    private static final class TABLE_CARD {
        private static String TABLE_NAME = "card";
        private static String CARD_ID = "card_id";
        private static String PK = "id";
        private static String THEME_ID = "theme";
        private static String THEME_SUBTOPIC = "subtopic";
        private static String CARD_ITEM = "item";
        private static String CARD_IMG = "img";
        private static String CARD_HEADING = "heading";
        private static String CARD_DESCRIPTION = "description";
    }

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_CARD.TABLE_NAME + " ("
                    + TABLE_CARD.PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TABLE_CARD.CARD_ID + " INTEGER DEFAULT -1, "
                    + TABLE_CARD.THEME_ID + " INTEGER DEFAULT -1, "
                    + TABLE_CARD.THEME_SUBTOPIC + " TEXT DEFAULT \"\", "
                    + TABLE_CARD.CARD_ITEM + " INTEGER DEFAULT -1, "
                    + TABLE_CARD.CARD_IMG + " TEXT DEFAULT \"\", "
                    + TABLE_CARD.CARD_HEADING + " TEXT DEFAULT \"\", "
                    + TABLE_CARD.CARD_DESCRIPTION + " TEXT DEFAULT \"\" "
                    +");";

    private int pk = -1;

    @SerializedName("id")
    private int id = -1;

    @SerializedName("theme")
    private int themeId = -1;

    @SerializedName("subtopic")
    private String subtopic = null;

    @SerializedName("item")
    private int item = -1;

    @SerializedName("img")
    private String img = "";

    @SerializedName("heading")
    private String heading = "";

    @SerializedName("description")
    private String description = "";

    public int getId() {
        return id;
    }

    public int getThemeId() {
        return themeId;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public String getImgName(){
        String imgName = themeId + "/";
        if (subtopic == null || subtopic.equals("null") || subtopic.isEmpty()){
            return imgName + themeId + "." + item + ".jpg";
        }
        return imgName + themeId + "." + subtopic + "." + item + ".jpg";
    }

    public int getItem() {
        return item;
    }

    public String getImg() {
        return img;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }


    public Card setId(int id) {
        this.id = id;
        return this;
    }

    public Card setThemeId(int themeId) {
        this.themeId = themeId;
        return this;
    }

    public Card setSubtopic(String subtopic) {
        this.subtopic = subtopic;
        return this;
    }

    public Card setItem(int item) {
        this.item = item;
        return this;
    }

    public Card setImg(String img) {
        this.img = img;
        return this;
    }

    public Card setHeading(String heading) {
        this.heading = heading;
        return this;
    }

    public Card setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean save(MainDbHelper dbHelper) {

        ContentValues cv = new ContentValues();
        cv.put(TABLE_CARD.CARD_ID, this.getId());
        cv.put(TABLE_CARD.THEME_ID, this.getThemeId());
        cv.put(TABLE_CARD.CARD_ITEM, this.getItem());
        cv.put(TABLE_CARD.CARD_IMG, this.getImg());
        cv.put(TABLE_CARD.CARD_HEADING, this.getHeading());
        cv.put(TABLE_CARD.THEME_SUBTOPIC, this.getSubtopic());
        cv.put(TABLE_CARD.CARD_DESCRIPTION, this.getDescription());
        return dbHelper.getWritableDatabase().insert(TABLE_CARD.TABLE_NAME, null, cv)!= -1;
    }
    public static void recreateTable(MainDbHelper dbHelper) {
        recreateTable(dbHelper.getWritableDatabase());
    }

    public Card setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public Card setFromSource(Object source, TypeDataConverter converter) {
        this.id = converter.getInteger(source, TABLE_CARD.CARD_ID, this.getId());
        this.themeId = converter.getInteger(source, TABLE_CARD.THEME_ID, this.getThemeId());
        this.subtopic = converter.getString(source, TABLE_CARD.THEME_SUBTOPIC, this.getSubtopic());
        this.item = converter.getInteger(source, TABLE_CARD.CARD_ITEM, this.getItem());
        this.img = converter.getString(source, TABLE_CARD.CARD_IMG, this.getImg());
        this.heading = converter.getString(source, TABLE_CARD.CARD_HEADING, this.getHeading());
        this.description = converter.getString(source, TABLE_CARD.CARD_DESCRIPTION, this.getDescription());
        return this;
    }


    public static ArrayList<Card> getAll(MainDbHelper dbHelper, String condition) {
        ArrayList<Card> result = new ArrayList<>();
        Cursor c = readAllByCondition(dbHelper, condition);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Card card = new Card();
            card.setFromCursorRow(c);
            result.add(card);
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
                "SELECT * FROM " + TABLE_CARD.TABLE_NAME
                , null
        );
    }
    public static Cursor readAllByCondition(MainDbHelper dbHelper, String condition) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_CARD.TABLE_NAME + " WHERE " + condition
                , null
        );
    }

    public static List<Card> getByTheme(MainDbHelper dbHelper, int themeId) {
        List<Card> list = getAll(dbHelper, TABLE_CARD.THEME_ID + "=" + themeId);

        Collections.sort(list, new Comparator<Card>() {
            public int compare(Card card1, Card card2) {
                return card1.getOrderNum().compareTo(card2.getOrderNum());
            }
        });

        return list;

    }

    private Integer getOrderNum() {
        String[] result = this.heading.split("#");
        try {
            return Integer.parseInt(result[0]);
        } catch (Exception e) {
            return 999999;
        }
    }


    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD.TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }


    public static void checkTableExist(MainDbHelper dbHelper) {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_CARD.TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                recreateTable(dbHelper);
            }
            cursor.close();
        }
    }
    public static boolean columnsExist(MainDbHelper dbHelper){

        return FullHelper.isColumnExists(dbHelper, TABLE_CARD.TABLE_NAME, TABLE_CARD.THEME_SUBTOPIC);
    }


}
