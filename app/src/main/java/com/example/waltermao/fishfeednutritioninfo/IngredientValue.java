package com.example.waltermao.fishfeednutritioninfo;

import android.util.Log;

/**
 * Created by waltermao on 2018-02-10.
 */

public enum IngredientValue {

    CRUDE_PROTEIN("Crude  Protein(%)", 37, true),
    TOTAL_CHO("Total CHO(%)", 10, false),
    ARGININE("Arginine(%)", 1.59, true),
    SODIUM("Sodium(%)", 1.5, true),
    IODINE("Iodine(mg/kg)", 4.0, false),
    CHLORINE("Chlorine(%)", 2.0, false),
    VITAMIN_A("Vitamin A(IU/kg)", 5.6, true),
    VITAMIN_C("Vitamin C(mg/kg)", 2.0, true),
    VITAMIN_K("Vitamin K(mg/kg)", 6.0, true),
    CALCIUM("Calcium(%)", 3.0, false),
    IRON("Iron(mg/kg)", 5.0, true);

    private static String[] queryDBColNames;
    private static final String LOG_TAG = IngredientValue.class.getSimpleName();

    private double mThreshold;
    private String mDBCol;
    private boolean mGreaterThan;

    IngredientValue(String dbCol, double threshold, boolean greaterThan) {
        mThreshold = threshold;
        mDBCol = dbCol;
        mGreaterThan = greaterThan;
    }

    public String getDBCol() { return mDBCol; }

    public boolean getGreaterThan() {
        return mGreaterThan;
    }

    public double getThreshold() {
        return mThreshold;
    }

    public static String[] getThresholds() {
        String[] thresh = new String[values().length];
        for(int i = 0; i < values().length; ++i) {
            thresh[i] = String.valueOf(values()[i].mThreshold);
        }
        return thresh;
    }

    public static String[] getActualColNames() {
        String[] names = new String[values().length];
        for(int i = 0; i < values().length; ++i) {
            names[i] = values()[i].getDBCol();
        }
        return names;
    }

    private static void initQueryColNames() {
        if(queryDBColNames != null) {
            return;
        }
        queryDBColNames = new String[IngredientValue.values().length];
        for(int i = 0; i < IngredientValue.values().length; ++i) {
            queryDBColNames[i] = "[" + IngredientValue.values()[i].getDBCol() + "]";
        }
    }

    public static String[] getQueryDBColNames() {
        if(queryDBColNames == null) {
            initQueryColNames();
        }
        return queryDBColNames;
    }

}
