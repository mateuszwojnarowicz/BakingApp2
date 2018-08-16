package com.mateusz.bakingapp2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Utilities.Constants;
import com.mateusz.bakingapp2.Widget.RecipeWidgetService;

import java.lang.reflect.Type;

import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(Constants.INTENT_EXTRA_RECIPE_KEY);
        if(recipe!=null) {
            saveData(this);
        } else {
            recipe=loadData(this);
        }
        RecipeWidgetService.updateWidget(this, recipe);
        setTitle(recipe.getStringName());
        MasterFragment recipeFragment = new MasterFragment();
        recipeFragment.setData(recipe);
        recipeFragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction().add(R.id.recipe_fragment_container, recipeFragment).commit();
        if(getResources().getBoolean(R.bool.isTablet)){
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setData(recipe);
            detailFragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction().add(R.id.step_fragment_container, detailFragment).commit();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                recipe= data.getParcelableExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void saveData(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        editor.putString("recipe", json);
        editor.apply();
    }

    public static Recipe loadData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipe", null);
        Type type = new TypeToken<Recipe>(){}.getType();
        return gson.fromJson(json, type);
    }


}
