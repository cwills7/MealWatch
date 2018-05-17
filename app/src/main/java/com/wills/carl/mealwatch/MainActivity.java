package com.wills.carl.mealwatch;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.lb.auto_fit_textview.AutoResizeTextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView dateList;
    private DateItemAdapter adapter;
    Button upsetBtn;
    Button addMealBtn;
    AdView mAdView;
    AutoResizeTextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JodaTimeAndroid.init(this);

        ArrayList<LocalDate> dates = setupDateRecyclerData();

        titleView = (AutoResizeTextView) findViewById(R.id.main_title);
        titleView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, R.dimen.title_text_size, getResources().getDisplayMetrics()));

        dateList = (RecyclerView) findViewById(R.id.date_rv);
        dateList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.canScrollVertically();
        dateList.setLayoutManager(llm);
        adapter = new DateItemAdapter(this, dates);
        dateList.setAdapter(adapter);


        upsetBtn = (Button) findViewById(R.id.upset_btn);
        upsetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpsetActivity.class);
                startActivity(i);
            }
        });

        addMealBtn = (Button) findViewById(R.id.add_meal_btn);
        addMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent i = new Intent(view.getContext(), AddMeal.class);
                  view.getContext().startActivity(i);
            }
        });

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("15BFAD47BFFFB7BE220C3CD3EFACDF76")
                .build();
        mAdView.loadAd(adRequest);

    }

    private ArrayList<LocalDate> setupDateRecyclerData() {
        ArrayList<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        dates.add(today);
        dates.add(today.minusDays(1));
        dates.add(today.minusDays(2));
        dates.add(today.minusDays(3));
        dates.add(today.minusDays(4));
        dates.add(today.minusDays(5));
        dates.add(today.minusDays(6));
        return dates;
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

    public void rateApp(View rateMeButton) {
        String packageName = this.getPackageName();
        String playStoreAppUri = "market://details?id=" + packageName;
        String playStoreSiteUri = "https://play.google.com/store/apps/details?id=" + packageName;

        try {
            Intent playStoreAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreAppUri));
            startActivity(playStoreAppIntent);
        } catch (ActivityNotFoundException e) {
            Intent playStoreBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreSiteUri));
            startActivity(playStoreBrowserIntent);
        }
    }

}
