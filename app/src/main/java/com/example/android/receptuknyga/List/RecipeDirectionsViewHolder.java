package com.example.android.receptuknyga.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.receptuknyga.R;
import com.example.android.receptuknyga.RecipeDirections;


class RecipeDirectionsViewHolder extends RecyclerView.ViewHolder {

    private final TextView directionsRecyclerView;
    private TextView numberDirections;

    RecipeDirectionsViewHolder(View itemView) {
        super(itemView);
        directionsRecyclerView = itemView.findViewById(R.id.directions_recyclerview);
        numberDirections = itemView.findViewById(R.id.number_direction);
    }

    void bind(final RecipeDirections recipeDirections) {
        if (recipeDirections != null) {
            numberDirections.setText(String.valueOf(recipeDirections.getDirectionsNumber()));
            directionsRecyclerView.setText(recipeDirections.getDirections());
        }
    }
}