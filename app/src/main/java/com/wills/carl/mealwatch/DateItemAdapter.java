package com.wills.carl.mealwatch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class DateItemAdapter extends RecyclerView.Adapter<DateItemAdapter.ViewHolder>{

    private ArrayList<LocalDate> dateList;
    private final LayoutInflater inflater;


    public DateItemAdapter(Context c, ArrayList<LocalDate> dates) {
        this.inflater = LayoutInflater.from(c);
        dateList = new ArrayList<>();
        dateList = dates;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.date_rv_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalDate date = dateList.get(position);
        holder.dateTv.setText(date.toString());
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView dateTv;
        private ViewHolder(final View v){
            super(v);
            dateTv = (TextView) v.findViewById(R.id.date_item_tv);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(),DateViewActivity.class);
                    i.putExtra("date", dateList.get(getAdapterPosition()));
                    view.getContext().startActivity(i);
                }
            });

        }
    }





}
