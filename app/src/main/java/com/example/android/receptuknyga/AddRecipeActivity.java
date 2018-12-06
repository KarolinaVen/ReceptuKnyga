package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
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
    EditText ingredientsNumber;
    EditText panDiameter;
    EditText panHeight;
    EditText panLength;
    EditText panBreadth;
    String filePath;
    String panItem;
    String categoryItem;
    Bitmap yourSelectedImage;
    RatingBar ratingBar;
    AppDatabase appDatabase;
    ImageView imageViewPhoto;
    ImageButton addDirectionButton;
    ImageButton addIngredientButton;
    Spinner categorySpinner;
    Spinner panSpinner;
    LiveData<FullRecipe> fullRecipe;
    RadioButton metricRadioButton;
    RadioButton usRadioButton;
    Toolbar myToolbar;

    int recipeId;
    int measurementSystemId = 1;

    private Toast toast;
    private long lastBackPressTime = 0;

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
        directions_layout = findViewById(R.id.directions_layout);
        add_ingredients_layout = findViewById(R.id.add_ingredients_layout);
        ingredientsNumber = findViewById(R.id.ingredients_number);
        panBreadth = findViewById(R.id.pan_breadth);
        panDiameter = findViewById(R.id.pan_diameter);
        panHeight = findViewById(R.id.pan_height);
        panLength = findViewById(R.id.pan_length);

        recipeNameInput = findViewById(R.id.recipe_title2);
        enterButton(recipeNameInput);

        comments = findViewById(R.id.comments);
        enterButton(comments);

        source = findViewById(R.id.source);
        enterButton(source);

        yield = findViewById(R.id.yield);
        yield.setTransformationMethod(null);

        preparationHours = findViewById(R.id.preparationHours);
        preparationHours.setTransformationMethod(null);

        preparationMinutes = findViewById(R.id.preparationMinutes);
        preparationMinutes.setTransformationMethod(null);

        cookingHours = findViewById(R.id.cookingHours);
        cookingHours.setTransformationMethod(null);

        cookingMinutes = findViewById(R.id.cookingMinutes);
        cookingMinutes.setTransformationMethod(null);

        ratingBar = findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        imageViewPhoto = findViewById(R.id.add_photo_id);
        imageViewPhoto.setOnClickListener(choosePhoto);

        addIngredientButton = findViewById(R.id.add_ingredient_button);
        addIngredientButton.setOnClickListener(ingredients);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            myToolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }

        categorySpinner = findViewById(R.id.category);
        categorySpinner();

        panSpinner = findViewById(R.id.pan_spinner);
        panSpinner();

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

    public String normalized(String text){
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
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
                    ((TextView) parent.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#6E6E6E"));
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void panSpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.pan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panSpinner.setPrompt("Forma");
        panSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter, R.layout.pan_spinner, this));
        panSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    panItem = parent.getItemAtPosition(position).toString();
                    ((TextView) parent.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#6E6E6E"));
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    if (panItem.equals("Apvali forma")) {
                        panDiameter.setVisibility(View.VISIBLE);
                        panHeight.setVisibility(View.VISIBLE);
                        panLength.setVisibility(View.GONE);
                        panBreadth.setVisibility(View.GONE);
                    } else {
                        panLength.setVisibility(View.VISIBLE);
                        panHeight.setVisibility(View.VISIBLE);
                        panBreadth.setVisibility(View.VISIBLE);
                        panDiameter.setVisibility(View.GONE);
                    }
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

    public void enterButton(final EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    editText.setSelection(0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void addIngredient(RecipeIngredient ingredient, int id) {
        Log.i(TAG, "addIngirient called with: " + ingredient);

        View view = LayoutInflater.from(this).inflate(R.layout.ingridients, add_ingredients_layout, false);

        Spinner ingredientsSpinner = view.findViewById(R.id.measurement_spinner);

        ImageButton clearIngredientButton = view.findViewById(R.id.clear_ingredient_button);
        clearIngredientButton.setOnClickListener(remove);

        EditText nameInput = view.findViewById(R.id.ingredients_edit_text);

        if (ingredient != null) {

            nameInput.setText(ingredient.getIngredientName());

            EditText amountInput = view.findViewById(R.id.ingredients_number);
            amountInput.setText(String.valueOf(ingredient.getIngredientAmount()));

            String name = appDatabase.measurementDao().idToName(ingredient.getMeasurementId());

            spinnerMeasurement(ingredientsSpinner, name, id);
        } else {
            spinnerMeasurement(ingredientsSpinner, null, id);
        }
        add_ingredients_layout.addView(view);
        enterButton(nameInput);
    }

    public void addDirection(RecipeDirections recipeDirections) {
        View view = LayoutInflater.from(this).inflate(R.layout.directions, directions_layout, false);

        ImageButton clearDirectionButton = view.findViewById(R.id.clear_direction_button);
        clearDirectionButton.setOnClickListener(removeDirection);

        EditText directionsInput = view.findViewById(R.id.directions_edit_text);

        if (recipeDirections != null) {

            directionsInput.setText(recipeDirections.getDirections());
        }
        directions_layout.addView(view);
        enterButton(directionsInput);
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
        } else if (preparationHours.getText().toString().trim().length() == 0
                && preparationMinutes.getText().toString().trim().length() == 0) {
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
        } else if (cookingHours.getText().toString().trim().length() == 0
                && cookingMinutes.getText().toString().trim().length() == 0) {
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
        String recipeNameASCII = Normalizer.normalize(recipeNameInput.getText().toString(), Normalizer.Form.NFD).
                replaceAll("[^\\p{ASCII}]", "");
        recipeResult.setRecipeNameASCII(recipeNameASCII);
        recipeResult.setImagePath(filePath);
        recipeResult.setRating(ratingBar.getRating());
        recipeResult.setCategory(categoryItem);
        recipeResult.setPreparationTime(preparationTime());
        recipeResult.setCookingTime(cookingTime());
        recipeResult.setComments(comments.getText().toString());
        recipeResult.setSource(source.getText().toString());

        if (metricRadioButton.isChecked()) {
            recipeResult.setMeasurementSystemId(1);
        } else {
            recipeResult.setMeasurementSystemId(2);
        }
        recipeResult.setPan(panItem);

        if (panLength.getText().toString().trim().length() == 0) {
            String panCount = "0";
            try {
                recipeResult.setLength(Integer.parseInt(panCount));
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            recipeResult.setLength(Integer.parseInt(panLength.getText().toString()));
        }

        if (panHeight.getText().toString().trim().length() == 0) {
            String panCount = "0";
            try {
                recipeResult.setHeight(Integer.parseInt(panCount));
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            recipeResult.setHeight(Integer.parseInt(panHeight.getText().toString()));
        }

        if (panDiameter.getText().toString().trim().length() == 0) {
            String panCount = "0";
            try {
                recipeResult.setDiameter(Integer.parseInt(panCount));
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            recipeResult.setDiameter(Integer.parseInt(panDiameter.getText().toString()));
        }

        if (panBreadth.getText().toString().trim().length() == 0) {
            String panCount = "0";
            try {
                recipeResult.setBreadth(Integer.parseInt(panCount));
            } catch (NumberFormatException ex) {
                System.err.println("Wrong input");
            }
        } else {
            recipeResult.setBreadth(Integer.parseInt(panBreadth.getText().toString()));
        }

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

        return recipeResult;
    }

    public ArrayList<RecipeIngredient> saveAllIngredients(int recipeId) {
        int childCount = add_ingredients_layout.getChildCount();
        int number = 1;

        ArrayList<RecipeIngredient> ingredientsList = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            RecipeIngredient recipeIngredients = new RecipeIngredient();

            View view = add_ingredients_layout.getChildAt(i);

            EditText nameInput = view.findViewById(R.id.ingredients_edit_text);
            Spinner choice = view.findViewById(R.id.measurement_spinner);
            EditText amountInput = view.findViewById(R.id.ingredients_number);

            if (amountInput.getText().toString().trim().length() == 0
                    && nameInput.getText().toString().trim().length() == 0
                    && choice.getSelectedItem() == null) {
                continue;
            } else {
                recipeIngredients.setNumber(number);
                number++;
                if (amountInput.getText().toString().trim().length() == 0) {
                    Double amount = 0.0;
                    recipeIngredients.setIngredientAmount(amount);
                } else {
                    recipeIngredients.setIngredientAmount(Double.parseDouble(amountInput.getText().toString()));
                }

                if (nameInput.getText().toString().trim().length() == 0) {
                    String name = "-";
                    recipeIngredients.setIngredientName(name);
                } else {
                    recipeIngredients.setIngredientName(nameInput.getText().toString());
                    String ingredientASCII = Normalizer.normalize(nameInput.getText().toString(), Normalizer.Form.NFD).
                            replaceAll("[^\\p{ASCII}]", "");
                    recipeIngredients.setIngredientNameASCII(ingredientASCII);
                }

                if (choice.getSelectedItem() == null) {
                    String measurement = "vnt.";
                    recipeIngredients.setMeasurementId(appDatabase.measurementDao().nameToId(measurement));
                } else {
                    String measurementName = choice.getSelectedItem().toString();
                    recipeIngredients.setMeasurementId(appDatabase.measurementDao().nameToId(measurementName));
                }
            }
            recipeIngredients.setRecipeId(recipeId);
            ingredientsList.add(recipeIngredients);
        }
        return ingredientsList;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Spauskite dar kartą, kad grįžtumėte", Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }

    public ArrayList<RecipeDirections> saveAllDirections(int recipeId) {
        int childCount = directions_layout.getChildCount();

        int number = 1;
        ArrayList<RecipeDirections> directionsList = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            RecipeDirections recipeDirections = new RecipeDirections();

            View view = directions_layout.getChildAt(i);

            EditText directionsInput = view.findViewById(R.id.directions_edit_text);

            if (directionsInput.getText().toString().trim().length() == 0) {
                continue;
            } else {
                recipeDirections.setDirectionsNumber(number);
                number++;
                recipeDirections.setDirections(directionsInput.getText().toString());
            }
            recipeDirections.setRecipeId(recipeId);
            directionsList.add(recipeDirections);
        }
        return directionsList;
    }

    public void updateRecipe() {
        final ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.pan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fullRecipe.observe(this, new Observer<FullRecipe>() {
            @Override
            public void onChanged(@Nullable FullRecipe fullRecipe) {
                if (fullRecipe != null) {
                    recipeNameInput.setText(fullRecipe.recipe.getRecipeName());
                    yourSelectedImage = BitmapFactory.decodeFile(fullRecipe.recipe.getImagePath());
                    imageViewPhoto.setImageBitmap(yourSelectedImage);

                    int id = fullRecipe.recipe.getMeasurementSystemId();

                    ratingBar.setRating(fullRecipe.recipe.getRating());
                    categorySpinner.setAdapter(categoryAdapter);
                    panSpinner.setAdapter(adapter);

                    String compareCategorySpinner = fullRecipe.recipe.getCategory();
                    String comparePanSpinner = fullRecipe.recipe.getPan();

                    int spinnerPosition = categoryAdapter.getPosition(compareCategorySpinner);
                    int panSpinnerPosition = adapter.getPosition(comparePanSpinner);

                    categorySpinner.setSelection(spinnerPosition);
                    panSpinner.setSelection(panSpinnerPosition);

                    if(comparePanSpinner == null){
                        panSpinner();
                    } else if (comparePanSpinner.equals("Apvali forma")) {
                        panDiameter.setVisibility(View.VISIBLE);
                        panHeight.setVisibility(View.VISIBLE);
                        panLength.setVisibility(View.GONE);
                        panBreadth.setVisibility(View.GONE);
                        panDiameter.setText(String.valueOf(fullRecipe.recipe.getDiameter()));
                        panHeight.setText(String.valueOf(fullRecipe.recipe.getHeight()));
                    } else if (comparePanSpinner.equals("Kvadratinė forma")) {
                        panLength.setVisibility(View.VISIBLE);
                        panHeight.setVisibility(View.VISIBLE);
                        panBreadth.setVisibility(View.VISIBLE);
                        panDiameter.setVisibility(View.GONE);
                        panHeight.setText(String.valueOf(fullRecipe.recipe.getHeight()));
                        panBreadth.setText(String.valueOf(fullRecipe.recipe.getBreadth()));
                        panLength.setText(String.valueOf(fullRecipe.recipe.getLength()));
                    }

                    preparationHours.setText(String.valueOf(fullRecipe.recipe.getPreparationTime() / 60));
                    preparationMinutes.setText(String.valueOf(fullRecipe.recipe.getPreparationTime() % 60));
                    cookingHours.setText(String.valueOf(fullRecipe.recipe.getCookingTime() / 60));
                    cookingMinutes.setText(String.valueOf(fullRecipe.recipe.getCookingTime() % 60));
                    yield.setText(String.valueOf(fullRecipe.recipe.getYield()));
                    comments.setText(fullRecipe.recipe.getComments());
                    source.setText(fullRecipe.recipe.getSource());
                    filePath = fullRecipe.recipe.getImagePath();

                    if (id == 1) {
                        metricRadioButton.setChecked(true);
                        metricRadioButton.setClickable(false);
                        usRadioButton.setClickable(false);
                        usRadioButton.setAlpha(0.45f);
                    } else {
                        usRadioButton.setChecked(true);
                        metricRadioButton.setClickable(false);
                        metricRadioButton.setAlpha(0.45f);
                        usRadioButton.setClickable(false);
                    }

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