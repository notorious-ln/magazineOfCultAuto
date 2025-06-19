package com.example.ShopOfSportClothes.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ShopOfSportClothes.LoginActivity;
import com.example.ShopOfSportClothes.R;
import com.example.ShopOfSportClothes.activityRegistration;
import com.example.ShopOfSportClothes.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        checkAuthStatus();

        return root;
    }

    private void checkAuthStatus() {
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            showLoggedInUI();
        } else {
            showLoggedOutUI();
        }
    }

    private void showLoggedInUI() {
        binding.log.setVisibility(View.GONE);
        binding.reg.setVisibility(View.GONE);

        String username = sharedPreferences.getString(KEY_USERNAME, "Пользователь");
        binding.tvWelcome.setText("Добро пожаловать, " + username + "!");
        binding.tvWelcome.setVisibility(View.VISIBLE);

        binding.btnLogout.setVisibility(View.VISIBLE);
        binding.btnLogout.setOnClickListener(v -> logout());
    }

    private void showLoggedOutUI() {
        binding.log.setVisibility(View.VISIBLE);
        binding.reg.setVisibility(View.VISIBLE);

        binding.tvWelcome.setVisibility(View.GONE);
        binding.btnLogout.setVisibility(View.GONE);

        binding.log.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
        binding.reg.setOnClickListener(v -> startActivity(new Intent(getActivity(), activityRegistration.class)));
    }

    private void logout() {
        // Очищаем данные в AuthPrefs
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.remove(KEY_USERNAME);
        editor.apply();

        // Очищаем user_id в user_prefs
        SharedPreferences userPrefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor userPrefsEditor = userPrefs.edit();
        userPrefsEditor.remove("user_id");
        userPrefsEditor.apply();

        checkAuthStatus(); // Обновляем UI
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAuthStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}