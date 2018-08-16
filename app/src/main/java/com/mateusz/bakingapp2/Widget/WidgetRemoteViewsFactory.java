package com.mateusz.bakingapp2.Widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.R;
import com.mateusz.bakingapp2.Utilities.Constants;

import java.lang.reflect.Type;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Recipe mRecipe;

    public WidgetRemoteViewsFactory(Context context){
        this.mContext=context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mRecipe = loadData(mContext);
    }

    public static Recipe loadData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_RECIPE, null);
        Type type = new TypeToken<Recipe>(){}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mRecipe.getListIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);
        remoteViews.setTextViewText(R.id.list_item_widget_text_view, mRecipe.getListIngredients().get(position).getStringIngredient());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
