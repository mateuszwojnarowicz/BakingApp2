package com.mateusz.bakingapp2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mateusz.bakingapp2.DetailPhoneActivity;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Model.Step;
import com.mateusz.bakingapp2.R;
import com.mateusz.bakingapp2.Utilities.Constants;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {

    private final Context mContext;
    private final Recipe mData;

    public StepsAdapter(Context context, Recipe data){
        this.mContext=context;
        this.mData=data;
    }

    @NonNull
    @Override
    public StepsAdapter.StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_item, parent, false);
        return new StepHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final StepsAdapter.StepHolder holder, int position) {

        final Step step = mData.getListSteps().get(holder.getAdapterPosition());
        holder.setTextViewName(step.getStringShortDescription());
        holder.setTextViewName(holder.getAdapterPosition()+". "+step.getStringShortDescription());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(mContext.getResources().getBoolean(R.bool.isTablet))){
                    Intent intent;
                    intent = new Intent(mContext, DetailPhoneActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_OTHER_KEY, mData);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.getListSteps().size();
    }

    public class StepHolder extends RecyclerView.ViewHolder{

        final TextView textViewName;
        final LinearLayout itemLayout;


        public StepHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.steps_list_item_text_view);
            itemLayout = itemView.findViewById(R.id.steps_list_item_layout);
        }

        public void setTextViewName(String title){textViewName.setText(title);}
    }
}
