package com.wills.carl.mealwatch.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class UpsetEventContract {

    public static final String CONTENT_AUTHORITY = "com.wills.carl.mealwatch";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_UPSET_EVENT = "upset";

    public static final class UpsetEventEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_UPSET_EVENT)
                .build();

        public static final String TABLE_NAME = "upset";

        //Columns
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NUM_DAIRY = "dairy";
        public static final String COLUMN_NUM_GLUTEN = "gluten";

        public static Uri buildUpsetEventUriWithDate(long date){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

    }
}
