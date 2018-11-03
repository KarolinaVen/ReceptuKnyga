package com.example.android.receptuknyga.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.receptuknyga.AppDatabase;
import com.example.android.receptuknyga.R;
import com.example.android.receptuknyga.RecipeIngredient;

import java.text.DecimalFormat;

class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;
    private final TextView numberOfIngredientsRecyclerView;
    private final TextView ingredientsMeasurementRecyclerView;
    private final AppDatabase appDatabase;

    RecipeIngredientViewHolder(View itemView) {
        super(itemView);

        appDatabase = AppDatabase.getInstance(itemView.getContext());

        textView = itemView.findViewById(R.id.ingredients_recyclerview);
        numberOfIngredientsRecyclerView = itemView.findViewById(R.id.number_of_ingredients_recyclerview);
        ingredientsMeasurementRecyclerView = itemView.findViewById(R.id.ingredients_measurement_recyclerview);
    }

    void bind(final RecipeIngredient recipeIngredient) {
        if (recipeIngredient != null) {
            textView.setText(recipeIngredient.getIngredientName());
            String amount = new DecimalFormat("#0.##").format(recipeIngredient.getIngredientAmount());
            numberOfIngredientsRecyclerView.setText(amount);
            String name = appDatabase.measurementDao().idToName(recipeIngredient.getMeasurementId());
            ingredientsMeasurementRecyclerView.setText(name);
        }
    }
}