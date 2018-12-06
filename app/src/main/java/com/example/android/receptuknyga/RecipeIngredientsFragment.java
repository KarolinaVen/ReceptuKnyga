package com.example.android.receptuknyga;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.receptuknyga.List.RecipeIngredientListAdapter;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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
    Spinner panSpinner2;
    String panItem;
    String panDiameter;
    String panHeight;
    String panLength;
    String panBreadth;
    TextView pan;
    String panItem2;
    EditText diameter2;
    EditText height2;
    EditText length2;
    EditText breadth2;
    TextView diameter;
    TextView diameterText;
    TextView height;
    TextView heightText;
    TextView length;
    TextView lengthText;
    TextView breadth;
    TextView breadthText;
    TextView yieldCount;
    LinearLayout panConversionLayout;

    double savedYield;

    ImageButton calculation;
    ImageButton subtract;
    ImageButton add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_recipe_ingredients_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDatabase = AppDatabase.getInstance(view.getContext());

        panConversionLayout = view.findViewById(R.id.pan_conversion_layout);

        calculation = view.findViewById(R.id.calculation);
        subtract = view.findViewById(R.id.subtract);
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double yield = Double.parseDouble(yieldCount.getText().toString());
                if (yield > 1.0) {
                    yield--;
                    String amount = new DecimalFormat("#0.##").format(yield);
                    yieldCount.setText(amount);
                    calculateYield(yield);
                    savedYield--;
                }
            }
        });

        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double yield = Double.parseDouble(yieldCount.getText().toString());
                yield++;
                String amount = new DecimalFormat("#0.##").format(yield);
                yieldCount.setText(amount);
                calculateYield(yield);
                savedYield++;
            }
        });

        diameter = view.findViewById(R.id.diameter);
        diameterText = view.findViewById(R.id.diameter_text);
        height = view.findViewById(R.id.height);
        heightText = view.findViewById(R.id.height_text);
        length = view.findViewById(R.id.length);
        lengthText = view.findViewById(R.id.length_text);
        breadth = view.findViewById(R.id.breadth);
        breadthText = view.findViewById(R.id.breadth_text);
        diameter2 = view.findViewById(R.id.diameter2);
        height2 = view.findViewById(R.id.height2);
        length2 = view.findViewById(R.id.length2);
        breadth2 = view.findViewById(R.id.breadth2);
        radioGroup = view.findViewById(R.id.radio_group);
        metricSystemFragment = view.findViewById(R.id.metric_system_fragment);
        usSystemFragment = view.findViewById(R.id.us_system_fragment);
        recyclerView = view.findViewById(R.id.recipe_ingredient_recycler);
        pan = view.findViewById(R.id.pan);
        yieldCount = view.findViewById(R.id.yield_count);

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
                    ((TextView) parent.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#6E6E6E"));
                    ((TextView) parent.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    if (panItem2.equals("Apvali forma")) {
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

    public void calculateYield(double yield) {
        List<RecipeIngredient> ingredients = fullRecipeLiveData.getValue().recipeIngredient;
        List<RecipeIngredient> calculateIngredients = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            calculateIngredients.add(recalculateYield(ingredients.get(i), yield));
        }
        adapter.setRecipes(calculateIngredients);
    }

    public RecipeIngredient recalculateYield(RecipeIngredient recipeIngredient, double yield) {

        double ingredient = recipeIngredient.getIngredientAmount();
        double ingredientAmount = (ingredient / savedYield) * yield;

        recipeIngredient.setIngredientAmount(ingredientAmount);

        return recipeIngredient;
    }

    public RecipeIngredient calculateIngredient(RecipeIngredient recipeIngredient, int systemId) {
        String panDiameter2 = diameter2.getText().toString();
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

        if (panItem.equals("Apvali forma") && panItem2.equals("Apvali forma")) {
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
        } else if (panItem.equals("Apvali forma") && panItem2.equals("Kvadratinė forma")) {
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
        } else if (panItem.equals("Kvadratinė forma") && panItem2.equals("Apvali forma")) {
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
        if (panDiameter.trim().length() == 0 || panHeight.trim().length() == 0) {
            return 0.0;
        } else {
            return 3.14 * ((Double.valueOf(panDiameter) / 2) * (Double.valueOf(panDiameter) / 2)) * Double.valueOf(panHeight);
        }
    }

    public double squareVolume(String panHeight, String panLength, String panBreadth) {
        if (panHeight.trim().length() == 0 || panLength.trim().length() == 0 || panBreadth.trim().length() == 0) {
            return 0.0;
        } else {
            return Double.valueOf(panLength) * Double.valueOf(panBreadth) * Double.valueOf(panHeight);
        }
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

                    if (fullRecipe.recipe.getPan() != null) {
                        if (fullRecipe.recipe.getPan().equals("Apvali forma")) {
                            pan.setText(fullRecipe.recipe.getPan());
                            panDiameter = String.valueOf(fullRecipe.recipe.getDiameter());
                            panHeight = String.valueOf(fullRecipe.recipe.getHeight());
                            panItem = fullRecipe.recipe.getPan();
                            diameterText.setVisibility(View.VISIBLE);
                            diameter.setVisibility(View.VISIBLE);
                            height.setVisibility(View.VISIBLE);
                            heightText.setVisibility(View.VISIBLE);
                            height.setText(String.valueOf(fullRecipe.recipe.getHeight()));
                            diameter.setText(String.valueOf(fullRecipe.recipe.getDiameter()));
                        } else {
                            pan.setText(fullRecipe.recipe.getPan());
                            panHeight = String.valueOf(fullRecipe.recipe.getHeight());
                            panLength = String.valueOf(fullRecipe.recipe.getLength());
                            panBreadth = String.valueOf(fullRecipe.recipe.getBreadth());
                            height.setVisibility(View.VISIBLE);
                            heightText.setVisibility(View.VISIBLE);
                            length.setVisibility(View.VISIBLE);
                            lengthText.setVisibility(View.VISIBLE);
                            breadth.setVisibility(View.VISIBLE);
                            breadthText.setVisibility(View.VISIBLE);
                            breadth.setText(String.valueOf(fullRecipe.recipe.getBreadth()));
                            height.setText(String.valueOf(fullRecipe.recipe.getHeight()));
                            length.setText(String.valueOf(fullRecipe.recipe.getLength()));
                        }

                    } else {
                        panConversionLayout.setVisibility(View.GONE);
                    }

                    yieldCount.setText(String.valueOf(fullRecipe.recipe.getYield()));

                    savedYield = fullRecipe.recipe.getYield();

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