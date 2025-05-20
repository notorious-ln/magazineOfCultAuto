package com.example.magazineofcultauto.ui.home;

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

import com.example.magazineofcultauto.BMW_Activity;
import com.example.magazineofcultauto.BMW_models;
import com.example.magazineofcultauto.ChildAdapter;
import com.example.magazineofcultauto.ChildItem;
import com.example.magazineofcultauto.ParentAdapter;
import com.example.magazineofcultauto.ParentItem;
import com.example.magazineofcultauto.R;
import com.example.magazineofcultauto.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<ChildItem> childItems = Arrays.asList(
                new ChildItem("BMW", new ArrayList<>()),
                new ChildItem("Audi", new ArrayList<>())
        );
        List<ChildItem> childItems1 = Arrays.asList(
                new ChildItem("Honda", new ArrayList<>()),
                new ChildItem("Nissan", new ArrayList<>())
        );
        ParentItem parentItem = new ParentItem("Немецкие машины", childItems);
        ParentItem parentItem1 = new ParentItem("Японские машины", childItems1);
        parentItem.setExpanded(true); // Раскрываем родительский элемент
        data.add(new ParentItem("Немецкие машины", childItems));
        data.add(new ParentItem("Японские машины", childItems1));

        ChildAdapter.OnChildClickListener listener = childItem -> {
            android.util.Log.d("HomeFragment", "Child clicked: " + childItem.getTitle());
            if ("BMW".equals(childItem.getTitle())) {
                android.util.Log.d("HomeFragment", "Navigating to BMW_Activity");
                // Запускаем BMW_Activity с помощью Intent
                Intent intent = new Intent(requireContext(), BMW_Activity.class);
                startActivity(intent);
            }
        };

        // Настройка ParentAdapter
        ParentAdapter adapter = new ParentAdapter(data, listener); // Передаем listener
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}