package com.example.android.receptuknyga;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "recipeId", childColumns = "recipeId", onDelete = ForeignKey.CASCADE))
public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ingredientsId;
    private int recipeId;
    private String ingredientName;
    private String ingredientNameASCII;
    private double ingredientAmount;
    private int measurementId;
    private int number;

    public String getIngredientNameASCII() {
        return ingredientNameASCII;
    }

    public void setIngredientNameASCII(String ingredientNameASCII) {
        this.ingredientNameASCII = ingredientNameASCII;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    @NonNull
    public int getIngredientsId() {
        return ingredientsId;
    }

    public void setIngredientsId(@NonNull int ingredientsId) {
        this.ingredientsId = ingredientsId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(double ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "ingredientName='" + ingredientName + '\'' +
                ", ingredientAmount=" + ingredientAmount +
                ", measurementId=" + measurementId +
                '}';
    }
}
