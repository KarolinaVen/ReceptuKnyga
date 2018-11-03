package com.example.android.receptuknyga.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.receptuknyga.R;
import com.example.android.receptuknyga.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private final LayoutInflater mInflater;
    private List<Recipe> mRecipe;

    public RecipeListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(mRecipe.get(position));
    }

    public void setRecipes(List<Recipe> recipes) {
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