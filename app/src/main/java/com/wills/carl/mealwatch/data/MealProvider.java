package com.wills.carl.mealwatch.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MealProvider extends ContentProvider {

    //Data Matching URI's
    public static final int CODE_MEALS = 145;
    public static final int CODE_MEAL_WITH_DATE = 146;
    public static final int CODE_UPSET_EVENT = 189;
    public static final int CODE_UPSET_EVENT_WITH_DATE = 190;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MealDbHelper mMealHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MealContract.CONTENT_AUTHORITY;


        //Add other paths here
        matcher.addURI(authority, MealContract.PATH_MEALS, CODE_MEALS);
        matcher.addURI(authority, MealContract.PATH_MEALS + "/#", CODE_MEAL_WITH_DATE);
        matcher.addURI(authority, UpsetEventContract.PATH_UPSET_EVENT, CODE_UPSET_EVENT);
        matcher.addURI(authority,UpsetEventContract.PATH_UPSET_EVENT + "/#", CODE_UPSET_EVENT_WITH_DATE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mMealHelper = new MealDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;


        switch(sUriMatcher.match(uri)){
            case CODE_MEAL_WITH_DATE: {
                String normalizedDate = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedDate};

                cursor = mMealHelper.getReadableDatabase().query(
                        MealContract.MealEntry.TABLE_NAME,
                        projection,
                        MealContract.MealEntry.COLUMN_DATE + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case CODE_UPSET_EVENT: {

                cursor = mMealHelper.getReadableDatabase().query(
                        UpsetEventContract.UpsetEventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
        }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mMealHelper.getWritableDatabase();
        long _id;
        switch(sUriMatcher.match(uri)){
            case CODE_MEALS: {
                _id = db.insert(MealContract.MealEntry.TABLE_NAME, null, contentValues);
                break;
            }
            case CODE_UPSET_EVENT: {
                _id = db.insert(UpsetEventContract.UpsetEventEntry.TABLE_NAME, null, contentValues);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }

        if (_id != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int numRowsDeleted;
        if (null == s) s = "1";

        switch(sUriMatcher.match(uri)){

            case CODE_MEALS: {
                numRowsDeleted = mMealHelper.getWritableDatabase().delete(
                        MealContract.MealEntry.TABLE_NAME,
                        s,
                        strings);
                break;
            }
            case CODE_UPSET_EVENT: {
                numRowsDeleted = mMealHelper.getWritableDatabase().delete(
                        UpsetEventContract.UpsetEventEntry.TABLE_NAME,
                        s,
                        strings
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }


}
