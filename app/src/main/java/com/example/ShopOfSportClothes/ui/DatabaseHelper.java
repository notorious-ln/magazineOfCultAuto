package com.example.ShopOfSportClothes.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clothing_database.db";
    public static final int DATABASE_VERSION = 4;

    // Таблица пользователей
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Таблица товаров
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_IMAGE = "item_image";
    public static final String COLUMN_ITEM_DATA = "item_data";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_CATEGORY = "item_category";

    // Таблица избранных товаров
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_USER_ID = "user_id";

    private static final String TAG = "DatabaseHelper";

    // Singleton instance
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        logTableStructure();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL);";

        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_ID + " TEXT NOT NULL UNIQUE, " +
                COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                COLUMN_ITEM_IMAGE + " TEXT, " +
                COLUMN_ITEM_DATA + " TEXT, " +
                COLUMN_ITEM_PRICE + " REAL NOT NULL, " +
                COLUMN_ITEM_CATEGORY + " TEXT NOT NULL);";

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_ID + " TEXT NOT NULL, " +
                COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                COLUMN_ITEM_IMAGE + " TEXT, " +
                COLUMN_ITEM_DATA + " TEXT, " +
                COLUMN_ITEM_PRICE + " REAL NOT NULL, " +
                COLUMN_ITEM_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " +
                TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_ITEM_ID + ") REFERENCES " +
                TABLE_ITEMS + "(" + COLUMN_ITEM_ID + ") ON DELETE CASCADE);";

        String createIndexFavorites = "CREATE INDEX idx_favorites_user_item ON " +
                TABLE_FAVORITES + "(" + COLUMN_USER_ID + ", " + COLUMN_ITEM_ID + ");";

        db.execSQL(createUsersTable);
        db.execSQL(createItemsTable);
        db.execSQL(createFavoritesTable);
        db.execSQL(createIndexFavorites);

        initializeItems(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Обновление базы данных с версии " + oldVersion + " на " + newVersion);
        if (oldVersion < 4) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void initializeItems(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, "tshirt_001");
        values.put(COLUMN_ITEM_NAME, "Los Angeles T-Shirt");
        values.put(COLUMN_ITEM_IMAGE, "los_angeles_tshirt.jpg");
        values.put(COLUMN_ITEM_DATA, "{\"size\":\"M\", \"color\":\"Black\"}");
        values.put(COLUMN_ITEM_PRICE, 2999.99);
        values.put(COLUMN_ITEM_CATEGORY, "Tshirts");
        db.insert(TABLE_ITEMS, null, values);

        values.clear();
        values.put(COLUMN_ITEM_ID, "tshirt_002");
        values.put(COLUMN_ITEM_NAME, "New York T-Shirt");
        values.put(COLUMN_ITEM_IMAGE, "new_york_tshirt.jpg");
        values.put(COLUMN_ITEM_DATA, "{\"size\":\"L\", \"color\":\"Black\"}");
        values.put(COLUMN_ITEM_PRICE, 3499.99);
        values.put(COLUMN_ITEM_CATEGORY, "Tshirts");
        db.insert(TABLE_ITEMS, null, values);

        values.clear();
        values.put(COLUMN_ITEM_ID, "tshirt_003");
        values.put(COLUMN_ITEM_NAME, "Tokyo T-Shirt");
        values.put(COLUMN_ITEM_IMAGE, "tokyo_tshirt.webp");
        values.put(COLUMN_ITEM_DATA, "{\"size\":\"L\", \"color\":\"White\"}");
        values.put(COLUMN_ITEM_PRICE, 3999.99);
        values.put(COLUMN_ITEM_CATEGORY, "Tshirts");
        db.insert(TABLE_ITEMS, null, values);
    }

    public long addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);
            return db.insertOrThrow(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при добавлении пользователя: " + e.getMessage());
            return -1;
        }
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                    COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{username, password});
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при проверке учетных данных: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean addFavoriteWithSize(String itemId, long userId, String size) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Проверяем, есть ли уже такая футболка с таким размером
        Cursor cursor = db.query(
                TABLE_FAVORITES,
                new String[]{COLUMN_ID},
                COLUMN_ITEM_ID + " = ? AND " + COLUMN_USER_ID + " = ? AND " + COLUMN_ITEM_DATA + " LIKE ?",
                new String[]{itemId, String.valueOf(userId), "%\"size\":\"" + size + "\"%"},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        if (exists) {
            return false; // Уже существует
        }

        // Получаем данные товара
        Cursor itemCursor = db.query(
                TABLE_ITEMS,
                new String[]{COLUMN_ITEM_NAME, COLUMN_ITEM_IMAGE, COLUMN_ITEM_PRICE, COLUMN_ITEM_CATEGORY},
                COLUMN_ITEM_ID + " = ?",
                new String[]{itemId},
                null, null, null);

        if (!itemCursor.moveToFirst()) {
            itemCursor.close();
            return false;
        }

        // Создаем JSON с размером
        JSONObject dataJson = new JSONObject();
        try {
            dataJson.put("size", size);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, itemId);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_ITEM_NAME, itemCursor.getString(0));
        values.put(COLUMN_ITEM_IMAGE, itemCursor.getString(1));
        values.put(COLUMN_ITEM_PRICE, itemCursor.getDouble(2));
        values.put(COLUMN_ITEM_CATEGORY, itemCursor.getString(3));
        values.put(COLUMN_ITEM_DATA, dataJson.toString());

        long result = db.insert(TABLE_FAVORITES, null, values);
        itemCursor.close();

        return result != -1;
    }

    public Cursor getUserFavoritesWithSizes(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLE_FAVORITES,
                new String[]{COLUMN_ID, COLUMN_ITEM_ID, COLUMN_ITEM_NAME,
                        COLUMN_ITEM_IMAGE, COLUMN_ITEM_DATA, COLUMN_ITEM_PRICE, COLUMN_ITEM_CATEGORY},
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);
    }

    public long getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS +
                    " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_ID);
                if (columnIndex >= 0) {
                    return cursor.getLong(columnIndex);
                } else {
                    Log.e(TAG, "Столбец " + COLUMN_ID + " не найден в результате запроса");
                }
            }
            return -1;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при получении user_id: " + e.getMessage());
            return -1;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public long addItem(String itemId, String itemName, String itemImage, String itemData,
                        double itemPrice, String itemCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ITEM_ID, itemId);
            values.put(COLUMN_ITEM_NAME, itemName);
            values.put(COLUMN_ITEM_IMAGE, itemImage);
            values.put(COLUMN_ITEM_DATA, itemData);
            values.put(COLUMN_ITEM_PRICE, itemPrice);
            values.put(COLUMN_ITEM_CATEGORY, itemCategory);
            return db.insertOrThrow(TABLE_ITEMS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при добавлении товара: " + e.getMessage());
            return -1;
        }
    }

    public Cursor getAllItemsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ITEMS,
                new String[]{COLUMN_ID, COLUMN_ITEM_ID, COLUMN_ITEM_NAME,
                        COLUMN_ITEM_IMAGE, COLUMN_ITEM_DATA, COLUMN_ITEM_PRICE, COLUMN_ITEM_CATEGORY},
                COLUMN_ITEM_CATEGORY + "=?",
                new String[]{category},
                null, null, null);
    }

    public Cursor getItemById(String itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ITEMS,
                new String[]{COLUMN_ID, COLUMN_ITEM_ID, COLUMN_ITEM_NAME,
                        COLUMN_ITEM_IMAGE, COLUMN_ITEM_DATA, COLUMN_ITEM_PRICE, COLUMN_ITEM_CATEGORY},
                COLUMN_ITEM_ID + "=?",
                new String[]{itemId},
                null, null, null);
    }

    public int deleteItem(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(TABLE_ITEMS, COLUMN_ITEM_ID + "=?", new String[]{itemId});
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при удалении товара: " + e.getMessage());
            return 0;
        }
    }

    public long addFavorite(String itemId, String itemName, String itemImage,
                            String itemData, double itemPrice, String itemCategory, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Проверяем, есть ли уже запись с таким itemId, userId и itemData (размером)
            if (isFavoriteWithSize(itemId, userId, itemData)) {
                // Если запись существует, обновляем её
                ContentValues values = new ContentValues();
                values.put(COLUMN_ITEM_NAME, itemName);
                values.put(COLUMN_ITEM_IMAGE, itemImage);
                values.put(COLUMN_ITEM_DATA, itemData);
                values.put(COLUMN_ITEM_PRICE, itemPrice);
                values.put(COLUMN_ITEM_CATEGORY, itemCategory);
                int updatedRows = db.update(TABLE_FAVORITES, values,
                        COLUMN_ITEM_ID + "=? AND " + COLUMN_USER_ID + "=? AND " + COLUMN_ITEM_DATA + "=?",
                        new String[]{itemId, String.valueOf(userId), itemData});
                Log.d(TAG, "Обновлено " + updatedRows + " записей в избранном для товара " + itemId + ", пользователя " + userId);
                return updatedRows > 0 ? 1 : -1;
            } else {
                // Если записи нет, добавляем новую
                ContentValues values = new ContentValues();
                values.put(COLUMN_ITEM_ID, itemId);
                values.put(COLUMN_ITEM_NAME, itemName);
                values.put(COLUMN_ITEM_IMAGE, itemImage);
                values.put(COLUMN_ITEM_DATA, itemData);
                values.put(COLUMN_ITEM_PRICE, itemPrice);
                values.put(COLUMN_ITEM_CATEGORY, itemCategory);
                values.put(COLUMN_USER_ID, userId);

                long result = db.insertOrThrow(TABLE_FAVORITES, null, values);
                Log.d(TAG, "Добавлен элемент " + itemId + " в избранное для пользователя " + userId + ", ID: " + result);
                return result;
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при добавлении в избранное: " + e.getMessage(), e);
            return -1;
        }
    }

    public Cursor getUserFavorites(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FAVORITES,
                new String[]{COLUMN_ID, COLUMN_ITEM_ID, COLUMN_ITEM_NAME,
                        COLUMN_ITEM_IMAGE, COLUMN_ITEM_DATA, COLUMN_ITEM_PRICE, COLUMN_ITEM_CATEGORY},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
    }

    // Проверяет, есть ли в избранном товар с конкретным размером
    public boolean isFavoriteWithSize(String itemId, long userId, String itemData) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_FAVORITES,
                    new String[]{COLUMN_ID},
                    COLUMN_ITEM_ID + "=? AND " + COLUMN_USER_ID + "=? AND " + COLUMN_ITEM_DATA + "=?",
                    new String[]{itemId, String.valueOf(userId), itemData},
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при проверке избранного: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Возвращает размер из избранного для конкретного товара и пользователя
    public String getFavoriteSize(String itemId, long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_FAVORITES,
                    new String[]{COLUMN_ITEM_DATA},
                    COLUMN_ITEM_ID + "=? AND " + COLUMN_USER_ID + "=?",
                    new String[]{itemId, String.valueOf(userId)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                String itemData = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ITEM_DATA));
                try {
                    JSONObject json = new JSONObject(itemData);
                    return json.getString("size");
                } catch (JSONException e) {
                    Log.e(TAG, "Ошибка парсинга JSON: " + e.getMessage());
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при получении размера: " + e.getMessage());
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Устаревший метод, оставлен для обратной совместимости
    public boolean isFavorite(String itemId, long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_FAVORITES,
                    new String[]{COLUMN_ID},
                    COLUMN_ITEM_ID + "=? AND " + COLUMN_USER_ID + "=?",
                    new String[]{itemId, String.valueOf(userId)},
                    null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при проверке избранного: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int removeFavorite(String itemId, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(TABLE_FAVORITES,
                    COLUMN_ITEM_ID + "=? AND " + COLUMN_USER_ID + "=?",
                    new String[]{itemId, String.valueOf(userId)});
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при удалении из избранного: " + e.getMessage());
            return 0;
        }
    }

    public void debugFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES, null);

            Log.d(TAG, "Количество избранных товаров: " + cursor.getCount());

            int itemNameIndex = cursor.getColumnIndex(COLUMN_ITEM_NAME);
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (itemNameIndex == -1 || userIdIndex == -1) {
                Log.e(TAG, "Колонки '" + COLUMN_ITEM_NAME + "' или '" + COLUMN_USER_ID + "' не найдены!");
                return;
            }

            while (cursor.moveToNext()) {
                String itemName = cursor.getString(itemNameIndex);
                long userId = cursor.getLong(userIdIndex);
                Log.d(TAG, "Товар: " + itemName + ", Пользователь: " + userId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при отладке избранного: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void logTableStructure() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + TABLE_FAVORITES + ")", null);

            Log.d(TAG, "Структура таблицы " + TABLE_FAVORITES + ":");
            while (cursor.moveToNext()) {
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                Log.d(TAG, name + " (" + type + ")");
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при логировании структуры таблицы: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void closeDatabase() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db != null && db.isOpen()) {
                db.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при закрытии базы данных: " + e.getMessage());
        }
    }
}