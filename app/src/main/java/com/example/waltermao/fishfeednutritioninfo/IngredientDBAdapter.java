package com.example.waltermao.fishfeednutritioninfo;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by waltermao on 2018-02-10.
 */

public class IngredientDBAdapter {
    private static final String LOG_TAG = IngredientDBAdapter.class.getSimpleName();

    private static final Integer NUM_RESULTS_LIMIT = 6;

    private final WeakReference<Context> mContextReference;
    private static final String DB_NAME = "ingredients";
    private SQLiteDatabase mDb;
    private FeedIngredientCompositionDBHelper mDbHelper;

    public IngredientDBAdapter(Context context) {
        mContextReference = new WeakReference<Context>(context);
        mDbHelper = new FeedIngredientCompositionDBHelper(mContextReference.get(),DB_NAME);
    }

    public IngredientDBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(LOG_TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public IngredientDBAdapter open() throws SQLException {
        try {
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(LOG_TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public List<IngredientSearchSuggestion> getSearchSuggestions(String searchTerm) {
        List<IngredientSearchSuggestion> suggestions = new ArrayList<>();
        CursorWrapper cursor = queryIngredients(
                new String[]{IngredientsTable.COLS.ING_CODE,IngredientsTable.COLS.NAME},
                IngredientsTable.COLS.NAME + " LIKE ?",
                new String[] {"%" + searchTerm + "%"});
        try {
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    Long ingCode = Long.parseLong(cursor.getString(0));
                    String name = cursor.getString(1);
                    suggestions.add(new IngredientSearchSuggestion(name,ingCode));
                    cursor.moveToNext();
                }
            }
        } catch (SQLException mSQLException) {
            Log.e(LOG_TAG, "error in getSearchSuggestions cursor: " + mSQLException.toString());
            throw mSQLException;
        } finally {
            cursor.close();
        }
        return suggestions;
    }

    public Ingredient getSingleIngredient(long ingCode) {
        CursorWrapper cursor = queryIngredients(
                IngredientValue.getQueryDBColNames(),
                IngredientsTable.COLS.ING_CODE + " = ?",
                new String[]{String.valueOf(ingCode)}
        );
        Ingredient ing = new Ingredient();
        try {
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for(IngredientValue ingVal : IngredientValue.values()) {
                    double val = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ingVal.getDBCol())));
                    ing.putIngVal(ingVal.getDBCol(),val);
                }
            }
        } catch (SQLException mSQLException) {
            Log.e(LOG_TAG, "error in getSingleIngredient cursor: " + mSQLException.toString());
            throw mSQLException;
        } finally {
            cursor.close();
        }
        return ing;
    }

    // method used to execute a SELECT command on the database ingredients
    private CursorWrapper queryIngredients(String[] cols, String whereClause, String[] whereArgs) {
        Cursor cursor = mDb.query(
                IngredientsTable.NAME, //tablename
                cols, // columns = null selects all columns (SELECT *)
                whereClause, //where clause with placeholders
                whereArgs, //where clause's arguments (placeholder values)
                null, //groupby
                null, //having
                null, //orderby
                String.valueOf(NUM_RESULTS_LIMIT) // limit
        );
        return new CursorWrapper(cursor);
    }

}
