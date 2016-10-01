package com.acukanov.sibedge.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acukanov.sibedge.injection.annotations.ApplicationContext;
import com.acukanov.sibedge.utils.LogUtils;

import javax.inject.Inject;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = LogUtils.makeLogTag(DatabaseOpenHelper.class);
    private static final String DATABASE_NAME = "sibedge_database.db";
    private static final int VERSION = 2;

    @Inject
    public DatabaseOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            LogUtils.error(LOG_TAG, DatabaseTables.TableItems.CREATE_TABLE_ITEMS);
            db.execSQL(DatabaseTables.TableItems.CREATE_TABLE_ITEMS);
            for (int i = 0; i < 5; i++) {
                db.execSQL(DatabaseTables.TableItems.createTestData(i));
                LogUtils.error(LOG_TAG, DatabaseTables.TableItems.createTestData(i));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
