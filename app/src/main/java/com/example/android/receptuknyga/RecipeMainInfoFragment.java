package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecipeMainInfoFragment extends Fragment {

    LiveData<FullRecipe> fullRecipeLiveData;
    TextView recipeName;
    TextView recipePreparationHour;
    TextView recipePreparationMinute;
    TextView recipeCookingHour;
    TextView recipeCookingMinute;
    TextView yield;
    TextView comment;
    TextView category;
    TextView recipeSource;
    TextView totalHour;
    TextView totalMinute;
    ImageView recipePhoto;
    Bitmap recipePhotoBitmap;
    RatingBar recipeRating;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_recipe_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeName = view.findViewById(R.id.main_fragment_recipe_name);
        recipePhoto = view.findViewById(R.id.main_fragment_photo);
        recipeRating = view.findViewById(R.id.main_fragment_rating);
        recipePreparationHour = view.findViewById(R.id.main_fragment_preparation_hour);
        recipePreparationMinute = view.findViewById(R.id.main_fragment_preparation_minute);
        recipeCookingHour = view.findViewById(R.id.main_fragment_cooking_hour);
        recipeCookingMinute = view.findViewById(R.id.main_fragment_cooking_minute);
        yield = view.findViewById(R.id.main_fragment_yield);
        category = view.findViewById(R.id.main_fragment_category);
        comment = view.findViewById(R.id.main_fragment_comment_text);
        recipeSource = view.findViewById(R.id.main_fragment_source);
        totalHour = view.findViewById(R.id.main_fragment_total_hour);
        totalMinute = view.findViewById(R.id.main_fragment_total_minute);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeInfoActivity activity = (RecipeInfoActivity) getActivity();
        this.fullRecipeLiveData = activity.getFullRecipe();
    }

    @Override
    public void onStart() {
        super.onStart();
        fullRecipeLiveData.observe(this, new Observer<FullRecipe>() {
            @Override
            public void onChanged(@Nullable FullRecipe fullRecipe) {
                if (fullRecipe != null) {
                    recipeName.setText(fullRecipe.recipe.getRecipeName());
                    recipePhotoBitmap = BitmapFactory.decodeFile(fullRecipe.recipe.getImagePath());
                    recipePhoto.setImageBitmap(recipePhotoBitmap);
                    recipeRating.setRating(fullRecipe.recipe.getRating());
                    LayerDrawable stars = (LayerDrawable) recipeRating.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

                    recipePreparationHour.setText(String.valueOf(fullRecipe.recipe.getPreparationTime() / 60));
                    recipePreparationMinute.setText(String.valueOf(fullRecipe.recipe.getPreparationTime() % 60));
                    recipeCookingHour.setText(String.valueOf(fullRecipe.recipe.getCookingTime() / 60));
                    recipeCookingMinute.setText(String.valueOf(fullRecipe.recipe.getCookingTime() % 60));
                    int totalHours = (fullRecipe.recipe.getPreparationTime() + fullRecipe.recipe.getCookingTime()) / 60;
                    int totalMinutes = (fullRecipe.recipe.getPreparationTime() + fullRecipe.recipe.getCookingTime()) % 60;
                    totalHour.setText(String.valueOf(totalHours));
                    totalMinute.setText(String.valueOf(totalMinutes));
                    yield.setText(String.valueOf(fullRecipe.recipe.getYield()));
                    category.setText(fullRecipe.recipe.getCategory());
                    comment.setText(fullRecipe.recipe.getComments());
                    recipeSource.setText(fullRecipe.recipe.getSource());
                }
            }
        });
    }
}