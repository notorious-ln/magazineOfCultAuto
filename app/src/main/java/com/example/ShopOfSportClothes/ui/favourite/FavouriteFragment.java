package com.example.ShopOfSportClothes.ui.favourite;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ShopOfSportClothes.Adapters.FavoritesAdapter;
import com.example.ShopOfSportClothes.R;
import com.example.ShopOfSportClothes.databinding.FragmentFavouriteBinding;
import com.example.ShopOfSportClothes.ui.DatabaseHelper;
import Items.FavoriteItem;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private FavoritesAdapter adapter;
    private DatabaseHelper dbHelper;
    private long currentUserId;

    private FragmentFavouriteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavouriteViewModel favouriteViewModel =
                new ViewModelProvider(this).get(FavouriteViewModel.class);
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        emptyText = view.findViewById(R.id.emptyFavoritesText);
        dbHelper = DatabaseHelper.getInstance(getContext());

        currentUserId = getCurrentUserId();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoritesAdapter(new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FavoriteItem item) {
                // Переход уже обработан в адаптере
            }


            @Override
            public void onRemoveClick(FavoriteItem item) {
                removeFavorite(item);
            }
        }, dbHelper, true); // Указываем, что это избранное

        recyclerView.setAdapter(adapter);
        loadFavorites();

        return view;
    }

    public void addFavoriteWithSize(String itemId, String size) {
        if (currentUserId != -1) {
            new Thread(() -> {
                boolean added = dbHelper.addFavoriteWithSize(itemId, currentUserId, size);
                requireActivity().runOnUiThread(() -> {
                    if (added) {
                        Toast.makeText(getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                        loadFavorites(); // Обновляем список
                    } else {
                        Toast.makeText(getContext(), "Уже в избранном", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        }
    }

    private void loadFavorites() {
        try {
            if (currentUserId == -1) {
                adapter.setFavorites(new ArrayList<>());
                if (emptyText != null) emptyText.setVisibility(View.VISIBLE);
                return;
            }

            Cursor cursor = dbHelper.getUserFavoritesWithSizes(currentUserId);
            List<FavoriteItem> items = new ArrayList<>();

            if (cursor != null) {
                // Определяем индексы колонок
                int itemIdIndex = cursor.getColumnIndexOrThrow("item_id");
                int itemNameIndex = cursor.getColumnIndexOrThrow("item_name");
                int itemImageIndex = cursor.getColumnIndexOrThrow("item_image");
                int itemDataIndex = cursor.getColumnIndexOrThrow("item_data");
                int itemPriceIndex = cursor.getColumnIndexOrThrow("item_price");
                int itemCategoryIndex = cursor.getColumnIndexOrThrow("item_category");

                while (cursor.moveToNext()) {
                    String itemData = cursor.getString(itemDataIndex) != null ? cursor.getString(itemDataIndex) : "{\"size\":\"M\"}";

                    items.add(new FavoriteItem(
                            cursor.getString(itemIdIndex),
                            cursor.getString(itemNameIndex),
                            cursor.getString(itemImageIndex),
                            itemData,
                            cursor.getDouble(itemPriceIndex),
                            cursor.getString(itemCategoryIndex))
                    );
                }
                cursor.close();
            }

            adapter.setFavorites(items);
            if (emptyText != null) emptyText.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        } catch (Exception e) {
            Log.e("FavouriteFragment", "Ошибка загрузки избранного", e);
            Toast.makeText(getContext(), "Ошибка загрузки избранного", Toast.LENGTH_SHORT).show();
        }
    }
    private void removeFavorite(FavoriteItem item) {
        if (currentUserId != -1) {
            new Thread(() -> {
                int deleted = dbHelper.removeFavorite(item.getItemId(), currentUserId);
                requireActivity().runOnUiThread(() -> {
                    if (deleted > 0) {
                        Toast.makeText(getContext(), "Удалено из избранного: " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Ошибка удаления", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        }
    }
    private long getCurrentUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1L);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}