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
        Cursor cursor = dbHelper.getUserFavorites(currentUserId);
        List<FavoriteItem> favorites = new ArrayList<>();

        if (cursor != null) {
            try {
                // Получаем индексы колонок один раз
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_NAME);
                int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_IMAGE);
                int dataIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CAR_DATA);

                // Проверяем, что все индексы валидны
                if (idIndex >= 0 && nameIndex >= 0 && imageIndex >= 0 && dataIndex >= 0) {
                    while (cursor.moveToNext()) {
                        String carId = cursor.getString(idIndex);
                        String carName = cursor.getString(nameIndex);
                        String carImage = cursor.getString(imageIndex);
                        String carData = cursor.getString(dataIndex);

                        favorites.add(new FavoriteItem(carId, carName, carImage, carData));
                    }
                } else {
                    Log.e("FavouriteFragment", "One or more column indexes not found");
                }
            } finally {
                cursor.close();
            }
        }

        adapter.setFavorites(favorites);
        emptyText.setVisibility(favorites.isEmpty() ? View.VISIBLE : View.GONE);
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