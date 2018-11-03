package com.example.android.receptuknyga;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class FullRecipe {

    @Embedded
    Recipe recipe;

    @Relation(parentColumn = "recipeId", entityColumn = "recipeId")
    List<RecipeIngredient> recipeIngredient;

    @Relation(parentColumn = "recipeId", entityColumn = "recipeId")
    List<RecipeDirections> recipeDirections;
}
