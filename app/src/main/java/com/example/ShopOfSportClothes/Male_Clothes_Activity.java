package com.example.ShopOfSportClothes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ShopOfSportClothes.Adapters.ChildAdapter;
import com.example.ShopOfSportClothes.Adapters.newParentAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Items.ChildItem;
import Items.newParentItem;

public class Male_Clothes_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.male_clothes);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = findViewById(R.id.male_clothes_recycler);

        List<newParentItem> data = new ArrayList<>();

        List<ChildItem> upCloth = Arrays.asList(
                new ChildItem("Футболки", new ArrayList<>()),
                new ChildItem("Кофты", new ArrayList<>())


        );
        List<ChildItem> underCloth = Arrays.asList(
                new ChildItem("Брюки", new ArrayList<>()),
                new ChildItem("Спортивные штаны", new ArrayList<>())


        );
       newParentItem upClothItem  = new newParentItem("Верхняя одежда", upCloth);
        newParentItem underClothItem  = new newParentItem("Штаны", underCloth);
        upClothItem .setExpanded(false);
        underClothItem .setExpanded(false);

       data.add(upClothItem);
       data.add(underClothItem);


        ChildAdapter.OnChildClickListener listener = childItem -> {
            android.util.Log.d("HomeFragment", "Child clicked: " + childItem.getTitle());
            if ("Футболки".equals(childItem.getTitle())) {
                android.util.Log.d("HomeFragment", "Navigating to Tshirts_activity");

                Intent intent = new Intent(this, Tshirts_activity.class);
                startActivity(intent);
            }
        };


        newParentAdapter adapter = new newParentAdapter(data, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Используем this вместо requireContext()
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}