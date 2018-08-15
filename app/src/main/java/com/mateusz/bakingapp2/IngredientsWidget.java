package com.mateusz.bakingapp2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Utilities.Constants;
import com.mateusz.bakingapp2.Widget.RecipeWidgetService;

import java.lang.reflect.Type;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        Recipe recipe = loadData(context);
        Log.e("is recipe null?", String.valueOf(recipe==null));
        if (recipe != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

            views.setTextViewText(R.id.ingredients_widget_tv, recipe.getStringName());
            views.setOnClickPendingIntent(R.id.ingredients_widget_tv, pendingIntent);

            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.ingredients_widget_lv, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.ingredients_widget_lv);
        }
    }

    public static Recipe loadData(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, mContext.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipe", null);
        Type type = new TypeToken<Recipe>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

