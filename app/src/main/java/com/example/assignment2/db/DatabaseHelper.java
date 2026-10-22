package com.example.assignment2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.assignment2.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodieOrder.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RESTAURANTS = "restaurants";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CUISINE = "cuisine";
    public static final String COLUMN_LOCATION = "location";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESTAURANTS_TABLE = "CREATE TABLE " + TABLE_RESTAURANTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CUISINE + " TEXT,"
                + COLUMN_LOCATION + " TEXT" + ")";
        db.execSQL(CREATE_RESTAURANTS_TABLE);

        // Seed default restaurants matching reference image
        seedDefaultRestaurants(db);
    }

    private void seedDefaultRestaurants(SQLiteDatabase db) {
        insertDefaultRestaurant(db, "Domino's Pizza", "Italian", "Bandra West");
        insertDefaultRestaurant(db, "Pizza Hut", "Italian", "Andheri");
        insertDefaultRestaurant(db, "Burger King", "Fast Food", "Malad");
        insertDefaultRestaurant(db, "Subway", "Healthy", "Borivali");
    }

    private void insertDefaultRestaurant(SQLiteDatabase db, String name, String cuisine, String location) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CUISINE, cuisine);
        values.put(COLUMN_LOCATION, location);
        db.insert(TABLE_RESTAURANTS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        onCreate(db);
    }

    // CRUD Operations

    public long addRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, restaurant.getName());
        values.put(COLUMN_CUISINE, restaurant.getCuisine());
        values.put(COLUMN_LOCATION, restaurant.getLocation());
        long id = db.insert(TABLE_RESTAURANTS, null, values);
        db.close();
        return id;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RESTAURANTS + " ORDER BY " + COLUMN_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int cuisineIndex = cursor.getColumnIndex(COLUMN_CUISINE);
                int locationIndex = cursor.getColumnIndex(COLUMN_LOCATION);

                Restaurant restaurant = new Restaurant(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(cuisineIndex),
                        cursor.getString(locationIndex)
                );
                restaurantList.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return restaurantList;
    }

    public int updateRestaurant(Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, restaurant.getName());
        values.put(COLUMN_CUISINE, restaurant.getCuisine());
        values.put(COLUMN_LOCATION, restaurant.getLocation());

        int rows = db.update(TABLE_RESTAURANTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(restaurant.getId())});
        db.close();
        return rows;
    }

    public void deleteRestaurant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
