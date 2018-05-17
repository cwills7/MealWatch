package com.wills.carl.mealwatch;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wills.carl.mealwatch.data.MealContract;
import com.wills.carl.mealwatch.data.UpsetEventContract;
import com.wills.carl.mealwatch.model.Meal;
import com.wills.carl.mealwatch.model.UpsetEvent;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpsetActivity extends AppCompatActivity {

    LocalDate selected;
    Calendar cal = Calendar.getInstance();
    int year,month,day;
    ArrayList<Meal> mealList;

    RecyclerView upsetEventRv;
    int numGluten, numDairy;
    TextView summary;
    TextView previousSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upset_main);


        mealList = new ArrayList<>();
        selected = LocalDate.now();
        year = selected.getYear();
        month = selected.getMonthOfYear();
        day = selected.getDayOfMonth();
        LocalDate yesterday = selected.minusDays(1);

        getLastTwoDaysMeals(yesterday);

        insertUpsetEvent(selected, numGluten, numDairy);


        upsetEventRv = (RecyclerView) findViewById(R.id.upset_rv);
        LinearLayoutManager llm = new LinearLayoutManager( this);
        llm.canScrollVertically();
        upsetEventRv.setLayoutManager(llm);
        UpsetAdapter adapter = new UpsetAdapter(this, mealList);
        upsetEventRv.setAdapter(adapter);


        summary = (TextView) findViewById(R.id.upset_summary);
        summary.setText(String.format(getResources().getString(R.string.summary), numGluten, numDairy));

        generatePreviousSummary();

    }



    private void getLastTwoDaysMeals(LocalDate yesterday) {
        //Get last 2 days worth of entries
        Uri uri = MealContract.MealEntry.buildMealUriWithDate(selected.toDateTimeAtStartOfDay().toDate().getTime());
        Cursor cur = this.getContentResolver().query(uri,null, null, null, null);
        convertCurToMeals(cur);
        Uri uri2 = MealContract.MealEntry.buildMealUriWithDate(yesterday.toDateTimeAtStartOfDay().toDate().getTime());
        Cursor cur2 = this.getContentResolver().query(uri2,null, null, null, null);
        convertCurToMeals(cur2);
    }

    private void generatePreviousSummary() {
        ArrayList<UpsetEvent> lastUpsetEvents = getLastThreeUpsetEvents();

        previousSummary = (TextView) findViewById(R.id.previous_summary);
        StringBuilder prev = new StringBuilder("The last " + lastUpsetEvents.size() + " times you felt this way, you had this many of each: \n");
        for(int i = 0; i < lastUpsetEvents.size(); i++){
            prev.append(String.format(Locale.ENGLISH, "   Date: %s  \n         Number of items with Gluten: %d\n         Number of items with Dairy: %d\n\n",
                    lastUpsetEvents.get(i).getDate().toString(),
                    lastUpsetEvents.get(i).getNumGluten(),
                    lastUpsetEvents.get(i).getNumDairy()));
        }
        if(lastUpsetEvents.size() == 0){
            prev = new StringBuilder("You've never felt like this before! Next time you feel like this, you will be able to compare what you ate now, to what you ate then!");
        }

        previousSummary.setText(prev.toString());
    }

    private void insertUpsetEvent(LocalDate selected, int numGluten, int numDairy){
        Uri uri = UpsetEventContract.UpsetEventEntry.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(UpsetEventContract.UpsetEventEntry.COLUMN_DATE, selected.toDateTimeAtStartOfDay().toDate().getTime());
        cv.put(UpsetEventContract.UpsetEventEntry.COLUMN_NUM_DAIRY, numDairy);
        cv.put(UpsetEventContract.UpsetEventEntry.COLUMN_NUM_GLUTEN, numGluten);
        this.getContentResolver().insert(uri, cv);
    }

    private void convertCurToMeals(Cursor cur){
        cur.moveToFirst();
        while(!cur.isAfterLast()){
            Meal m = new Meal(
                    cur.getString(cur.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_NAME)),
                    cur.getInt(cur.getColumnIndex(MealContract.MealEntry.COLUMN_DAIRY)) > 0,
                    cur.getInt(cur.getColumnIndex(MealContract.MealEntry.COLUMN_GLUTEN)) >0,
                    LocalDate.fromDateFields(new Date(cur.getLong(cur.getColumnIndex(MealContract.MealEntry.COLUMN_DATE))))
            );

            if (m.isGluten()){
                numGluten++;
            }
            if (m.isDairy()) {
                numDairy ++;
            }

            mealList.add(m);
            cur.moveToNext();
        }
        cur.close();
    }

    public Date getToday(){
        Calendar cal = Calendar.getInstance();

        Date date = cal.getTime();
        return date;
    }

    private ArrayList<UpsetEvent> getLastThreeUpsetEvents(){
        ArrayList<UpsetEvent> previousSummary = new ArrayList<>();

        Uri uri = UpsetEventContract.UpsetEventEntry.CONTENT_URI;
        Cursor cur = this.getContentResolver().query(uri, null, null, null, UpsetEventContract.UpsetEventEntry.COLUMN_DATE + " DESC");

        cur.moveToFirst();
        for (int i = 0; i < 3; i++){
            if(!cur.isLast() && !cur.isAfterLast()) {
                previousSummary.add(
                        new UpsetEvent(
                                LocalDate.fromDateFields(new Date(cur.getLong(cur.getColumnIndex(MealContract.MealEntry.COLUMN_DATE)))),
                                cur.getInt(cur.getColumnIndex(UpsetEventContract.UpsetEventEntry.COLUMN_NUM_DAIRY)),
                                cur.getInt(cur.getColumnIndex(UpsetEventContract.UpsetEventEntry.COLUMN_NUM_GLUTEN))
                        )
                );
            }
            cur.moveToNext();
        }

        cur.close();
        return previousSummary;
    }

}
