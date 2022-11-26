package com.example.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tokoDB";
    private static final int DATABASE_VERSION = 1;

    // table
    private static final String PRODUCT_TABLE = "products";
    private static final String PRODUCT_ID = "id";
    private static final String PRODUCT_NAME = "name";
    private static final String PRODUCT_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_PRODUCT = "CREATE TABLE " + PRODUCT_TABLE
                + "(" + PRODUCT_ID + " INTEGER PRIMARY KEY, "
                + PRODUCT_NAME + " VARCHAR(255), "
                + PRODUCT_PRICE + " INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_PRICE, product.getPrice());

        db.insert(PRODUCT_TABLE, null, values);
        db.close();
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT * from " + PRODUCT_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping data per item
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getInt(2));

                allProducts.add(product);
            } while (cursor.moveToNext());
        }
        db.close();
        return allProducts;
    }

    public void updateProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_PRICE, product.getPrice());

        db.update(PRODUCT_TABLE, values, PRODUCT_ID + "=?",
                new String[] {String.valueOf(product.getId())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = getWritableDatabase();
        int a = db.delete(PRODUCT_TABLE, PRODUCT_ID + "=?", new String[] {String.valueOf(product.getId())});
        db.close();
        System.out.println(a);
    }
}
