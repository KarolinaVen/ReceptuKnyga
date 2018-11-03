package com.example.android.receptuknyga.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.receptuknyga.R;
import com.example.android.receptuknyga.Recipe;
import com.example.android.receptuknyga.RecipeInfoActivity;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    private final ImageView recipe_image;
    private final RatingBar rating;
    private final TextView recipeName;
    private final TextView recipeCategory;

    RecipeViewHolder(View itemView) {
        super(itemView);
        recipe_image = itemView.findViewById(R.id.recipe_image);
        rating = itemView.findViewById(R.id.rating);
        recipeName = itemView.findViewById(R.id.recipeName);
        recipeCategory = itemView.findViewById(R.id.recipeCategory);
    }

    void bind(final Recipe recipe) {
        if (recipe != null) {
            Bitmap bmImg = BitmapFactory.decodeFile(recipe.getImagePath());

            recipe_image.setImageBitmap(bmImg);
            recipe_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRecipe(recipe.getRecipeId());
                }
            });

            rating.setRating(recipe.getRating());
            recipeName.setText(recipe.getRecipeName());
            recipeCategory.setText(recipe.getCategory());
        }
    }

    private void openRecipe(int recipeId) {
        Activity activity = (Activity) itemView.getContext();
        Intent intent = new Intent(activity, RecipeInfoActivity.class);
        intent.putExtra("id", recipeId);
        activity.startActivity(intent);
    }
}