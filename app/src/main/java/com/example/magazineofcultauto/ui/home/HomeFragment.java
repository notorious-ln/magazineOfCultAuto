package com.example.magazineofcultauto.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magazineofcultauto.ChildItem;
import com.example.magazineofcultauto.GrandchildItem;
import com.example.magazineofcultauto.ParentAdapter;
import com.example.magazineofcultauto.ParentItem;
import com.example.magazineofcultauto.Photo;
import com.example.magazineofcultauto.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private View rootView;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // 1. Сначала инициализируем binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 2. Теперь получаем RecyclerView через binding
        RecyclerView recyclerView = binding.recyclerView;

        // 3. Заполняем данными
        List<ParentItem> data = new ArrayList<>();

        List<Photo> photos1 = Arrays.asList(
                new Photo("https://example.com/photo1.jpg")

        );
        List<GrandchildItem> grandchildItems = Arrays.asList(
                new GrandchildItem("Подраздел 1", photos1),
                new GrandchildItem("Подраздел 2", new ArrayList<>())
        );
        List<ChildItem> childItems1 = Arrays.asList(
                new ChildItem("Подэлемент 1", grandchildItems),
                new ChildItem("Подэлемент 2", new ArrayList<>())
        );



        data.add(new ParentItem("Категория 1", childItems1));
        data.add(new ParentItem("Категория 2", childItems1));

        // 4. Настраиваем RecyclerView
        ParentAdapter adapter = new ParentAdapter(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Исправлено здесь
        recyclerView.setAdapter(adapter);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}