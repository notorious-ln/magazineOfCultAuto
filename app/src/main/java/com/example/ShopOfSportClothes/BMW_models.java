package com.example.ShopOfSportClothes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ShopOfSportClothes.Adapters.GrandchildAdapter;
import com.example.ShopOfSportClothes.databinding.FragmentBMWModelsBinding;

import java.util.Arrays;
import java.util.List;

import Items.GrandchildItem;
import Items.Photo;


public class BMW_models extends Fragment {



    private FragmentBMWModelsBinding binding;
    private static final String ARG_TITLE = "title";
    public static BMW_models newInstance(String title) {
        BMW_models fragment = new BMW_models();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBMWModelsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Настройка RecyclerView с моделями BMW
        RecyclerView recyclerView = binding.recyclerBMW;

        List<Photo> photos = Arrays.asList(
                new Photo("https://example.com/bmw_m5.jpg"),
                new Photo("https://example.com/bmw_x5.jpg")
        );

        List<GrandchildItem> bmwModels = Arrays.asList(
                new GrandchildItem("M5 E39", photos),
                new GrandchildItem("X5 G05", photos)
        );

        GrandchildAdapter adapter = new GrandchildAdapter(bmwModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }
}

