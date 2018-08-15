package com.mateusz.bakingapp2.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateusz.bakingapp2.IngredientsWidget;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Utilities.Constants;

import java.lang.reflect.Type;

public class RecipeWidgetService extends RemoteViewsService{

    public static void updateWidget(Context context, Recipe recipe) {
        saveData(recipe, context);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidget.class));
        IngredientsWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new WidgetRemoteViewsFactory(getApplicationContext());
    }
    public static Recipe loadData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipe", null);
        Type type = new TypeToken<Recipe>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static void saveData(Recipe mRecipe, Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mRecipe);
        editor.putString("recipe", json);
        editor.apply();
    }
}
