package com.wills.carl.mealwatch;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wills.carl.mealwatch.data.MealContract;
import com.wills.carl.mealwatch.model.Meal;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;


public class DateViewActivity extends AppCompatActivity {

    RecyclerView dateList;
    TextView dateTitle;
    private ArrayList<Meal> mealList;
    Button addMealbtn;
    AdView mAdView;
    DateViewAdapter adapter;
    LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_view);

        this.mealList = new ArrayList<Meal>();

        Intent i = getIntent();
        date = (LocalDate) i.getSerializableExtra("date");
        dateTitle = (TextView) findViewById(R.id.date_title);
        dateTitle.setText(date.toString());
        long d = date.toDate().getTime();
        getMealsForDate(date.toDateTimeAtStartOfDay().toDate().getTime());

        dateList = (RecyclerView) findViewById(R.id.date_list_rv);
        LinearLayoutManager llm = new LinearLayoutManager( this);
        llm.canScrollVertically();
        dateList.setLayoutManager(llm);
        adapter = new DateViewAdapter(this, this.mealList);
        dateList.setAdapter(adapter);

        addMealbtn = (Button) findViewById(R.id.add_new_meal_btn);
        addMealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddMeal.class);
                i.putExtra("date", date);
                startActivity(i);
            }
        });

        MobileAds.initialize(this, "ca-app-pub-8945138794185085~5522167142");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("15BFAD47BFFFB7BE220C3CD3EFACDF76")
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMealsForDate(date.toDateTimeAtStartOfDay().toDate().getTime());
        adapter.notifyDataSetChanged();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void getMealsForDate(long date){
        this.mealList.clear();
        Uri uri = MealContract.MealEntry.buildMealUriWithDate(date);

        Cursor cur = this.getContentResolver().query(uri, null, null, null, null);

        cur.moveToFirst();
        while(!cur.isAfterLast()){
            Meal m = new Meal(
                    cur.getString(cur.getColumnIndex(MealContract.MealEntry.COLUMN_MEAL_NAME)),
                    cur.getInt(cur.getColumnIndex(MealContract.MealEntry.COLUMN_DAIRY)) > 0,
                    cur.getInt(cur.getColumnIndex(MealContract.MealEntry.COLUMN_GLUTEN)) > 0,
                    LocalDate.fromDateFields(new Date(cur.getLong(cur.getColumnIndex(MealContract.MealEntry.COLUMN_DATE))))
            );
            m.setId(cur.getLong(cur.getColumnIndex(MealContract.MealEntry._ID)));

            if(m.getDate().toDateTimeAtStartOfDay().toDate().getTime() == date) {
                this.mealList.add(m);
            }

            cur.moveToNext();
        }
        cur.close();

    }


    private long convertStringToDateLong(String dateString){
        SimpleDateFormat f = new SimpleDateFormat("MMM DD yyyy");
        long millis = 0;
        try {
            Date d = f.parse(dateString);
            millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

}
