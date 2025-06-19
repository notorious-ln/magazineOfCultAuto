package com.example.ShopOfSportClothes;

import android.os.Bundle;

import com.example.ShopOfSportClothes.ui.DatabaseHelper;
import com.example.ShopOfSportClothes.ui.favourite.FavouriteFragment;
import com.example.ShopOfSportClothes.ui.home.HomeFragment;
import com.example.ShopOfSportClothes.ui.user.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ShopOfSportClothes.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Установите Toolbar как ActionBar


        BottomNavigationView navView = findViewById(R.id.nav_view);






        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_home);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favourite, R.id.navigation_notifications)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        // Если пришел флаг - открываем избранное
        if (getIntent() != null && getIntent().getBooleanExtra("OPEN_FAVORITES_TAB", false)) {
            navView.setSelectedItemId(R.id.navigation_favourite);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.getInstance(this).closeDatabase();
    }


}