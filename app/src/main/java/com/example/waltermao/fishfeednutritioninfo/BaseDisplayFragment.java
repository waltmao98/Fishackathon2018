package com.example.waltermao.fishfeednutritioninfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

/**
 * Created by waltermao on 2018-02-10.
 */

public abstract class BaseDisplayFragment extends Fragment {

    private static final String LOG_TAG = BaseDisplayFragment.class.getSimpleName();

    private FloatingSearchView mSearchView;
    private TableView mTableView;
    private FrameLayout mTableContainer;

    private static final String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
            { "and", "a", "second", "test" } };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ingredients,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchView = view.findViewById(R.id.floating_search_view);

        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) focusSearchBar();
                else focusTable();
            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                focusSearchBar();
                if(newQuery != null && !newQuery.isEmpty()) {
                    queryForSearchSuggestions(newQuery);
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                focusTable();
                onSuggestionSelected(searchSuggestion);
            }

            @Override
            public void onSearchAction(String currentQuery) {
            }
        });

        mTableContainer = view.findViewById(R.id.table_container);

        mTableView = view.findViewById(R.id.tableView);
        mTableView.setColumnCount(4);
        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 1);
        columnModel.setColumnWeight(3, 1);
        mTableView.setColumnModel(columnModel);
    }

    private void focusSearchBar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mSearchView.setLayoutParams(params);
        mTableContainer.setVisibility(View.GONE);
    }

    private void focusTable() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
        mSearchView.setLayoutParams(params);
        mSearchView.swapSuggestions(new ArrayList<SearchSuggestion>());
        mSearchView.setDismissFocusOnItemSelection(true);
        mTableContainer.setVisibility(View.VISIBLE);
    }

    abstract List<SearchSuggestion> queryForSearchSuggestions(String searchTerm);

    void updateSuggestions(List<? extends SearchSuggestion> suggestions) {
        mSearchView.swapSuggestions(suggestions);
    }

    abstract void onSuggestionSelected(SearchSuggestion suggestion); // load the selected info

    public TableView getTableView() {return mTableView;}

    FloatingSearchView getSearchView() { return mSearchView; }

}
