package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class RecipeInfoActivity extends AppCompatActivity {

    private static final String TAG = "RecipeInfoActivity";

    int recipeId;
    LiveData<FullRecipe> fullRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        recipeId = getIntent().getIntExtra("id", -1);
        Log.d(TAG, "Activity started with recipe id = " + recipeId);
        fullRecipe = AppDatabase.getInstance(this).recipeDao().allRecipeInfo(recipeId);
        Log.d(TAG, "Receive recipe from db " + fullRecipe);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("SANTRAUKA"));
        tabLayout.addTab(tabLayout.newTab().setText("INGREDIENTAI"));
        tabLayout.addTab(tabLayout.newTab().setText("EIGA"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Toolbar toolbar = findViewById(R.id.main_fragments_toolbar);
        setSupportActionBar(toolbar);

        final ViewPager viewPager = findViewById(R.id.pager);

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public LiveData<FullRecipe> getFullRecipe() {
        return fullRecipe;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify_button: {
                modifyButtonPressed(recipeId);
                return true;
            }
            case R.id.delete_button: {
                deleteRecipe();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_for_fragments, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    public void modifyButtonPressed(int recipeId) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("id", recipeId);
        startActivity(intent);
    }

    public void deleteRecipe() {
        AppDatabase.getInstance(this).recipeDao().deleteRecipe(recipeId);
        finish();
    }
}