package com.example.android.receptuknyga;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.receptuknyga.List.RecipeListAdapter;

import java.text.Normalizer;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    EditText searchName;
    EditText searchIngredient;
    ImageButton searchButton;
    ImageButton searchIngredientButton;
    Spinner categorySearchSpinnerMain;
    String categoryItem;
    ImageButton searchCategoryButton;
    LinearLayout noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        isStoragePermissionGranted();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        allRecipes();

        noResults = findViewById(R.id.no_result_layout);

        searchCategoryButton = findViewById(R.id.searchCategoryButton);
        searchCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCategory();
            }
        });

        categorySearchSpinnerMain = findViewById(R.id.categorySearchSpinnerMain);
        categorySpinner();

        searchName = findViewById(R.id.searchNameEditText);
        enterButtonNameSearch(searchName);

        searchIngredient = findViewById(R.id.searchIngredientEditText);
        enterButtonIngredientSearch(searchIngredient);

        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(searchRecipe);

        searchIngredientButton = findViewById(R.id.searchIngredientButton);
        searchIngredientButton.setOnClickListener(searchIngredientRecipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void categorySpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories, R.layout.simple_spinner_item_main);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_main);

        categorySearchSpinnerMain.setPrompt("Kategorija");
        categorySearchSpinnerMain.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter, R.layout.contact_spinner_row_nothing_selected_main, this));
        categorySearchSpinnerMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    categoryItem = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LinearLayout searchLayout = findViewById(R.id.searchLayout);
        LinearLayout searchCategoryLayout = findViewById(R.id.searchCategoryLayout);
        LinearLayout searchIngredientLayout = findViewById(R.id.search_ingredient_layout);

        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.addRecipeId: {
                searchLayout.setVisibility(View.GONE);
                searchCategoryLayout.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.homeId: {
                searchLayout.setVisibility(View.GONE);
                searchCategoryLayout.setVisibility(View.GONE);
                searchIngredientLayout.setVisibility(View.GONE);
                allRecipes();
                break;
            }
            case R.id.search: {
                allRecipes();
                searchLayout.setVisibility(View.VISIBLE);
                searchCategoryLayout.setVisibility(View.GONE);
                searchIngredientLayout.setVisibility(View.GONE);
                break;
            }
            case R.id.categorySearch: {
                allRecipes();
                searchCategoryLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                searchIngredientLayout.setVisibility(View.GONE);
                break;
            }
            case R.id.ingredientSearch: {
                allRecipes();
                searchIngredientLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                searchCategoryLayout.setVisibility(View.GONE);
                break;
            }
        }
        categorySpinner();
        return super.onOptionsItemSelected(item);
    }

    public void searchIngredient() {
        String ingredientASCII = Normalizer.normalize(searchIngredient.getText().toString(), Normalizer.Form.NFD).
                replaceAll("[^\\p{ASCII}]", "");
        final LiveData<List<Recipe>> searchIngredients =
                AppDatabase.getInstance(getApplicationContext()).recipeDao().
                        ingredientName(ingredientASCII);

        RecyclerView recyclerView = findViewById(R.id.recipeMainView);
        final RecipeListAdapter adapter = new RecipeListAdapter(this);
        searchIngredients.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });


        if (adapter.getItemCount() == 0) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);

        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void search() {
        String nameASCII = Normalizer.normalize(searchName.getText().toString(), Normalizer.Form.NFD).
                replaceAll("[^\\p{ASCII}]", "");
        final LiveData<List<Recipe>> searchRecipe =
                AppDatabase.getInstance(getApplicationContext()).recipeDao().
                        recipeName(nameASCII);

        RecyclerView recyclerView = findViewById(R.id.recipeMainView);

        final RecipeListAdapter adapter = new RecipeListAdapter(this);

        searchRecipe.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });

        if (adapter.getItemCount() == 0) {
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);

        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void searchCategory() {
        final LiveData<List<Recipe>> searchRecipeCategory =
                AppDatabase.getInstance(getApplicationContext()).
                        recipeDao().recipesByCategory(categoryItem);

        RecyclerView recyclerView = findViewById(R.id.recipeMainView);

        final RecipeListAdapter adapter = new RecipeListAdapter(this);
        searchRecipeCategory.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });

        if(adapter.getItemCount() == 0){
            noResults.setVisibility(View.VISIBLE);
        } else {
            noResults.setVisibility(View.GONE);

        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void allRecipes() {
        RecyclerView recyclerView = findViewById(R.id.recipeMainView);

        final RecipeListAdapter adapter = new RecipeListAdapter(this);
        final LiveData<List<Recipe>> recipeList =
                AppDatabase.getInstance(this).recipeDao().allRecipes();

        recipeList.observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public void enterButtonNameSearch(final EditText name) {
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    name.setSelection(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                    searchButton.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void enterButtonIngredientSearch(final EditText ingredient) {
        ingredient.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    ingredient.setSelection(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ingredient.getWindowToken(), 0);
                    searchIngredientButton.performClick();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public View.OnClickListener searchRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(searchName.getWindowToken(), 0);
            }
            search();
            searchName.getText().clear();
        }
    };

    public View.OnClickListener searchIngredientRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(searchIngredient.getWindowToken(), 0);
            }
            searchIngredient();
            searchIngredient.getText().clear();
        }
    };
}