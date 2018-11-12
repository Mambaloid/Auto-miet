package ru.labon.automiet.helpers;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.labon.automiet.models.AnswerGroup;
import ru.labon.automiet.models.Card;
import ru.labon.automiet.models.Question;
import ru.labon.automiet.models.Task;
import ru.labon.automiet.models.ThemeAb;
import ru.labon.automiet.models.UserAnswer;

/**
 * Created by Admin on 03.10.2017.
 */

public class MainDbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "MAIN.DB";
    private static final int DB_VERSION = 1;

    private Context context;
    private MainDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
    public Context getContext() {
        return context;
    }
    private static MainDbHelper instance;
    public static synchronized MainDbHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new MainDbHelper(context);
        return instance;
    }
    private SQLiteDatabase openedDatabase;
    @Override
    public SQLiteDatabase getWritableDatabase() {
        if( openedDatabase == null || !openedDatabase.isOpen() ) {
            openedDatabase = super.getWritableDatabase();
        }
        return openedDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void recreateAllTables(){
        ThemeAb.recreateTable(this);
        Task.recreateTable(this);
        Card.recreateTable(this);
        Question.recreateTable(this);
        AnswerGroup.recreateTable(this);
        UserAnswer.recreateTable(this);
    }
}
