package com.wills.carl.mealwatch.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class MealContract {


    public static final String CONTENT_AUTHORITY = "com.wills.carl.mealwatch";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MEALS = "meals";

    public static final class MealEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MEALS)
                .build();

        public static final String TABLE_NAME = "meals";

        //Columns
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MEAL_NAME = "name";
        public static final String COLUMN_DAIRY = "dairy";
        public static final String COLUMN_GLUTEN = "gluten";

        public static Uri buildMealUriWithDate(long date){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

    }

}
