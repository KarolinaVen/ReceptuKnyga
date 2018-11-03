package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.android.receptuknyga.List.RecipeIngredientListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientsFragment extends Fragment {

    LiveData<FullRecipe> fullRecipeLiveData;
    RecyclerView recyclerView;
    RecipeIngredientListAdapter adapter;
    RadioButton metricSystemFragment;
    RadioButton usSystemFragment;
    AppDatabase appDatabase;
    RadioGroup radioGroup;
    Spinner panSpinner;
    Spinner panSpinner2;
    String panItem;
    String panItem2;
    EditText diameter2;
    EditText height2;
    EditText length2;
    EditText breadth2;
    EditText diameter;
    EditText height;
    EditText length;
    EditText breadth;
    ImageButton calculation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_recipe_ingredients_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDatabase = AppDatabase.getInstance(view.getContext());

        calculation = view.findViewById(R.id.calculation);

        diameter = view.findViewById(R.id.diameter);
        height = view.findViewById(R.id.height);
        length = view.findViewById(R.id.length);
        breadth = view.findViewById(R.id.breadth);
        diameter2 = view.findViewById(R.id.diameter2);
        height2 = view.findViewById(R.id.height2);
        length2 = view.findViewById(R.id.length2);
        breadth2 = view.findViewById(R.id.breadth2);
        radioGroup = view.findViewById(R.id.radio_group);
        metricSystemFragment = view.findViewById(R.id.metric_system_fragment);
        usSystemFragment = view.findViewById(R.id.us_system_fragment);
        recyclerView = view.findViewById(R.id.recipe_ingredient_recycler);

        panSpinner = view.findViewById(R.id.pan_spinner);
        panSpinner();

        panSpinner2 = view.findViewById(R.id.pan_spinner2);
        panSpinner2();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.metric_system_fragment) {
                    changeSystem(MeasurementSystem.ID_METRIC);
                    calculation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calculatePan(MeasurementSystem.ID_METRIC);
                        }
                    });
                } else {
                    changeSystem(MeasurementSystem.ID_US);
                    calculation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            calculatePan(MeasurementSystem.ID_US);
                        }
                    });
                }
            }
        });
    }

    public void panSpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.pan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panSpinner.setPrompt("Forma");
        panSpinner.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter, R.layout.pan_spinner, getContext()));
        panSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    panItem = parent.getItemAtPosition(position).toString();
                    if (panItem.equals("Apvali")) {
                        diameter.setVisibility(View.VISIBLE);
                        height.setVisibility(View.VISIBLE);
                        length.setVisibility(View.GONE);
                        breadth.setVisibility(View.GONE);
                    } else {
                        length.setVisibility(View.VISIBLE);
                        height.setVisibility(View.VISIBLE);
                        breadth.setVisibility(View.VISIBLE);
                        diameter.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void panSpinner2() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.pan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panSpinner2.setPrompt("Forma");
        panSpinner2.setAdapter(new NothingSelectedSpinnerAdapter(
                adapter, R.layout.pan_spinner, getContext()));
        panSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    panItem2 = parent.getItemAtPosition(position).toString();
                    if (panItem2.equals("Apvali")) {
                        diameter2.setVisibility(View.VISIBLE);
                        height2.setVisibility(View.VISIBLE);
                        length2.setVisibility(View.GONE);
                        breadth2.setVisibility(View.GONE);
                    } else {
                        length2.setVisibility(View.VISIBLE);
                        height2.setVisibility(View.VISIBLE);
                        breadth2.setVisibility(View.VISIBLE);
                        diameter2.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void calculatePan(int systemId) {
        List<RecipeIngredient> ingredients = fullRecipeLiveData.getValue().recipeIngredient;
        List<RecipeIngredient> calculateIngredients = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            calculateIngredients.add(calculateIngredient(ingredients.get(i), systemId));
        }
        adapter.setRecipes(calculateIngredients);
    }

    public RecipeIngredient calculateIngredient(RecipeIngredient recipeIngredient, int systemId) {
        String panDiameter = diameter.getText().toString();
        String panDiameter2 = diameter2.getText().toString();
        String panHeight = height.getText().toString();
        String panHeight2 = height2.getText().toString();
        String panLength = length.getText().toString();
        String panLength2 = length2.getText().toString();
        String panBreadth = breadth.getText().toString();
        String panBreadth2 = breadth2.getText().toString();

        RecipeIngredient recipeIngredientCopy = new RecipeIngredient();
        recipeIngredientCopy.setIngredientsId(recipeIngredient.getIngredientsId());
        recipeIngredientCopy.setRecipeId(recipeIngredient.getRecipeId());
        recipeIngredientCopy.setIngredientName(recipeIngredient.getIngredientName());
        recipeIngredientCopy.setIngredientAmount(recipeIngredient.getIngredientAmount());
        recipeIngredientCopy.setMeasurementId(recipeIngredient.getMeasurementId());

        int ingredientSystemId = appDatabase.measurementDao().getMeasurementSystemId(recipeIngredient.getMeasurementId());

        if (panItem.equals("Apvali") && panItem2.equals("Apvali")) {
            double circleVolume = circleVolume(panDiameter, panHeight);
            double circleVolume2 = circleVolume(panDiameter2, panHeight2);
            double scaleFactor = circleVolume2 / circleVolume;
            double ingredient = recipeIngredient.getIngredientAmount();
            double answer = ingredient * scaleFactor;
            recipeIngredientCopy.setIngredientAmount(answer);
            if (ingredientSystemId == systemId) {
                return recipeIngredientCopy;
            } else {
                return Converters.convertIngredient(recipeIngredientCopy);
            }
        } else if (panItem.equals("Apvali") && panItem2.equals("Kvadratinė")) {
            double circleVolume = circleVolume(panDiameter, panHeight);
            double squareVolume2 = squareVolume(panHeight2, panLength2, panBreadth2);
            double scaleFactor = squareVolume2 / circleVolume;
            double ingredient = recipeIngredient.getIngredientAmount();
            double answer = ingredient * scaleFactor;
            recipeIngredientCopy.setIngredientAmount(answer);
            if (ingredientSystemId == systemId) {
                return recipeIngredientCopy;
            } else {
                return Converters.convertIngredient(recipeIngredientCopy);
            }
        } else if (panItem.equals("Kvadratinė") && panItem2.equals("Apvali")) {
            double circleVolume2 = circleVolume(panDiameter2, panHeight2);
            double squareVolume = squareVolume(panHeight, panLength, panBreadth);
            double scaleFactor = circleVolume2 / squareVolume;
            double ingredient = recipeIngredient.getIngredientAmount();
            double answer = ingredient * scaleFactor;
            recipeIngredientCopy.setIngredientAmount(answer);
            if (ingredientSystemId == systemId) {
                return recipeIngredientCopy;
            } else {
                return Converters.convertIngredient(recipeIngredientCopy);
            }
        } else {
            double squareVolume = squareVolume(panHeight, panLength, panBreadth);
            double squareVolume2 = squareVolume(panHeight2, panLength2, panBreadth2);
            double scaleFactor = squareVolume2 / squareVolume;
            double ingredient = recipeIngredient.getIngredientAmount();
            double answer = ingredient * scaleFactor;
            recipeIngredientCopy.setIngredientAmount(answer);
            if (ingredientSystemId == systemId) {
                return recipeIngredientCopy;
            } else {
                return Converters.convertIngredient(recipeIngredientCopy);
            }
        }
    }

    public double circleVolume(String panDiameter, String panHeight) {
        return 3.14 * ((Double.valueOf(panDiameter) / 2) * (Double.valueOf(panDiameter) / 2)) * Double.valueOf(panHeight);
    }

    public double squareVolume(String panHeight, String panLength, String panBreadth) {
        return Double.valueOf(panLength) * Double.valueOf(panBreadth) * Double.valueOf(panHeight);
    }

    @SuppressWarnings("ConstantConditions")
    public void changeSystem(int systemId) {
        List<RecipeIngredient> ingredients = fullRecipeLiveData.getValue().recipeIngredient;
        List<RecipeIngredient> convertedIngredients = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            convertedIngredients.add(convert(ingredients.get(i), systemId));
        }
        adapter.setRecipes(convertedIngredients);
    }

    public RecipeIngredient convert(RecipeIngredient recipeIngredient, int systemId) {
        int ingredientSystemId = appDatabase.measurementDao().getMeasurementSystemId(recipeIngredient.getMeasurementId());
        if (ingredientSystemId == systemId) {
            return recipeIngredient;
        } else {
            return Converters.convertIngredient(recipeIngredient);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeInfoActivity activity = (RecipeInfoActivity) getActivity();

        this.fullRecipeLiveData = activity.getFullRecipe();

        adapter = new RecipeIngredientListAdapter(getContext());

        fullRecipeLiveData.observe(this, new Observer<FullRecipe>() {
            @Override
            public void onChanged(@Nullable final FullRecipe fullRecipe) {
                if (fullRecipe != null) {
                    adapter.setRecipes(fullRecipe.recipeIngredient);

                    final int id = fullRecipe.recipe.getMeasurementSystemId();

                    if (id == MeasurementSystem.ID_METRIC) {
                        metricSystemFragment.setChecked(true);
                        metricSystemFragment.jumpDrawablesToCurrentState();
                    } else {
                        usSystemFragment.setChecked(true);
                        usSystemFragment.jumpDrawablesToCurrentState();
                    }
                }
            }
        });
    }
}