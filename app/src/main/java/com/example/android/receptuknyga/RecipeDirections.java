package com.example.android.receptuknyga;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey(entity = Recipe.class, parentColumns = "recipeId", childColumns = "recipeId", onDelete = ForeignKey.CASCADE))
public class RecipeDirections {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int recipeDirectionsId;
    private String directions;
    private int recipeId;
    int directionsNumber;

    public int getDirectionsNumber() {
        return directionsNumber;
    }

    public void setDirectionsNumber(int directionsNumber) {
        this.directionsNumber = directionsNumber;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    public int getRecipeDirectionsId() {
        return recipeDirectionsId;
    }

    public void setRecipeDirectionsId(@NonNull int recipeDirectionsId) {
        this.recipeDirectionsId = recipeDirectionsId;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }
}
