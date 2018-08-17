package com.mateusz.bakingapp2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mateusz.bakingapp2.Adapter.MainAdapter;
import com.mateusz.bakingapp2.Model.Ingredient;
import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Model.Step;
import com.mateusz.bakingapp2.Utilities.Constants;
import com.mateusz.bakingapp2.Utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_error_text_view)
    TextView textView;

    private List<Recipe> mRecipes;
    private Loader<List<Recipe>> loader;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState!=null&&mRecipes.isEmpty()) {
            if(savedInstanceState.containsKey(Constants.SAVED_STATE_KEY_RECIPES)){
                mRecipes = stringToRecipes(savedInstanceState.getString(Constants.SAVED_STATE_KEY_RECIPES));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        loaderManager = getSupportLoaderManager();
        loader = loaderManager.getLoader(Constants.LOADER_RECIPES_ID);
        mRecipes = new ArrayList<Recipe>();

        Log.e("SIZE", String.valueOf(mRecipes.size()));
    }




    @Override
    protected void onResume() {
        super.onResume();
        makeOperationLoadRecipes();
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null&&mRecipes.isEmpty()) {
            if(savedInstanceState.containsKey(Constants.SAVED_STATE_KEY_RECIPES)){
                mRecipes = stringToRecipes(savedInstanceState.getString(Constants.SAVED_STATE_KEY_RECIPES));
            }
        }

    }

    public String recipesToString(List<Recipe> recipes){
        Gson gson = new Gson();
        String json = gson.toJson(recipes);
        return json;
    }

    public List<Recipe> stringToRecipes(String string){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Recipe>>(){}.getType();
        List<Recipe> recipes = gson.fromJson(string, type);
        return recipes;
    }

    boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void makeOperationLoadRecipes(){
        if(isOnline()&&mRecipes.isEmpty()) {
            textView.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            if (loader == null) {
                loaderManager.initLoader(Constants.LOADER_RECIPES_ID, bundle, this);
            } else {
                loaderManager.restartLoader(Constants.LOADER_RECIPES_ID, bundle, this);
            }
        } else if (mRecipes.isEmpty()){
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.error_message);
        }

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(mRecipes.size()<1){
                    forceLoad();
                } else {
                    deliverResult(mRecipes);
                }

            }

            @Override
            public void deliverResult(List<Recipe> data) {
                mRecipes = data;
                super.deliverResult(data);
            }

            @Override
            public List<Recipe> loadInBackground() {
                String jsonString = "";
                try {
                    jsonString += NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getRecipesUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return getRecipes(jsonString);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<Recipe>> loader, List<Recipe> data) {
        mRecipes = data;
        Log.e("DATA", String.valueOf(data.size()));
        MainAdapter mAdapter = new MainAdapter(this, this, mRecipes);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<Recipe>> loader) {

    }


    public List<Recipe> getRecipes(String jsonString){
        List<Recipe> recipes = new ArrayList<Recipe>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int a = 0; a < jsonArray.length(); a++){
                JSONObject jsonRecipe = jsonArray.getJSONObject(a);
                String stringName = jsonRecipe.getString(Constants.JSON_KEY_RECIPE_NAME);
                List<Ingredient> listIngredients = getListOfIngredients(jsonRecipe.getJSONArray(Constants.JSON_KEY_RECIPE_INGREDIENTS));
                List<Step> listSteps = getListOfSteps(jsonRecipe.getJSONArray(Constants.JSON_KEY_RECIPE_STEPS));
                int intServings = jsonRecipe.getInt(Constants.JSON_KEY_RECIPE_SERVINGS);
                recipes.add(new Recipe(stringName, listIngredients, listSteps, intServings));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private List<Ingredient> getListOfIngredients(JSONArray ingredientArray){
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        try {
            for (int b = 0; b < ingredientArray.length(); b++){
                JSONObject jsonIngredient = ingredientArray.getJSONObject(b);
                double doubleQuantity = jsonIngredient.getDouble(Constants.JSON_KEY_INGREDIENT_QUANTITY);
                String stringMeasure = jsonIngredient.getString(Constants.JSON_KEY_INGREDIENT_MEASURE);
                String stringIngredient = jsonIngredient.getString(Constants.JSON_KEY_INGREDIENT_INGREDIENT);
                ingredients.add(new Ingredient(doubleQuantity, stringMeasure, stringIngredient));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    private List<Step> getListOfSteps(JSONArray stepsArray){
        List<Step> steps = new ArrayList<Step>();
        try {
            for (int c = 0; c < stepsArray.length(); c++){
                JSONObject jsonStep = stepsArray.getJSONObject(c);
                int intId = jsonStep.getInt(Constants.JSON_KEY_STEP_ID);
                String stringShortDescription = jsonStep.getString(Constants.JSON_KEY_STEP_SHORT_DESCRIPTION);
                String stringDescription = jsonStep.getString(Constants.JSON_KEY_STEP_DESCRIPTION);
                String stringVideoURL = jsonStep.getString(Constants.JSON_KEY_STEP_VIDEO_URL);
                String stringThumbnailURL = jsonStep.getString(Constants.JSON_KEY_STEP_THUMBNAIL_URL);
                steps.add(new Step(intId, stringShortDescription, stringDescription, stringVideoURL, stringThumbnailURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return steps;
    }
}
