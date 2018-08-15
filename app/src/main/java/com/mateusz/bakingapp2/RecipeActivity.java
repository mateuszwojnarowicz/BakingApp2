package com.mateusz.bakingapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Utilities.Constants;

import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        final Recipe recipe = intent.getParcelableExtra(Constants.INTENT_EXTRA_RECIPE_KEY);
        setTitle(recipe.getStringName());

        MasterFragment recipeFragment = new MasterFragment();
        recipeFragment.setData(recipe.getListIngredients(), recipe.getListSteps());
        getSupportFragmentManager().beginTransaction().add(R.id.recipe_fragment_container, recipeFragment).commit();

    }
}
