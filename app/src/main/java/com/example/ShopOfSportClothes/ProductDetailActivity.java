package com.example.ShopOfSportClothes;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ShopOfSportClothes.R;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail); // Убедитесь, что есть layout

        TextView itemName = findViewById(R.id.item_name);
        TextView itemPrice = findViewById(R.id.item_price);
        TextView itemData = findViewById(R.id.item_data);
        TextView itemImage = findViewById(R.id.item_image);

        // Получаем данные из Intent
        String name = getIntent().getStringExtra("item_name");
        String price = getIntent().getStringExtra("item_price");
        String data = getIntent().getStringExtra("item_data");
        String image = getIntent().getStringExtra("item_image");

        itemName.setText(name != null ? name : "Нет данных");
        itemPrice.setText(price != null ? price : "0.0");
        itemData.setText(data != null ? data : "Нет данных");
        itemImage.setText(image != null ? image : "Нет изображения");
    }
}