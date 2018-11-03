package com.example.android.receptuknyga;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    PagerAdapter(FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                RecipeMainInfoFragment recipeMainInfoFragment = new RecipeMainInfoFragment();
                return recipeMainInfoFragment;
            }
            case 1: {
                RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
                return recipeIngredientsFragment;
            }
            case 2: {
                RecipeDirectionsFragment recipeDirectionsFragment = new RecipeDirectionsFragment();
                return recipeDirectionsFragment;
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}