package com.example.korg.bakingapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_STEPS;
import static com.example.korg.bakingapp.RecipesAdapter.recipeCard;
import static com.example.korg.bakingapp.RecipesAdapter.recipeNameFragment;


public class RecipeNameDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ClickHandler {

    private static final int LOADER_ID = 2;
    private static final int GRID_COLS_LAND = 3;
    private static final int GRID_COLS_PORT = 1;
    private int recipeId;
    private static final String ID = "recipe_id";
    private RecipesAdapter recipesAdapter;
    private Cursor data;
    private RecyclerView recView;
    private boolean isTablet;

    public RecipeNameDetailsFragment() {
    }

    public static RecipeNameDetailsFragment newInstance(int id) {
        RecipeNameDetailsFragment fragment = new RecipeNameDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeId = getArguments().getInt(ID, 0);
        System.out.println("@@@@@id on new frag = "+recipeId);
        getActivity().getLoaderManager().restartLoader(LOADER_ID, null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_name_details, container, false);

        isTablet = getResources().getBoolean(R.bool.isTablet);
        recView = rootView.findViewById(R.id.recview);

        RecyclerView.LayoutManager layout;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT || isTablet)
            layout = new GridLayoutManager(getActivity(), GRID_COLS_PORT);
        else
            layout = new GridLayoutManager(getActivity(), GRID_COLS_LAND);

        recView.setLayoutManager(layout);
        recipesAdapter = new RecipesAdapter(getActivity(), data, recipeNameFragment, recipeCard);
        recView.invalidate();

        recView.setAdapter(recipesAdapter);


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = COLUMN_STEPS_RECIPE_ID.concat("=?");
        String[] selectionArgs = {String.valueOf(recipeId)};

        return new CursorLoader(getActivity(), CONTENT_URI_STEPS, null,
                selection, selectionArgs, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.data = data;
        if (recipesAdapter != null) {
            recipesAdapter.replaceData(data);
            recipesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (recipesAdapter != null) {
            recipesAdapter.replaceData(null);
            recipesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void itemChanged() {
        recipesAdapter.notifyDataSetChanged();
    }
}
