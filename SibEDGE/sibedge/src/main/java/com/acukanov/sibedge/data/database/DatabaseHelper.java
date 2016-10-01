package com.acukanov.sibedge.data.database;


import android.database.Cursor;

import com.acukanov.sibedge.data.database.model.Items;
import com.acukanov.sibedge.utils.LogUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DatabaseHelper {
    private static final String LOG_TAG = LogUtils.makeLogTag(DatabaseHelper.class);
    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DatabaseOpenHelper databaseOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(databaseOpenHelper);
    }

    public BriteDatabase getBriteDatabase() {
        return mDb;
    }

    public Observable<Items> findAllItems() {
        return Observable.create(subscriber -> {
            Items item = new Items();
            String query = "SELECT * FROM " + DatabaseTables.TABLE_ITEMS;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.TableItems.parseCursor(cursor).toString());
                item = (DatabaseTables.TableItems.parseCursor(cursor));
                subscriber.onNext(item);
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }

    public Observable<Items> findItemById(long id) {
        return Observable.create(subscriber -> {
            String query = "SELECT * FROM " + DatabaseTables.TABLE_ITEMS
                    + " WHERE " + DatabaseTables.TABLE_ITEMS + "." + DatabaseTables.TableItems.COLUMN_ID + " = " + id;
            LogUtils.error(LOG_TAG, query);
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()){
                subscriber.onNext(DatabaseTables.TableItems.parseCursor(cursor));
                LogUtils.error(LOG_TAG, DatabaseTables.TableItems.parseCursor(cursor).toString());
            }
            cursor.close();
            subscriber.unsubscribe();
        });
    }

    public Observable<Long> createItem(Items item) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                long id = mDb.insert(DatabaseTables.TABLE_ITEMS, DatabaseTables.TableItems.createOrUpdateItem(item));
                transaction.markSuccessful();
                subscriber.onNext(id);
            } finally {
                transaction.end();
                LogUtils.debug(LOG_TAG, "Created item = " + item.getId() + "\t" + item.getText() + "\t" + item.getCheck());
            }
        });
    }

    public Observable<Void> updateItem(Items item) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.update(
                        DatabaseTables.TABLE_ITEMS,
                        DatabaseTables.TableItems.createOrUpdateItem(item),
                        DatabaseTables.TableItems.COLUMN_ID + "=?",
                        String.valueOf(item.id)
                        );
                transaction.markSuccessful();
                subscriber.onCompleted();
                subscriber.unsubscribe();
            } finally {
                transaction.end();
                LogUtils.debug(LOG_TAG, "Updated item = " + item.getId() + "\t" + item.getText() + "\t" + item.getCheck());
            }
        });
    }

    public Observable<Void> deleteItem(Items item) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.delete(
                        DatabaseTables.TABLE_ITEMS,
                        DatabaseTables.TableItems.COLUMN_ID + "=?",
                        String.valueOf(item.id)
                );
                transaction.markSuccessful();
                subscriber.onCompleted();
                subscriber.unsubscribe();
            } finally {
                transaction.end();
                LogUtils.debug(LOG_TAG, "Deleted item = " + item.getId() + "\t" + item.getText() + "\t" + item.getCheck());
            }
        });
    }

    public Observable<Void> clearTables() {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                Cursor cursor = mDb.query("SELECT name FROM sqlite_manager WHERE type='table'");
                while (cursor.moveToNext()) {
                    mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    LogUtils.debug(LOG_TAG, "clear table");
                }
                cursor.close();
                transaction.markSuccessful();
                subscriber.onCompleted();
            } finally {
                transaction.end();
            }
        });
    }
}
