package com.example.waltermao.fishfeednutritioninfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by waltermao on 2018-02-10.
 */

public class Ingredient {

    private String name;

    public static final String GOOD = "good";
    public static final String BAD = "bad";

    Map<String, Double> mIngValuesMap; // maps each ing value table col name to it's value

    public Ingredient() {
        mIngValuesMap = new HashMap<>();
    }

    public void putIngVal(String ingValDBColName, double val) {
        mIngValuesMap.put(ingValDBColName, val);
    }

    public double getIngVal(String ingValDBColName) {
        return mIngValuesMap.get(ingValDBColName);
    }

    public String[] getIngMapValues() {
        String[] vals = new String[mIngValuesMap.size()];
        int i = 0;
        for (IngredientValue val : IngredientValue.values()) {
            vals[i] = String.valueOf(mIngValuesMap.get(val.getDBCol()));
            ++i;
        }
        return vals;
    }

    public String[] getResults() {
        String[] results = new String[mIngValuesMap.size()];
        int i = 0;
        for (IngredientValue val : IngredientValue.values()) {
            if (val.getGreaterThan()) {
                if (mIngValuesMap.get(val.getDBCol()) >= val.getThreshold()) {
                    results[i] = GOOD;
                } else {
                    results[i] = BAD;
                }
            } else {
                if (mIngValuesMap.get(val.getDBCol()) <= val.getThreshold()) {
                    results[i] = GOOD;
                } else {
                    results[i] = BAD;
                }
            }
            ++i;
        }
        return results;
    }

    public Map<String, Double> getIngValuesMap() {
        return mIngValuesMap;
    }

    public double getSustainabilityScore() {
        double score = 0;
        for (int i = 0; i < IngredientValue.values().length; ++i) {

        }
        return 0;
    }


}
