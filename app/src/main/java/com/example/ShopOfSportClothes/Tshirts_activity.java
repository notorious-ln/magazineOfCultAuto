package com.example.ShopOfSportClothes;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ShopOfSportClothes.Adapters.FavoritesAdapter;
import com.example.ShopOfSportClothes.ui.DatabaseHelper;
import Items.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

public class Tshirts_activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private DatabaseHelper dbHelper;
    private long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tshirts);

        recyclerView = findViewById(R.id.recycler_Tshirts);
        dbHelper = DatabaseHelper.getInstance(this);
        currentUserId = getCurrentUserId();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavoriteItem item) {
                // Переход уже обработан в адаптере
            }

            @Override
            public void onRemoveClick(FavoriteItem item) {
                // Удаление не требуется, оставляем пустым
            }
        }, dbHelper, false); // Указываем, что это выбор, а не избранное

        recyclerView.setAdapter(adapter);
        loadTshirts();

        dbHelper.debugFavorites();
    }

    private void loadTshirts() {
        List<FavoriteItem> tshirts = new ArrayList<>();
        try {
            Cursor cursor = dbHelper.getAllItemsByCategory("Tshirts");
            if (cursor != null) {
                int itemIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_ID);
                int itemNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_NAME);
                int itemImageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_IMAGE);
                int itemDataIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_DATA);
                int itemPriceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_PRICE);
                int itemCategoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_CATEGORY);

                while (cursor.moveToNext()) {
                    String itemId = cursor.getString(itemIdIndex);
                    String itemName = cursor.getString(itemNameIndex);
                    String itemImage = itemImageIndex != -1 ? cursor.getString(itemImageIndex) : "";
                    String itemData = itemDataIndex != -1 ? cursor.getString(itemDataIndex) : "";
                    double itemPrice = itemPriceIndex != -1 ? cursor.getDouble(itemPriceIndex) : 0.0;
                    String itemCategory = itemCategoryIndex != -1 ? cursor.getString(itemCategoryIndex) : "";

                    tshirts.add(new FavoriteItem(itemId, itemName, itemImage, itemData, itemPrice, itemCategory));
                }
                cursor.close();
            }
            Log.d("Tshirts", "Loaded " + tshirts.size() + " t-shirts");
            adapter.setFavorites(tshirts);
        } catch (Exception e) {
            Log.e("Tshirts", "Ошибка загрузки футболок", e);
            Toast.makeText(this, "Ошибка загрузки футболок", Toast.LENGTH_SHORT).show();
        }
    }

    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTshirts();
    }
}