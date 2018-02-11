package com.example.waltermao.fishfeednutritioninfo;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by waltermao on 2018-02-10.
 */

@SuppressLint("ParcelCreator")
public class IngredientSearchSuggestion implements SearchSuggestion {

    private String mName; // ingredient name
    private Long mIngCode; // unique id in database

    public IngredientSearchSuggestion(String name, Long ingCode) {
        mName = name;
        mIngCode = ingCode;
    }

    public Long getIngCode() {
        return mIngCode;
    }

    @Override
    public String getBody() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // not used atm
    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
