package com.mateusz.bakingapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mateusz.bakingapp2.Model.Recipe;
import com.mateusz.bakingapp2.Utilities.Constants;

public class DetailPhoneActivity extends AppCompatActivity {

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_phone);
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(Constants.INTENT_EXTRA_OTHER_KEY);
        DetailFragment detailFragment = new DetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.step_phone_fragment_container, detailFragment).commit();
    }


}
