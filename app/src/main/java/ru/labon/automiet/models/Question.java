package ru.labon.automiet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ru.labon.automiet.helpers.CursorDataConverter;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.TypeDataConverter;

/**
 * Created by Admin on 17.10.2017.
 */

public class Question extends DbModel {

    private static class TABLE_QUESTION {
        private static String TABLE_NAME = "question";
        private static String ID = "id";
        private static String TASK = "task";
        private static String VARS_COUNT = "vars_count";
        private static String RIGHT_ANS = "right_ans";
        private static String COMMENT = "comment";
        private static String ANS_V1 = "ans_v1";
        private static String ANS_V2 = "ans_v2";
        private static String ANS_V3 = "ans_v3";
        private static String ANS_V4 = "ans_v4";
        private static String ANS_V5 = "ans_v5";
        private static String IMAGE = "image";
        private static String THEME_NUM = "theme_num";
        private static String IN_THEME_NUM = "in_theme_num";
        private static String ERROR = "ERROR";
        private static String SUCCESS = "SUCCESS";
    }

    public static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_QUESTION.TABLE_NAME + " ("
                    + TABLE_QUESTION.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TABLE_QUESTION.TASK + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.VARS_COUNT + " INTEGER DEFAULT -1, "
                    + TABLE_QUESTION.RIGHT_ANS + " INTEGER DEFAULT -1, "
                    + TABLE_QUESTION.COMMENT + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.ANS_V1 + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.ANS_V2 + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.ANS_V3 + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.ANS_V4 + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.ANS_V5 + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.IMAGE + " TEXT DEFAULT \"\", "
                    + TABLE_QUESTION.THEME_NUM + " INTEGER DEFAULT -1, "
                    + TABLE_QUESTION.IN_THEME_NUM + " INTEGER DEFAULT -1, "
                    + TABLE_QUESTION.ERROR + " INTEGER DEFAULT -1, "
                    + TABLE_QUESTION.SUCCESS + " INTEGER DEFAULT -1 "
                    + ");";



    private int qid = -1;
    @SerializedName("task")
    private String task = "";
    @SerializedName("vars_count")
    private int varsCount = -1;
    @SerializedName("right_ans")
    private int rightAns = -1;
    @SerializedName("comment")
    private String comment = "";
    @SerializedName("ans_v1")
    private String ansV1 = "";
    @SerializedName("ans_v2")
    private String ansV2 = "";
    @SerializedName("ans_v3")
    private String ansV3 = "";
    @SerializedName("ans_v4")
    private String ansV4 = "";
    @SerializedName("ans_v5")
    private String ansV5 = "";
    @SerializedName("image")
    private String image = "";
    @SerializedName("theme_num")
    private int themeNum = -1;
    @SerializedName("in_theme_num")
    private int inThemeNum = -1;
    @SerializedName("ERROR")
    private int error = -1;
    @SerializedName("SUCCESS")
    private int success = -1;


