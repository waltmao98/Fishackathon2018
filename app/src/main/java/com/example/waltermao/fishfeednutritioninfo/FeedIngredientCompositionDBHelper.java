package com.example.waltermao.fishfeednutritioninfo;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

public class FeedIngredientCompositionDBHelper extends SQLiteOpenHelper {

    public String mDBName;
    private static final String LOG_TAG = FeedIngredientCompositionDBHelper.class.getSimpleName();

    private static String DB_PATH = ""; //destination path (location) of our database on device

    private SQLiteDatabase mDataBase;

    private final WeakReference<Context> mContextReference;

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // upgrade the version
    }

    public FeedIngredientCompositionDBHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        mDBName = dbName;
        mContextReference = new WeakReference<>(context);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            Log.d(LOG_TAG,"DB_PATH: " + DB_PATH);
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
    }

    public void createDataBase() throws IOException {
        //If the database does not exist, copy it from the assets.
        if(!checkDataBase()) {
            try {
                //Copy the database from assets
                this.getReadableDatabase();
                this.close();
                copyDataBase();
                Log.d(LOG_TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                Log.e(LOG_TAG,"createDatabase error copying database");
                throw new Error("Error copying database");
            }
        }
    }

    // check if db exists
    private boolean checkDataBase() {
        File dbFile = mContextReference.get().getDatabasePath(mDBName);
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContextReference.get().getAssets().open(mDBName);
        String outFileName = DB_PATH + mDBName;
        Log.d(LOG_TAG,"outFileName: " + outFileName);
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + mDBName;
        Log.d(LOG_TAG, "mPath: " + mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

}
