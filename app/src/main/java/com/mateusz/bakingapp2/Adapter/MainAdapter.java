package com.mateusz.bakingapp2.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.R;
import com.mateusz.bakingapp2.RecipeActivity;
import com.mateusz.bakingapp2.Utilities.Constants;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private final Activity mActivity;
    private final Context mContext;
    private final List<Recipe> mData;

    public MainAdapter(Activity activity, Context context, List<Recipe> data){
        this.mActivity=activity;
        this.mContext=context;
        this.mData=data;
    }

    @NonNull
    @Override
    public MainAdapter.MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        return new MainHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.MainHolder holder, int position) {
        final Recipe recipe = mData.get(holder.getAdapterPosition());
        holder.setTextViewName(recipe.getStringName());
        holder.setTextViewServings(mContext.getResources().getString(R.string.servings)+" "+recipe.getIntServings());
        Log.e("RECIPE:", recipe.getStringName());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(mContext, RecipeActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_RECIPE_KEY, recipe);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MainHolder extends RecyclerView.ViewHolder{

        final TextView textViewName;
        final TextView textViewServings;
        final CardView itemLayout;


        public MainHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.main_list_item_text_view_name);
            textViewServings = itemView.findViewById(R.id.main_list_item_text_view_servings);
            itemLayout = itemView.findViewById(R.id.main_list_item_layout);
        }

        public void setTextViewName(String title){textViewName.setText(title);}
        public void setTextViewServings(String servings){textViewServings.setText(servings);}
    }
}
