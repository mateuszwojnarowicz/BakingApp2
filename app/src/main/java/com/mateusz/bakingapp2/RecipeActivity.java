package com.mateusz.bakingapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Utilities.Constants;
import com.mateusz.bakingapp2.Widget.RecipeWidgetService;

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
        RecipeWidgetService.updateWidget(this, recipe);
        setTitle(recipe.getStringName());
        MasterFragment recipeFragment = new MasterFragment();
        recipeFragment.setData(recipe);
        getSupportFragmentManager().beginTransaction().add(R.id.recipe_fragment_container, recipeFragment).commit();
        if(getResources().getBoolean(R.bool.isTablet)){
            DetailFragment detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.step_fragment_container, detailFragment).commit();
        }



    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        editor.putString("recipe", json);
        editor.apply();
    }


}
