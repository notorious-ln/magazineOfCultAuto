package com.example.ShopOfSportClothes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ShopOfSportClothes.ui.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

public class tokyo_Tshirt extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private long currentUserId;
    private String itemId;
    private Button favoriteButton;
    private String selectedSize = "M"; // Размер по умолчанию
    private MaterialButton sizeS, sizeM, sizeL, sizeXL, sizeXXL;
    private MaterialButton selectedSizeButton; // Для отслеживания выбранной кнопки

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tokyo_tshirt);

        dbHelper = DatabaseHelper.getInstance(this);
        currentUserId = getCurrentUserId();
        itemId = "tshirt_003";
        Button toFav = findViewById(R.id.toFav);
        favoriteButton = findViewById(R.id.favoriteButton);
        updateFavoriteButton();
        sizeS = findViewById(R.id.sizeS);
        sizeM = findViewById(R.id.sizeM);
        sizeL = findViewById(R.id.sizeL);
        sizeXL = findViewById(R.id.sizeXL);
        sizeXXL = findViewById(R.id.sizeXXL);

        // Настройка кнопок размеров
        setupSizeButtons();

        toFav.setOnClickListener(v -> {
            Intent intent = new Intent(tokyo_Tshirt.this, HomeActivity.class);
            intent.putExtra("OPEN_FAVORITES_TAB", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        favoriteButton.setOnClickListener(v -> toggleFavorite());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupSizeButtons() {
        int[] sizeButtons = {R.id.sizeS, R.id.sizeM, R.id.sizeL, R.id.sizeXL, R.id.sizeXXL};
        View.OnClickListener sizeClickListener = v -> {
            MaterialButton clickedButton = (MaterialButton) v;
            selectedSize = clickedButton.getText().toString(); // Обновляем выбранный размер

            Log.d("SIZE_BUTTON", "Кнопка выбрана: " + selectedSize);

            // Сбрасываем стиль предыдущей выбранной кнопки
            if (selectedSizeButton != null && selectedSizeButton != clickedButton) {
                selectedSizeButton.setSelected(false);
                selectedSizeButton.setTextColor(Color.BLACK); // Чёрный текст для неактивной кнопки
                Log.d("SIZE_BUTTON", "Сброс стиля для: " + selectedSizeButton.getText());
            }

            // Устанавливаем стиль для новой выбранной кнопки
            clickedButton.setSelected(true);
            clickedButton.setTextColor(Color.WHITE); // Белый текст для выбранной кнопки
            Log.d("SIZE_BUTTON", "Стиль применён для: " + selectedSize);

            selectedSizeButton = clickedButton;
            updateFavoriteButton(); // Обновляем кнопку после выбора размера
        };

        // Привязываем слушатель ко всем кнопкам
        for (int buttonId : sizeButtons) {
            MaterialButton button = findViewById(buttonId);
            button.setOnClickListener(sizeClickListener);
        }

        // Устанавливаем размер по умолчанию
        selectedSizeButton = sizeM; // По умолчанию "M"
        sizeM.setSelected(true);
        sizeM.setTextColor(Color.WHITE);
        Log.d("SIZE_BUTTON", "Установлен размер по умолчанию: M");
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUserId = getCurrentUserId();
        updateFavoriteButton();
    }

    private void toggleFavorite() {
        if (currentUserId == -1) {
            Toast.makeText(this, "Пожалуйста, авторизуйтесь для добавления в избранное", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        try {
            String itemData = "{\"size\":\"" + selectedSize + "\", \"color\":\"White\"}";
            if (dbHelper.isFavoriteWithSize(itemId, currentUserId, itemData)) {
                int deleted = dbHelper.removeFavorite(itemId, currentUserId);
                Log.d("FAVORITE", "Удалено из избранного: " + deleted);
                Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
            } else {
                long insertedId = dbHelper.addFavorite(
                        itemId,
                        "Tokyo T-Shirt",
                        "tokyo_tshirt.webp",
                        itemData,
                        3999.99,
                        "Tshirts",
                        currentUserId
                );
                Log.d("FAVORITE", "Добавлено в избранное, ID: " + insertedId);
                if (insertedId == -1) {
                    Toast.makeText(this, "Ошибка добавления в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Добавлено в избранное с размером " + selectedSize, Toast.LENGTH_SHORT).show();
                }
            }
            updateFavoriteButton();
        } catch (Exception e) {
            Log.e("FAVORITE", "Ошибка при работе с избранным", e);
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFavoriteButton() {
        if (currentUserId == -1) {
            favoriteButton.setText("Войдите для избранного");
            return;
        }

        String itemData = "{\"size\":\"" + selectedSize + "\", \"color\":\"White\"}";
        boolean isFavorite = dbHelper.isFavoriteWithSize(itemId, currentUserId, itemData);
        favoriteButton.setText(isFavorite ? "Удалить из избранного" : "Добавить в избранное");
        Log.d("FAVORITE", "Обновление кнопки: isFavorite=" + isFavorite + ", size=" + selectedSize);
    }

    private long getCurrentUserId() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        long userId = prefs.getLong("user_id", -1);
        Log.d("USER_ID", "Текущий ID пользователя: " + userId);
        return userId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}