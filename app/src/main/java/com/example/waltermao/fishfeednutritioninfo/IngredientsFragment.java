package com.example.waltermao.fishfeednutritioninfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by waltermao on 2018-02-10.
 */

public class IngredientsFragment extends BaseDisplayFragment {

    private static final String LOG_TAG = IngredientsFragment.class.getSimpleName();
    private static final int INGREDIENT_SUGGESTIONS_LOADER_ID = 22;
    private static final int INGREDIENT_LOADER_ID = 19;

    private static String[] ingTableHeaders = {"Property","Limit","Actual","Result"};

    private Ingredient mIngredient;

    public static IngredientsFragment createFragment() {
        return new IngredientsFragment();
    }

    @Override
    List<SearchSuggestion> queryForSearchSuggestions(final String searchTerm) {

        final List<SearchSuggestion> searchSuggestions = new ArrayList<>();

        getLoaderManager().restartLoader(INGREDIENT_SUGGESTIONS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<IngredientSearchSuggestion>>() {
            @Override
            public Loader<List<IngredientSearchSuggestion>> onCreateLoader(int id, Bundle args) {
                return new IngredientsLoader(getContext(),searchTerm);
            }

            @Override
            public void onLoadFinished(Loader<List<IngredientSearchSuggestion>> loader, List<IngredientSearchSuggestion> data) {
                updateSuggestions(data);
            }

            @Override
            public void onLoaderReset(Loader<List<IngredientSearchSuggestion>> loader) {

            }
        });

        return searchSuggestions;
    }

    @Override
    void onSuggestionSelected(SearchSuggestion suggestion) {
        if(!(suggestion instanceof IngredientSearchSuggestion)) {
            Log.e(LOG_TAG,"the suggestion is not an instance of IngredientSearchSuggestion");
            return;
        }
        final IngredientSearchSuggestion ingredientSearchSuggestion = (IngredientSearchSuggestion) suggestion;
        getLoaderManager().restartLoader(INGREDIENT_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Ingredient>() {
            @Override
            public Loader<Ingredient> onCreateLoader(int id, Bundle args) {
                return new SingleIngredientLoader(getContext(),ingredientSearchSuggestion.getIngCode());
            }

            @Override
            public void onLoadFinished(Loader<Ingredient> loader, Ingredient data) {
                TableView tableView = getTableView();
                tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),ingTableHeaders));

                String[][] tableData = generateTableData(data);

                tableView.setDataAdapter(new SimpleTableDataAdapter(getContext(), tableData));
            }

            @Override
            public void onLoaderReset(Loader<Ingredient> loader) {

            }
        });

    }

    private String[][] generateTableData(Ingredient ingredient) {
        if(ingredient == null) {
            Log.d(LOG_TAG,"ingredient is null. cannot generate data");
            return new String[0][0];
        }
        String[][] data = new String[IngredientValue.values().length][4];

        for(int i = 0; i < IngredientValue.values().length; ++i) {
            data[i][0] = IngredientValue.getActualColNames()[i];
            data[i][1] = IngredientValue.getThresholds()[i];
            data[i][2] = ingredient.getIngMapValues()[i];
            data[i][3] = "";
        }

        return data;
    }


    // for loading a single ingredient to be displayed
    private static class SingleIngredientLoader extends AsyncTaskLoader<Ingredient> {

        private long mIngCode;

        public SingleIngredientLoader(Context context, long ingCode) {
            super(context);
            mIngCode = ingCode;
        }

        @Override
        public Ingredient loadInBackground() {
            IngredientDBAdapter dbAdapter = new IngredientDBAdapter(getContext());
            dbAdapter.createDatabase();
            dbAdapter.open();
            Ingredient ing = dbAdapter.getSingleIngredient(mIngCode);
            dbAdapter.close();
            return ing;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }

    // for loading search suggestions
    private static class IngredientsLoader extends AsyncTaskLoader<List<IngredientSearchSuggestion>> {

        private String mSearchTerm;

        public IngredientsLoader(Context context, String searchTerm) {
            super(context);
            onContentChanged();
            mSearchTerm = searchTerm;
        }

        @Override
        public List<IngredientSearchSuggestion> loadInBackground() {

            IngredientDBAdapter dbAdapter = new IngredientDBAdapter(getContext());
            dbAdapter.createDatabase();
            dbAdapter.open();

            List<IngredientSearchSuggestion> suggestions = dbAdapter.getSearchSuggestions(mSearchTerm);
            dbAdapter.close();
            return suggestions;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }

}
