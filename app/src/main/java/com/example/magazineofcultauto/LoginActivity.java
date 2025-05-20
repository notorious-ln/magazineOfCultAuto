package com.example.magazineofcultauto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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


import com.example.magazineofcultauto.ui.DatabaseHelper;
import com.example.magazineofcultauto.ui.home.HomeFragment;



public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText enterLogin;
    private EditText enterPassword;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView reg = findViewById(R.id.Text_reg);
        enterLogin = findViewById(R.id.enterLogin);
        enterPassword = findViewById(R.id.enterPassword);
        buttonLogin = findViewById(R.id.buttonlogin);
        Intent acthome = new Intent(this, HomeActivity.class);
        Intent registration = new Intent(this, activityRegistration.class);
        TextView toMenu = findViewById(R.id.textToMenu);
        HomeFragment home = new HomeFragment();
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = enterLogin.getText().toString().trim();
                String password = enterPassword.getText().toString().trim();

                if (databaseHelper.checkUserCredentials(username, password)) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // Здесь можно перейти на следующую активность или выполнить другие действия
                    startActivity(acthome);

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
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
    private void setNewFragment(HomeFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_home, fragment) // Заменяем фрагмент
                .addToBackStack(null) // Добавляем в стек возврата
                .commit(); // Выполняем транзакцию
    }

}
