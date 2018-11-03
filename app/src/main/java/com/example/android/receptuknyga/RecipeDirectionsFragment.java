package com.example.android.receptuknyga;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.receptuknyga.List.RecipeDirectionsListAdapter;

public class RecipeDirectionsFragment extends Fragment {

    LiveData<FullRecipe> fullRecipeLiveData;
    RecyclerView recyclerView;
    RecipeDirectionsListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_recipe_directions_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recipeDirectionsRecycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecipeInfoActivity activity = (RecipeInfoActivity) getActivity();
        if (activity != null) {
            this.fullRecipeLiveData = activity.getFullRecipe();
        }

        adapter = new RecipeDirectionsListAdapter(getContext());

        fullRecipeLiveData.observe(this, new Observer<FullRecipe>() {
            @Override
            public void onChanged(@Nullable FullRecipe fullRecipe) {
                if (fullRecipe != null) {
                    adapter.setRecipes(fullRecipe.recipeDirections);
                }
            }
        });
    }
}