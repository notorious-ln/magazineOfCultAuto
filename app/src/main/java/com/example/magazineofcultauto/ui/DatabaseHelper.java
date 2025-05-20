package com.example.magazineofcultauto.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_database.db";

    private static final int DATABASE_VERSION = 2; // Увеличьте версию!
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_CAR_ID = "car_id";
    public static final String COLUMN_CAR_NAME = "car_name";
    public static final String COLUMN_CAR_IMAGE = "car_image";
    public static final String COLUMN_CAR_DATA = "car_data";
    public static final String COLUMN_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTable);


        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CAR_ID + " TEXT, " +
                COLUMN_CAR_NAME + " TEXT, " +
                COLUMN_CAR_IMAGE + " TEXT, " +
                COLUMN_CAR_DATA + " TEXT, " +
                COLUMN_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " +
                TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);

    }
    public long addFavorite(String carId, String carName, String carImage,
                            String carData, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAR_ID, carId);
        values.put(COLUMN_CAR_NAME, carName);
        values.put(COLUMN_CAR_IMAGE, carImage);
        values.put(COLUMN_CAR_DATA, carData);
        values.put(COLUMN_USER_ID, userId);
        return db.insert(TABLE_FAVORITES, null, values);
    }
    public Cursor getUserFavorites(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FAVORITES,
                new String[]{COLUMN_ID, COLUMN_CAR_ID, COLUMN_CAR_NAME, COLUMN_CAR_IMAGE},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
    }
    public boolean isFavorite(String carId, long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES,
                new String[]{COLUMN_ID},
                COLUMN_CAR_ID + "=? AND " + COLUMN_USER_ID + "=?",
                new String[]{carId, String.valueOf(userId)},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public int removeFavorite(String carId, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVORITES,
                COLUMN_CAR_ID + "=? AND " + COLUMN_USER_ID + "=?",
                new String[]{carId, String.valueOf(userId)});
    }

    // Метод для добавления пользователя
    public void addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Метод для получения всех пользователей
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);


    }
    public void debugFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES, null);

        Log.d("DB_DEBUG", "Количество записей в избранном: " + cursor.getCount());

        // Получаем индекс колонки (проверяем, что она есть)
        int carNameIndex = cursor.getColumnIndex(COLUMN_CAR_NAME);

        if (carNameIndex == -1) {
            Log.e("DB_DEBUG", "Колонка '" + COLUMN_CAR_NAME + "' не найдена!");
            cursor.close();
            return;
        }

        // Теперь безопасно получаем данные
        while (cursor.moveToNext()) {
            String carName = cursor.getString(carNameIndex);
            Log.d("DB_DEBUG", "Автомобиль: " + carName);
        }

        cursor.close();
    }
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}

