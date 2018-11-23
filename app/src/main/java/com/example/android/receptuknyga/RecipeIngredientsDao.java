package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeIngredientsDao {

    @Insert
    void insertRecipeIngredients(List<RecipeIngredient> recipeIngredients);



}
