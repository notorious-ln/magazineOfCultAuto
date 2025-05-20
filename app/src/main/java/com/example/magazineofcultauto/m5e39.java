package com.example.magazineofcultauto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.magazineofcultauto.ui.DatabaseHelper;

public class m5e39 extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private long currentUserId;
    private String carId;
    private Button favoriteButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_m5e39);


        dbHelper = new DatabaseHelper(this);
        currentUserId = getCurrentUserId();
        carId = "unique_car_id"; // Уникальный ID автомобиля

        favoriteButton = findViewById(R.id.favoriteButton);
        updateFavoriteButton();

        favoriteButton.setOnClickListener(v -> toggleFavorite());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void toggleFavorite() {

            if (dbHelper.isFavorite(carId, currentUserId)) {
                int deleted = dbHelper.removeFavorite(carId, currentUserId);
                Log.d("FAVORITE", "Удалено из избранного: " + deleted);
            } else {
                long insertedId = dbHelper.addFavorite(
                        carId,
                        "BMW M5 E39",  // Пример названия
                        "placeholder",  // Пример изображения
                        "{\"model\":\"M5 E39\"}",  // JSON данных
                        currentUserId
                );
                Log.d("FAVORITE", "Добавлено в избранное, ID: " + insertedId);
            }
            updateFavoriteButton();

    }

    private void updateFavoriteButton() {
        boolean isFavorite = dbHelper.isFavorite(carId, currentUserId);
        favoriteButton.setText(isFavorite ? "Удалить из избранного" : "Добавить в избранное");
    }

    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        long userId = prefs.getLong("user_id", -1);
        Log.d("USER_ID", "Текущий ID пользователя: " + userId);
        return userId;
    }
}