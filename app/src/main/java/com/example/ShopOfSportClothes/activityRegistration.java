package com.example.ShopOfSportClothes;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ShopOfSportClothes.ui.DatabaseHelper;
import com.example.ShopOfSportClothes.ui.home.HomeFragment;
import com.example.ShopOfSportClothes.ui.user.UserFragment;

public class activityRegistration extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText createlogin;
    private EditText mail;
    private EditText createpassword;
    private Button buttonRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        databaseHelper = new DatabaseHelper(this);
        createlogin = findViewById(R.id.createlogin);
        mail = findViewById(R.id.mail);
        createpassword = findViewById(R.id.password);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        Intent acthome = new Intent(this, HomeActivity.class);
        String messageOfReg = "Вы успешно зарегистрировались";
        HomeFragment main = new HomeFragment();
        UserFragment user = new UserFragment();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView mainMenu = findViewById(R.id.textTomenu);
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = createlogin.getText().toString().trim();
                String email = mail.getText().toString().trim();
                String password = createpassword.getText().toString().trim();

                databaseHelper.addUser(username, email, password);
                showReg(messageOfReg.toString());

                setNewFragment(user);
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
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
    private void showReg(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    private void setNewFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment_activity_home, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}