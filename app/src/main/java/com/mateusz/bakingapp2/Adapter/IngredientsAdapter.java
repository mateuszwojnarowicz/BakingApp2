package com.mateusz.bakingapp2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mateusz.bakingapp2.Model.Ingredient;
import com.mateusz.bakingapp2.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientHolder> {

    private final Context mContext;
    private final List<Ingredient> mData;

    public IngredientsAdapter(Context context, List<Ingredient> data){
        this.mContext=context;
        this.mData=data;
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_list_item, parent, false);
        return new IngredientHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapter.IngredientHolder holder, int position) {
        final Ingredient ingredient = mData.get(holder.getAdapterPosition());
        holder.setTextViewIngredient("- "+ingredient.getStringIngredient());
        holder.setTextViewQuantity(getQuantity(ingredient.getDoubleQuantity())+" "+ingredient.getStringMeasure());
    }

    private static String getQuantity(double quantity){
        if(Math.floor(quantity) == quantity) {
            return String.valueOf((int) quantity);
        } else {
            return String.valueOf(quantity);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder{

        final TextView textViewIngredient;
        final TextView textViewQuantity;
        final LinearLayout itemLayout;


        public IngredientHolder(View itemView) {
            super(itemView);
            textViewIngredient = itemView.findViewById(R.id.ingredients_list_item_text_view_ingredient);
            textViewQuantity = itemView.findViewById(R.id.ingredients_list_item_text_view_quantity);
            itemLayout = itemView.findViewById(R.id.ingredients_list_item_layout);
        }

        public void setTextViewIngredient(String value){textViewIngredient.setText(value);}
        public void setTextViewQuantity(String value){textViewQuantity.setText(value);}
    }

}
