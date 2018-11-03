package com.example.android.receptuknyga.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.receptuknyga.R;
import com.example.android.receptuknyga.RecipeDirections;

import java.util.List;

public class RecipeDirectionsListAdapter extends RecyclerView.Adapter<RecipeDirectionsViewHolder> {

    private final LayoutInflater mInflater;
    private List<RecipeDirections> mRecipe;

    public RecipeDirectionsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public RecipeDirectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recipe_directions_recyclerview, parent, false);
        return new RecipeDirectionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDirectionsViewHolder holder, int position) {
        holder.bind(mRecipe.get(position));
    }

    public void setRecipes(List<RecipeDirections> recipes) {
        mRecipe = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRecipe != null)
            return mRecipe.size();
        else return 0;
    }
}