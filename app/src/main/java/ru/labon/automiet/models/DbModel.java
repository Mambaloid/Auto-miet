package ru.labon.automiet.models;

import ru.labon.automiet.helpers.MainDbHelper;

/**
 * Created by Admin on 03.10.2017.
 */

public class DbModel {
//    abstract
    public boolean save(MainDbHelper dbHelper){return false;}
    public static void checkTableExist(MainDbHelper dbHelper){};
    private transient boolean isNewRecord = false;
    private transient boolean isFromServer = false;
    public DbModel setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
        return this;
    }
    public DbModel setIsFromServer(boolean isFromServer) {
        this.isFromServer = isFromServer;
        return this;
    }
    public boolean isNewRecord() {
        return this.isNewRecord;
    }
    public boolean isFromServer() {
        return this.isFromServer;
    }
}