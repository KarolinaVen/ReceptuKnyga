package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private static final String TAG = "AddRecipeActivity";

    private LinearLayout directions_layout;
    private LinearLayout add_ingredients_layout;

    EditText preparationHours;
    EditText preparationMinutes;
    EditText cookingHours;
    EditText cookingMinutes;
    EditText yield;
    EditText comments;
    EditText source;
    EditText recipeNameInput;
    EditText directionsEditText;
    EditText ingredientsEditText;
    EditText ingredientsNumber;
    String filePath;
    String categoryItem;
    Bitmap yourSelectedImage;
    RatingBar ratingBar;
    AppDatabase appDatabase;
    ImageView imageViewPhoto;
    ImageButton addDirectionButton;
    ImageButton addIngredientButton;
    Spinner categorySpinner;
    LiveData<FullRecipe> fullRecipe;
    RadioButton metricRadioButton;
    RadioButton usRadioButton;
    Toolbar myToolbar;

    int recipeId;
    int measurementSystemId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recipeId = getIntent().getIntExtra("id", -1);
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        if (recipeId != -1) {
            fullRecipe = AppDatabase.getInstance(this).recipeDao().allRecipeInfo(recipeId);
            updateRecipe();
        }

        metricRadioButton = findViewById(R.id.metricSystem);
        usRadioButton = findViewById(R.id.usSystem);
        recipeNameInput = findViewById(R.id.recipe_title2);
        yield = findViewById(R.id.yield);
        comments = findViewById(R.id.comments);
        source = findViewById(R.id.source);
        preparationHours = findViewById(R.id.preparationHours);
        preparationMinutes = findViewById(R.id.preparationMinutes);
        cookingHours = findViewById(R.id.cookingHours);
        cookingMinutes = findViewById(R.id.cookingMinutes);
        ratingBar = findViewById(R.id.ratingBar);
        directions_layout = findViewById(R.id.directions_layout);
        add_ingredients_layout = findViewById(R.id.add_ingredients_layout);
        directionsEditText = findViewById(R.id.directions_edit_text);
        ingredientsEditText = findViewById(R.id.ingredients_edit_text);
        ingredientsNumber = findViewById(R.id.ingredients_number);

        imageViewPhoto = findViewById(R.id.add_photo_id);
        imageViewPhoto.setOnClickListener(choosePhoto);

        addIngredientButton = findViewById(R.id.add_ingredient_button);
        addIngredientButton.setOnClickListener(ingredients);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        categorySpinner = findViewById(R.id.category);
        categorySpinner();

        addDirectionButton = findViewById(R.id.addDirectionButton);
        addDirectionButton.setOnClickListener(directions);
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.metricSystem: {
                if (metricRadioButton.isChecked()) {
                    measurementSystemId = 1;
                }
            }
            case R.id.usSystem: {
                if (usRadioButton.isChecked()) {
                    measurementSystemId = 2;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button: {
                save();
                finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public View.OnClickListener remove = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            add_ingredients_layout.removeView((View) v.getParent());
        }
    };

    public View.OnClickListener removeDirection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            directions_layout.removeView((View) v.getParent());
        }
    };

    public View.OnClickListener directions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addDirection(null);
        }
    };

    public void categorySpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setPrompt("Kategorija");
        categorySpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter, R.layout.contact_spinner_row_nothing_selected, this));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void spinnerMeasurement(final Spinner ingredientsSpinner, String selection, int measurementSystemId) {
        List<String> measurements = appDatabase.measurementDao().measurementListBySystemId(measurementSystemId);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, measurements);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientsSpinner.setPrompt("Matavimo vnt.");
        ingredientsSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter1, R.layout.contact_spinner_row_nothing_selected_measurement, this));

        if (selection != null) {
            final int selectedPos = adapter1.getPosition(selection) + 1;
            ingredientsSpinner.setSelection(selectedPos);
        }
    }

    public void addIngredient(RecipeIngredient ingredient, int id) {
        Log.i(TAG, "addIngirient called with: " + ingredient);
        View view = LayoutInflater.from(this).inflate(R.layout.ingridients, add_ingredients_layout, false);
        Spinner ingredientsSpinner = view.findViewById(R.id.measurement_spinner);

        ImageButton clearIngredientButton = view.findViewById(R.id.clear_ingredient_button);
        clearIngredientButton.setOnClickListener(remove);

        if (ingredient != null) {
            EditText nameInput = view.findViewById(R.id.ingredients_edit_text);
            nameInput.setText(ingredient.getIngredientName());

            EditText amountInput = view.findViewById(R.id.ingredients_number);
            amountInput.setText(String.valueOf(ingredient.getIngredientAmount()));

            String name = appDatabase.measurementDao().idToName(ingredient.getMeasurementId());

            spinnerMeasurement(ingredientsSpinner, name, id);
        } else {
            spinnerMeasurement(ingredientsSpinner, null, id);
        }

        add_ingredients_layout.addView(view);
    }

    public void addDirection(RecipeDirections recipeDirections) {
        View view = LayoutInflater.from(this).inflate(R.layout.directions, directions_layout, false);

        ImageButton clearDirectionButton = view.findViewById(R.id.clear_direction_button);
        clearDirectionButton.setOnClickListener(removeDirection);

        if (recipeDirections != null) {
            EditText directionsInput = view.findViewById(R.id.directions_edit_text);
            directionsInput.setText(recipeDirections.getDirections());
        }
        directions_layout.addView(view);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_view, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    public View.OnClickListener choosePhoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            final int ACTIVITY_SELECT_IMAGE = 1234;
            startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
        }
    };

    public View.OnClickListener ingredients = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (metricRadioButton.isChecked()) {
                measurementSystemId = 1;
                addIngredient(null, measurementSystemId);
                metricRadioButton.setClickable(false);
                usRadioButton.setClickable(false);
                usRadioButton.setAlpha(0.45f);
            } else {
                measurementSystemId = 2;
                addIngredient(null, measurementSystemId);
                metricRadioButton.setClickable(false);
                metricRadioButton.setAlpha(0.45f);
                usRadioButton.setClickable(false);
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1234: {
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getContentResolver().query
                                (selectedImage, filePathColumn, null,
                                        null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            filePath = cursor.getString(columnIndex);
                            cursor.close();
                        }
                    }
                    yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    imageViewPhoto.setImageBitmap(yourSelectedImage);
                }
            }
        }
    }

    public int preparationTime() {
        if (preparationHours.getText().toString().trim().length() == 0) {
            String minuteValue = preparationMinutes.getText().toString();
            String hourValue = "0";
            try {
                return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else if (preparationMinutes.getText().toString().trim().length() == 0) {
            String hourValue = preparationHours.getText().toString();
            String minuteValue = "0";
            try {
                return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else if (preparationHours.getText().toString().trim().length() == 0 && preparationMinutes.getText().toString().trim().length() == 0) {
            String hourValue = "0";
            String minuteValue = "0";
            try {
                return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            String hourValue = preparationHours.getText().toString();
            String minuteValue = preparationMinutes.getText().toString();

            return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
        }
        return 0;
    }

    public int cookingTime() {
        if (cookingHours.getText().toString().trim().length() == 0) {
            String hourValue = "0";
            String minuteValue = cookingMinutes.getText().toString();
            try {
                return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else if (cookingMinutes.getText().toString().trim().length() == 0) {
            String minuteValue = "0";
            String hourValue = cookingHours.getText().toString();
            try {
                return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else if (cookingHours.getText().toString().trim().length() == 0 && cookingMinutes.getText().toString().trim().length() == 0) {
            String minuteValue = "0";
            String hourValue = "0";
            try {
                return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            String hourValue = cookingHours.getText().toString();
            String minuteValue = cookingMinutes.getText().toString();
            return (Integer.parseInt(hourValue) * 60) + Integer.parseInt(minuteValue);
        }
        return 0;
    }

    public void save() {
        Recipe recipe = buildRecipe();

        if (recipeId == -1) {
            int recipeID = (int) appDatabase.recipeDao().insertRecipe(recipe);

            appDatabase.recipeDirectionsDao().insertRecipeDirections(saveAllDirections(recipeID));
            appDatabase.recipeIngredientsDao().insertRecipeIngredients(saveAllIngredients(recipeID));
        } else {
            recipe.setRecipeId(recipeId);

            appDatabase.recipeDao().deleteRecipe(recipe);
            appDatabase.recipeDao().insertRecipe(recipe);
            appDatabase.recipeDirectionsDao().insertRecipeDirections(saveAllDirections(recipeId));
            appDatabase.recipeIngredientsDao().insertRecipeIngredients(saveAllIngredients(recipeId));
        }
    }

    public Recipe buildRecipe() {
        Recipe recipeResult = new Recipe();
        recipeResult.setRecipeName(recipeNameInput.getText().toString());
        recipeResult.setImagePath(filePath);
        recipeResult.setRating(ratingBar.getRating());
        recipeResult.setCategory(categoryItem);
        recipeResult.setPreparationTime(preparationTime());
        recipeResult.setCookingTime(cookingTime());
        if (yield.getText().toString().trim().length() == 0) {
            String yieldCount = "0";
            try {
                recipeResult.setYield(Integer.parseInt(yieldCount));
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            recipeResult.setYield(Integer.parseInt(yield.getText().toString()));
        }
        recipeResult.setComments(comments.getText().toString());
        recipeResult.setSource(source.getText().toString());
        recipeResult.setMeasurementSystemId(measurementSystemId);
        return recipeResult;
    }

    public ArrayList<RecipeIngredient> saveAllIngredients(int recipeId) {
        int childCount = add_ingredients_layout.getChildCount();
        ArrayList<RecipeIngredient> ingredientsList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            RecipeIngredient recipeIngredients = new RecipeIngredient();
            View view = add_ingredients_layout.getChildAt(i);
            EditText nameInput = view.findViewById(R.id.ingredients_edit_text);
            recipeIngredients.setIngredientName(nameInput.getText().toString());
            EditText amountInput = view.findViewById(R.id.ingredients_number);
            recipeIngredients.setIngredientAmount(Double.parseDouble(amountInput.getText().toString()));
            Spinner choice = view.findViewById(R.id.measurement_spinner);
            String measurementName = choice.getSelectedItem().toString();
            recipeIngredients.setMeasurementId(appDatabase.measurementDao().nameToId(measurementName));
            recipeIngredients.setRecipeId(recipeId);
            ingredientsList.add(recipeIngredients);
        }
        return ingredientsList;
    }

    public ArrayList<RecipeDirections> saveAllDirections(int recipeId) {
        int childCount = directions_layout.getChildCount();
        ArrayList<RecipeDirections> directionsList = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            RecipeDirections recipeDirections = new RecipeDirections();
            View view = directions_layout.getChildAt(i);
            EditText directionsInput = view.findViewById(R.id.directions_edit_text);
            recipeDirections.setDirections(directionsInput.getText().toString());
            recipeDirections.setRecipeId(recipeId);
            directionsList.add(recipeDirections);
        }
        return directionsList;
    }

    public void updateRecipe() {
        final ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fullRecipe.observe(this, new Observer<FullRecipe>() {
            @Override
            public void onChanged(@Nullable FullRecipe fullRecipe) {
                if (fullRecipe != null) {
                    recipeNameInput.setText(fullRecipe.recipe.getRecipeName());
                    yourSelectedImage = BitmapFactory.decodeFile(fullRecipe.recipe.getImagePath());
                    imageViewPhoto.setImageBitmap(yourSelectedImage);
                    int system = fullRecipe.recipe.measurementSystemId;
                    if (system == 1) {
                        metricRadioButton.setChecked(true);
                    } else {
                        usRadioButton.setChecked(true);
                    }
                    ratingBar.setRating(fullRecipe.recipe.getRating());
                    categorySpinner.setAdapter(categoryAdapter);
                    String compareCategorySpinner = fullRecipe.recipe.getCategory();
                    int spinnerPosition = categoryAdapter.getPosition(compareCategorySpinner);
                    categorySpinner.setSelection(spinnerPosition);
                    preparationHours.setText(String.valueOf(fullRecipe.recipe.getPreparationTime() / 60));
                    preparationMinutes.setText(String.valueOf(fullRecipe.recipe.getPreparationTime() % 60));
                    cookingHours.setText(String.valueOf(fullRecipe.recipe.getCookingTime() / 60));
                    cookingMinutes.setText(String.valueOf(fullRecipe.recipe.getCookingTime() % 60));
                    yield.setText(String.valueOf(fullRecipe.recipe.getYield()));
                    comments.setText(fullRecipe.recipe.getComments());
                    source.setText(fullRecipe.recipe.getSource());
                    filePath = fullRecipe.recipe.getImagePath();
                    int id = fullRecipe.recipe.getMeasurementSystemId();
                    for (RecipeIngredient ingredient : fullRecipe.recipeIngredient) {
                        addIngredient(ingredient, id);
                    }
                    for (RecipeDirections directions : fullRecipe.recipeDirections) {
                        addDirection(directions);
                    }
                }
            }
        });
    }
}