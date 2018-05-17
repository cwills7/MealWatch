package com.wills.carl.mealwatch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wills.carl.mealwatch.data.MealContract;


import android.net.Uri;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

public class AddMeal extends AppCompatActivity{

    int year,month,day;
    Button setDateBtn;
    LocalDate today;
    Button sumbitMealBtn;
    EditText description;
    CheckBox dairy, gluten;
    Calendar cal = Calendar.getInstance();
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_meal);

        Intent i = getIntent();
        try{
            today = (LocalDate) i.getSerializableExtra("date");
        } catch(NullPointerException e){
            //nothin
        }

        if(today == null){
            today = LocalDate.now();
        }

        String todayString = today.toString();
        year = today.getYear();
        month = today.getMonthOfYear();
        day = today.getDayOfMonth();
        description = (EditText) findViewById(R.id.add_meal_desc);
        dairy = (CheckBox) findViewById(R.id.dairy_checkbox);
        gluten = (CheckBox) findViewById(R.id.gluten_checkbox);

        setDateBtn = (Button) findViewById(R.id.date_picker_dialog_btn);
        setDateBtn.setText(todayString);


        sumbitMealBtn = (Button) findViewById(R.id.submit_meal_btn);

        sumbitMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = MealContract.MealEntry.CONTENT_URI;
                ContentValues cv = new ContentValues();
                cv.put(MealContract.MealEntry.COLUMN_MEAL_NAME, description.getText().toString());
                cv.put(MealContract.MealEntry.COLUMN_DATE, today.toDate().getTime());
                cv.put(MealContract.MealEntry.COLUMN_DAIRY, dairy.isChecked());
                cv.put(MealContract.MealEntry.COLUMN_GLUTEN, gluten.isChecked());
                view.getContext().getContentResolver().insert(uri, cv);

                finish();
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

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    cal.set(arg1, arg2, arg3);
                    today = today.withDayOfMonth(arg3);
                    today = today.withMonthOfYear(arg2);
                    today = today.withYear(arg1);
                    setDateBtn.setText(today.toString());
                }
            };

    public Date getToday(){
        Calendar cal = Calendar.getInstance();

        Date date = cal.getTime();
        return date;
    }

}
