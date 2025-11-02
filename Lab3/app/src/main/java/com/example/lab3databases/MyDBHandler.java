package com.example.lab3databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_cmd = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + "INTEGER PRIMARY KEY, " +
                COLUMN_PRODUCT_NAME + " TEXT, " +
                COLUMN_PRODUCT_PRICE + " DOUBLE " + ")";

        db.execSQL(create_table_cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null); // returns "cursor" all products from the table
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, product.getProductName());
        values.put(COLUMN_PRODUCT_PRICE, product.getProductPrice());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor findProducts(String name, String price) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ";
        String[] selectionArgs = null;
        ArrayList<String> conditions = new ArrayList<>();
        ArrayList<String> args = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            conditions.add(COLUMN_PRODUCT_NAME + " LIKE ?");
            args.add(name + "%");
        }
        
        if (price != null && !price.isEmpty()) {
            conditions.add(COLUMN_PRODUCT_PRICE + " = ?");
            args.add(price);
        }

        if (conditions.isEmpty()) {
            query = "SELECT * FROM " + TABLE_NAME;
            return db.rawQuery(query, null);
        } else {
            query += String.join(" AND ", conditions);
            selectionArgs = args.toArray(new String[0]);
            return db.rawQuery(query, selectionArgs);
        }
    }

    public void deleteProduct(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_PRODUCT_NAME + " = ?";
        String[] whereArgs = {name};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }
}
