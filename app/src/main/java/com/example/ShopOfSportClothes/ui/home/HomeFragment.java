package com.example.ShopOfSportClothes.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ShopOfSportClothes.Male_Clothes_Activity;
import com.example.ShopOfSportClothes.Adapters.ChildAdapter;
import com.example.ShopOfSportClothes.Adapters.ParentAdapter;
import Items.ParentItem;
import com.example.ShopOfSportClothes.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Подготовка данных
        List<ParentItem> data = new ArrayList<>();

        ParentItem male = new ParentItem("Мужчинам", new ArrayList<>());
        ParentItem female = new ParentItem("Женщинам", new ArrayList<>());
        data.add(male);
        data.add(female);
        ParentAdapter.OnParentClickListener listener = (position, parentItem) -> {
            android.util.Log.d("HomeFragment", "Child clicked: " + parentItem.getTitle());
            if ("Мужчинам".equals(parentItem.getTitle())) {
                android.util.Log.d("HomeFragment", "Navigating to BMW_Activity");
                // Запускаем BMW_Activity с помощью Intent
                Intent intent = new Intent(requireContext(), Male_Clothes_Activity.class);
                startActivity(intent);
            }
        };
        // Слушатель кликов для дочерних элементов
        ChildAdapter.OnChildClickListener childListener = childItem -> {
            if ("BMW".equals(childItem.getTitle())) {
                startActivity(new Intent(requireContext(), Male_Clothes_Activity.class));
            }
        };


        // Настройка ParentAdapter
        ParentAdapter adapter = new ParentAdapter(data, childListener, listener); // Передаем listener
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}