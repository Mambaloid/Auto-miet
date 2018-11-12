package ru.labon.automiet.helpers;

import android.database.Cursor;



public class CursorDataConverter implements TypeDataConverter<Cursor> {
    private static CursorDataConverter instance;
    public static CursorDataConverter getInstance() {
        if( instance == null ) {
            instance = new CursorDataConverter();
        }
        return instance;
    }
    @Override
    public Integer getInteger(Cursor cursor, String columnName, Integer defaultValue) {
        int columnIndex = getColumnIndex(cursor, columnName);
        if( cursor.isNull(columnIndex) ) {
            return defaultValue;
        }
        return cursor.getInt(columnIndex);
    }
    @Override
    public Long getLong(Cursor cursor, String columnName, Long defaultValue) {
        int columnIndex = getColumnIndex(cursor, columnName);
        if( cursor.isNull(columnIndex) ) {
            return defaultValue;
        }
        return cursor.getLong(columnIndex);
    }
    @Override
    public Double getDouble(Cursor cursor, String columnName, Double defaultValue) {
        int columnIndex = getColumnIndex(cursor, columnName);
        if( cursor.isNull(columnIndex) ) {
            return defaultValue;
        }
        return cursor.getDouble(columnIndex);
    }
    @Override
    public String getString(Cursor cursor, String columnName, String defaultValue) {
        int columnIndex = getColumnIndex(cursor, columnName);
        if( cursor.isNull(columnIndex) ) {
            return defaultValue;
        }
        return cursor.getString(columnIndex);
    }
    public int getColumnIndex(Cursor cursor, String columnName) {
        return cursor.getColumnIndexOrThrow(columnName);
//        return cursor.getColumnIndex(columnName);
    }
}