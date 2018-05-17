package com.wills.carl.mealwatch;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wills.carl.mealwatch.model.Meal;

import java.util.ArrayList;

public class UpsetAdapter extends RecyclerView.Adapter<UpsetAdapter.ViewHolder>{

    private ArrayList<Meal> mealList;
    private final LayoutInflater inflater;


    public UpsetAdapter(Context c, ArrayList<Meal> meals) {
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
        }
    }



}
