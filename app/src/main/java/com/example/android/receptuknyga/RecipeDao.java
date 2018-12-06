package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insertRecipe(Recipe recipe);

    @Query("SELECT *  FROM Recipe ORDER BY recipeId DESC")
    LiveData<List<Recipe>> allRecipes();

    @Query("SELECT * FROM Recipe WHERE recipeNameASCII LIKE '%' || :recipeNameASCII || '%' GROUP BY recipeName ORDER BY recipeId DESC  ")
    LiveData<List<Recipe>> recipeName(String recipeNameASCII);

    @Query("SELECT * FROM Recipe, RecipeIngredient WHERE Recipe.recipeId = RecipeIngredient.recipeId AND ingredientNameASCII LIKE '%' || :ingredientNameASCII || '%' GROUP BY ingredientName")
    LiveData<List<Recipe>> ingredientName(String ingredientNameASCII);

    @Query("SELECT * FROM Recipe WHERE category = :category")
    LiveData<List<Recipe>> recipesByCategory(String category);

    @Query("SELECT * FROM Recipe WHERE recipeId = :recipeId")
    LiveData<FullRecipe> allRecipeInfo(int recipeId);

    @Query("DELETE FROM Recipe WHERE recipeId = :recipeId")
    void deleteRecipe(int recipeId);

    @Delete
    void deleteRecipe(Recipe recipe);
}
