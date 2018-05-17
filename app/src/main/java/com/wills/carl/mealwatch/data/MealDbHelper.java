package com.wills.carl.mealwatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wills.carl.mealwatch.data.MealContract.MealEntry;
import com.wills.carl.mealwatch.data.UpsetEventContract.UpsetEventEntry;

public class MealDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "meals.db";

    private static final int DATABASE_VERSION = 6;

    public MealDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MEAL_TABLE  =
                "CREATE TABLE " + MealEntry.TABLE_NAME + " (" +
                        MealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MealEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                        MealEntry.COLUMN_MEAL_NAME + " TEXT NOT NULL, " +
                        MealEntry.COLUMN_DAIRY + " NUMERIC NOT NULL, " +
                        MealEntry.COLUMN_GLUTEN + " NUMERIC NOT NULL);";
        final String SQL_CREATE_UPSET_EVENT_TABLE =
                "CREATE TABLE " + UpsetEventEntry.TABLE_NAME + " (" +
                        UpsetEventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        UpsetEventEntry.COLUMN_DATE + " INTEGER UNIQUE NOT NULL, " +
                        UpsetEventEntry.COLUMN_NUM_DAIRY + " INTEGER NOT NULL, " +
                        UpsetEventEntry.COLUMN_NUM_GLUTEN + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MEAL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_UPSET_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MealEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UpsetEventEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
