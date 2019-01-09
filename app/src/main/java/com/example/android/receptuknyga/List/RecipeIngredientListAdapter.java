package com.example.android.receptuknyga.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.receptuknyga.R;
import com.example.android.receptuknyga.RecipeIngredient;

import java.util.List;

public class RecipeIngredientListAdapter extends RecyclerView.Adapter<RecipeIngredientViewHolder> {

    private final LayoutInflater mInflater;
    private List<RecipeIngredient> mRecipe;

    public RecipeIngredientListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recipe_ingredient_recycler_view, parent, false);
        return new RecipeIngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        holder.bind(mRecipe.get(position));
    }

    public void setRecipes(List<RecipeIngredient> recipes) {
        mRecipe = recipes;
        notifyDataSetChanged();
    }

    public List<RecipeIngredient> getRecipes() {
        return mRecipe;
    }

    @Override
    public int getItemCount() {
        if (mRecipe != null)
            return mRecipe.size();
        else return 0;
    }
}