package com.example.ShopOfSportClothes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ShopOfSportClothes.ui.DatabaseHelper;
import com.example.ShopOfSportClothes.HomeActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText enterLogin;
    private EditText enterPassword;
    private MaterialButton buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);

        enterLogin = findViewById(R.id.enterLogin);
        enterPassword = findViewById(R.id.enterPassword);
        buttonLogin = findViewById(R.id.buttonlogin);
        TextView reg = findViewById(R.id.Text_reg);
        TextView toMenu = findViewById(R.id.textToMenu);

        Intent acthome = new Intent(this, HomeActivity.class);
        Intent registration = new Intent(this, activityRegistration.class);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = enterLogin.getText().toString().trim();
                String password = enterPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkUserCredentials(username, password)) {
                    // Получаем user_id из базы данных
                    long userId = databaseHelper.getUserIdByUsername(username);
                    if (userId != -1) {
                        // Сохраняем состояние авторизации в AuthPrefs
                        SharedPreferences authPrefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor authEditor = authPrefs.edit();
                        authEditor.putBoolean("isLoggedIn", true);
                        authEditor.putString("username", username);
                        authEditor.apply();

                        // Сохраняем user_id в user_prefs
                        SharedPreferences userPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor userEditor = userPrefs.edit();
                        userEditor.putLong("user_id", userId);
                        userEditor.apply();

                        Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Ошибка: не удалось получить ID пользователя", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Неверное имя пользователя или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registration);
            }
        });

        toMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(acthome);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private long getUserIdFromUsername(String username) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        long userId = -1;

        try {
            cursor = db.rawQuery(
                    "SELECT " + DatabaseHelper.COLUMN_ID + " FROM " + DatabaseHelper.TABLE_USERS +
                            " WHERE " + DatabaseHelper.COLUMN_USERNAME + " = ?", new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                if (idIndex >= 0) {
                    userId = cursor.getLong(idIndex);
                    Log.d("LoginActivity", "Найден userId: " + userId + " для пользователя " + username);
                } else {
                    Log.e("LoginActivity", "Колонка " + DatabaseHelper.COLUMN_ID + " не найдена в Cursor");
                }
            } else {
                Log.e("LoginActivity", "Пользователь с именем " + username + " не найден");
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Ошибка при выполнении запроса", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return userId;
    }
}