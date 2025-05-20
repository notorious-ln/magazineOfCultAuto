package com.example.magazineofcultauto;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BMW_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmw);

        // Настройка RecyclerView с моделями BMW
        RecyclerView recyclerView = findViewById(R.id.recycler_bmw);

        List<ParentItem> data = new ArrayList<>();

        List<ChildItem> sedans = Arrays.asList(
                new ChildItem("M5 e39", new ArrayList<>()),
                new ChildItem("M5 e34", new ArrayList<>())


        );
        List<ChildItem> crossovers = Arrays.asList(
                new ChildItem("X5 e53", new ArrayList<>()),
                new ChildItem("M5 e70", new ArrayList<>())


        );
       ParentItem parentItem = new ParentItem("Седаны", sedans);
       ParentItem parentItem1 = new ParentItem("Кроссоверы", crossovers);
       parentItem.setExpanded(true);
       parentItem1.setExpanded(true);

       data.add(new ParentItem("Седаны", sedans));
       data.add(new ParentItem("Кроссоверы", crossovers));


        ChildAdapter.OnChildClickListener listener = childItem -> {
            android.util.Log.d("HomeFragment", "Child clicked: " + childItem.getTitle());
            if ("M5 e39".equals(childItem.getTitle())) {
                android.util.Log.d("HomeFragment", "Navigating to BMW_Activity");
                // Запускаем BMW_Activity с помощью Intent
                Intent intent = new Intent(this, m5e39.class);
                startActivity(intent);
            }
        };

        ParentAdapter adapter = new ParentAdapter(data, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Используем this вместо requireContext()
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}