    public static ArrayList<Question> getByTask(MainDbHelper dbHelper, int taskNumber) {


        ArrayList<Question> result = new ArrayList<>();
        Cursor c = dbHelper.getWritableDatabase().rawQuery(
                "SELECT q.* FROM task qt, question q" +
                        " WHERE qt.task_number =  " + taskNumber +
                        " AND qt.theme_number = q.theme_num" +
                        " AND qt.in_theme_number = q.in_theme_num" +
                        " ORDER BY qt.in_task_number"
                , null
        );

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Question quest = new Question();
            quest.setFromCursorRow(c);
            result.add(quest);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    public static ArrayList<Question> getByTheme(MainDbHelper dbHelper, int themeNum) {
        return getAll(dbHelper,
                TABLE_QUESTION.THEME_NUM + " = " + themeNum + " ORDER BY " + TABLE_QUESTION.IN_THEME_NUM);
    }


    public static ArrayList<Question> getForExam(MainDbHelper dbHelper) {
        int max = 200;
        int min = 1;
        Random random = new Random();
        ArrayList<Question> questions = new ArrayList<>();
        for (int j = 0; j < 4; j++) {
            final Set<Integer> intSet = new HashSet<>();
            while (intSet.size() < 10) {
                intSet.add(random.nextInt(max - min) + min);
            }

            String inClosure = StringUtils.join(intSet, ",");
            Log.d("HashSet", inClosure);
            questions.addAll(getAll(dbHelper, TABLE_QUESTION.ID + " IN (" + inClosure + ")"));
            min += 200;
            max += 200;
        }
        return questions;
    }


    public static ArrayList<Question> getAll(MainDbHelper dbHelper) {
        ArrayList<Question> result = new ArrayList<>();
        Cursor c = readAll(dbHelper);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Question quest = new Question();
            quest.setFromCursorRow(c);
            result.add(quest);
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public static ArrayList<Question> getAll(MainDbHelper dbHelper, String condition) {
        ArrayList<Question> result = new ArrayList<>();
        Cursor c = readAllByCondition(dbHelper, condition);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Question quest = new Question();
            quest.setFromCursorRow(c);
            result.add(quest);
            c.moveToNext();
        }
        c.close();
        return result;
    }


    @Override
    public boolean save(MainDbHelper dbHelper) {
        ContentValues cv = new ContentValues();
       // cv.put(TABLE_QUESTION.ID, this.id);
        cv.put(TABLE_QUESTION.TASK, this.task);
        cv.put(TABLE_QUESTION.VARS_COUNT, this.varsCount);
        cv.put(TABLE_QUESTION.RIGHT_ANS, this.rightAns);
        cv.put(TABLE_QUESTION.COMMENT, this.comment);
        cv.put(TABLE_QUESTION.ANS_V1, this.ansV1);
        cv.put(TABLE_QUESTION.ANS_V2, this.ansV2);
        cv.put(TABLE_QUESTION.ANS_V3, this.ansV3);
        cv.put(TABLE_QUESTION.ANS_V4, this.ansV4);
        cv.put(TABLE_QUESTION.ANS_V5, this.ansV5);
        cv.put(TABLE_QUESTION.IMAGE, this.image);
        cv.put(TABLE_QUESTION.THEME_NUM, this.themeNum);
        cv.put(TABLE_QUESTION.IN_THEME_NUM, this.inThemeNum);
        cv.put(TABLE_QUESTION.ERROR, this.error);
        if (this.getId() != -1) {
            cv.put(TABLE_QUESTION.ID, this.getId());
            if (isNewRecord()) {
                return dbHelper.getWritableDatabase().insert(TABLE_QUESTION.TABLE_NAME, null, cv) != -1;
            }
            return dbHelper.getWritableDatabase().update(TABLE_QUESTION.TABLE_NAME, cv, TABLE_QUESTION.ID + "=" + this.getId(), null) > 0;
        }
        Log.d("---save",""+this.getId());
        return dbHelper.getWritableDatabase().insert(TABLE_QUESTION.TABLE_NAME, null, cv) != -1;

    }

    public static Cursor readAll(MainDbHelper dbHelper) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_QUESTION.TABLE_NAME
                , null
        );
    }

    public static Cursor readAllByCondition(MainDbHelper dbHelper, String condition) {
        return dbHelper.getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_QUESTION.TABLE_NAME + " WHERE " + condition
                , null
        );
    }

    public Question setFromCursorRow(Cursor c) {
        return setFromSource(c, CursorDataConverter.getInstance());
    }

    public Question setFromSource(Object source, TypeDataConverter converter) {
        //this.id = converter.getInteger(source, TABLE_QUESTION.ID, this.getId());
        this.varsCount = converter.getInteger(source, TABLE_QUESTION.VARS_COUNT, this.getVarsCount());
        this.rightAns = converter.getInteger(source, TABLE_QUESTION.RIGHT_ANS, this.getRightAns());
        this.comment = converter.getString(source, TABLE_QUESTION.COMMENT, this.getComment());
        this.ansV1 = converter.getString(source, TABLE_QUESTION.ANS_V1, this.getAnsV1());
        this.ansV2 = converter.getString(source, TABLE_QUESTION.ANS_V2, this.getAnsV2());
        this.ansV3 = converter.getString(source, TABLE_QUESTION.ANS_V3, this.getAnsV3());
        this.ansV4 = converter.getString(source, TABLE_QUESTION.ANS_V4, this.getAnsV4());
        this.ansV5 = converter.getString(source, TABLE_QUESTION.ANS_V5, this.getAnsV5());
        this.image = converter.getString(source, TABLE_QUESTION.IMAGE, this.getImage());
        this.themeNum = converter.getInteger(source, TABLE_QUESTION.THEME_NUM, this.getThemeNum());
        this.inThemeNum = converter.getInteger(source, TABLE_QUESTION.IN_THEME_NUM, this.getInThemeNum());
        this.task = converter.getString(source, TABLE_QUESTION.TASK, this.getTask());
        this.error = converter.getInteger(source, TABLE_QUESTION.ERROR, this.getError());
        this.success = converter.getInteger(source, TABLE_QUESTION.SUCCESS, this.getSuccess());
        return this;
    }

    public static void recreateTable(MainDbHelper dbHelper) {
        recreateTable(dbHelper.getWritableDatabase());
    }

    public static void recreateTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION.TABLE_NAME);
        db.execSQL(CREATE_TABLE_QUERY);
    }


    public static void checkTableExist(MainDbHelper dbHelper) {
        Cursor cursor = dbHelper.getWritableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_QUESTION.TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                recreateTable(dbHelper);
            }
            cursor.close();
        }
    }

    public void setId(int id) {
        this.qid = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setVarsCount(int varsCount) {
        this.varsCount = varsCount;
    }

    public void setRightAns(int rightAns) {
        this.rightAns = rightAns;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAnsV1(String ansV1) {
        this.ansV1 = ansV1;
    }

    public void setAnsV2(String ansV2) {
        this.ansV2 = ansV2;
    }

    public void setAnsV3(String ansV3) {
        this.ansV3 = ansV3;
    }

    public void setAnsV4(String ansV4) {
        this.ansV4 = ansV4;
    }

    public void setAnsV5(String ansV5) {
        this.ansV5 = ansV5;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setThemeNum(int themeNum) {
        this.themeNum = themeNum;
    }

    public void setInThemeNum(int inThemeNum) {
        this.inThemeNum = inThemeNum;
    }

    public void setError(int error) {
        this.error = error;
    }

    public void setSuccess(int success) {
        this.success = success;
    }


    public int getId() {
        return qid;
    }

    public String getTask() {
        return task;
    }

    public int getVarsCount() {
        return varsCount;
    }

    public int getRightAns() {
        return rightAns;
    }

    public String getComment() {
        return comment;
    }

    public String getAnsV1() {
        return ansV1;
    }

    public String getAnsV2() {
        return ansV2;
    }

    public String getAnsV3() {
        return ansV3;
    }

    public String getAnsV4() {
        return ansV4;
    }

    public String getAnsV5() {
        return ansV5;
    }

    public String getImage() {
        return image;
    }

    public int getThemeNum() {
        return themeNum;
    }

    public int getInThemeNum() {
        return inThemeNum;
    }

    public int getError() {
        return error;
    }

    public int getSuccess() {
        return success;
    }
}
