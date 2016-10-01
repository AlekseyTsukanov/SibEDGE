package com.acukanov.sibedge.data.database;


import android.content.ContentValues;
import android.database.Cursor;

import com.acukanov.sibedge.data.database.model.Items;

public class DatabaseTables {
    public static final String TABLE_ITEMS = "list_items";

    public DatabaseTables() {}

    public static final class TableItems {
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TEXT = "text_item";
        public static final String COLUMN_CHECK = "check_item";

        public static final String CREATE_TABLE_ITEMS =
                "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_TEXT + " TEXT NOT NULL,"
                + COLUMN_CHECK + " INT NOT NULL"
                + ");";

        public static ContentValues createOrUpdateItem(Items items) {
            ContentValues values = new ContentValues(2);
            values.put(COLUMN_TEXT, items.text);
            values.put(COLUMN_CHECK, items.check);
            return values;
        }

        public static Items parseCursor(Cursor cursor) {
            Items items = new Items();
            items.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            items.text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT));
            items.check = cursor.getInt(cursor.getColumnIndex(COLUMN_CHECK));
            return items;
        }

        public static String createTestData(int i) {
            int check = 0;
            if ((i + 1) % 2 == 0) {
                check = 1;
            }
            return "INSERT INTO " + TABLE_ITEMS + " VALUES ("
                    + (i + 1) + ", \"Item" + i + "\", " + check + ");";
        }
    }
}
