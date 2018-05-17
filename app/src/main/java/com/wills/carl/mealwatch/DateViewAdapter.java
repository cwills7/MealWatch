package com.wills.carl.mealwatch;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wills.carl.mealwatch.data.MealContract;
import com.wills.carl.mealwatch.model.Meal;

import java.util.ArrayList;

public class DateViewAdapter extends RecyclerView.Adapter<DateViewAdapter.ViewHolder>{

    private ArrayList<Meal> mealList;
    private final LayoutInflater inflater;


    public DateViewAdapter(Context c, ArrayList<Meal> meals) {
        this.inflater = LayoutInflater.from(c);
        mealList = new ArrayList<>();
        mealList = meals;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.upset_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal m = mealList.get(position);

        holder.upsetDesc.setText(m.getDescription());
        if(m.isDairy()) {
            holder.dairyIcon.setImageResource(R.drawable.dairy_icon);
        }
        if(m.isGluten()){
            holder.glutenIcon.setImageResource(R.drawable.gluten_icon);
        }


    }

    @Override
    public int getItemCount() {
        if (mealList == null)
            return 0;
        else
            return mealList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView upsetDesc;
        private final ImageView dairyIcon;
        private final ImageView glutenIcon;
        private ViewHolder(final View v){
            super(v);
            upsetDesc = (TextView) v.findViewById(R.id.upset_desc);
            dairyIcon = (ImageView) v.findViewById(R.id.dairy_icon);
            glutenIcon = (ImageView) v.findViewById(R.id.gluten_icon);



            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    switch(which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Meal m = mealList.get(getAdapterPosition());
                            Uri uri = MealContract.MealEntry.CONTENT_URI;
                            v.getContext().getContentResolver().delete(uri, "_id = ?", new String[]{Long.toString(m.getId())});
                            mealList.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:

                            break;


                    }
                }
            };

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Are you sure you want to delete this meal?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            });

        }


    }



}
