package com.example.android.receptuknyga;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

@Dao
public interface RecipeIngredientsDao {

    @Insert
    void insertRecipeIngredients(List<RecipeIngredient> recipeIngredients);
}
