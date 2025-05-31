package com.example.magazineofcultauto.ui.favourite;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazineofcultauto.FavoriteItem;
import com.example.magazineofcultauto.FavoritesAdapter;
import com.example.magazineofcultauto.R;
import com.example.magazineofcultauto.databinding.FragmentFavouriteBinding;
import com.example.magazineofcultauto.m5e39;
import com.example.magazineofcultauto.ui.DatabaseHelper;

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
        FavouriteViewModel dashboardViewModel =
                new ViewModelProvider(this).get(FavouriteViewModel.class);
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        emptyText = view.findViewById(R.id.emptyFavoritesText);
        dbHelper = new DatabaseHelper(getContext());

        // Получаем ID текущего пользователя (нужно реализовать получение из SharedPreferences)
        currentUserId = getCurrentUserId();
        adapter = new FavoritesAdapter(item -> {
            // Обработка клика - переход к деталям автомобиля
            Intent intent = new Intent(getActivity(), m5e39.class);
            intent.putExtra("car_data", item.getCarData());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        loadFavorites();

        return view;


    }
    private void loadFavorites() {
        try {
            Cursor cursor = dbHelper.getUserFavorites(currentUserId);
            List<FavoriteItem> items = new ArrayList<>();

            if (cursor != null) {
                // Получаем индексы всех колонок
                int idIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID);
                int carIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_ID);
                int carNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_NAME);
                int carImageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_IMAGE);
                int carDataIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_DATA);

                // Проверяем обязательные колонки
                if (carIdIndex == -1 || carNameIndex == -1) {
                    throw new IllegalStateException("Обязательные колонки не найдены");
                }

                while (cursor.moveToNext()) {
                    String carId = cursor.getString(carIdIndex);
                    String carName = cursor.getString(carNameIndex);

                    // Обработка необязательных колонок
                    String carImage = carImageIndex != -1 ? cursor.getString(carImageIndex) : "";
                    String carData = carDataIndex != -1 ? cursor.getString(carDataIndex) : "";

                    items.add(new FavoriteItem(carId, carName, carImage, carData));
                }
                cursor.close();
            }
            adapter.setFavorites(items);
        } catch (Exception e) {
            Log.e("FavouriteFragment", "Ошибка загрузки избранного", e);
            Toast.makeText(getContext(), "Ошибка загрузки избранного", Toast.LENGTH_SHORT).show();
        }
    }
    private long getCurrentUserId() {
        // Реализуйте получение ID текущего пользователя из SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getLong("user_id", -1);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();  // Перезагружаем данные при каждом открытии фрагмента
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}