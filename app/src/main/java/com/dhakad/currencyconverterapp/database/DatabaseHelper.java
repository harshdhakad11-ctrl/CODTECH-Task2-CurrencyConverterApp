package com.dhakad.currencyconverterapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import com.dhakad.currencyconverterapp.model.HistoryModel;

import java.util.ArrayList;

/**
 * Database Helper Class
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    private static final String DATABASE_NAME = "CurrencyConverter.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_HISTORY = "history";

    // Column Names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FROM = "from_currency";
    public static final String COLUMN_TO = "to_currency";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_DATE = "date_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_HISTORY + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_FROM + " TEXT,"
                        + COLUMN_TO + " TEXT,"
                        + COLUMN_AMOUNT + " REAL,"
                        + COLUMN_RESULT + " REAL,"
                        + COLUMN_DATE + " TEXT"
                        + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);

        onCreate(db);

    }

    /**
     * Insert conversion history into database
     */
    public void insertHistory(String from,
                              String to,
                              double amount,
                              double result,
                              String dateTime) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_FROM, from);
        values.put(COLUMN_TO, to);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_RESULT, result);
        values.put(COLUMN_DATE, dateTime);

        db.insert(TABLE_HISTORY, null, values);

        db.close();

    }

    /**
     * Get all conversion history
     */
    public ArrayList<HistoryModel> getAllHistory() {

        ArrayList<HistoryModel> historyList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_HISTORY + " ORDER BY id DESC",
                null);

        if (cursor.moveToFirst()) {

            do {

                historyList.add(new HistoryModel(

                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5)

                ));

            } while (cursor.moveToNext());

        }

        cursor.close();

        db.close();

        return historyList;

    }

    /**
     * Delete all history
     */
    public void clearHistory() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_HISTORY, null, null);

        db.close();

    }
}