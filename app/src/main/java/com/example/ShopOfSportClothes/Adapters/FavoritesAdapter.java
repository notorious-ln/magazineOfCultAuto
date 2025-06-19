package com.example.ShopOfSportClothes.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ShopOfSportClothes.R;
import com.example.ShopOfSportClothes.los_angeles_Tshirt;
import com.example.ShopOfSportClothes.new_york_Tshirt;
import com.example.ShopOfSportClothes.tokyo_Tshirt;
import com.example.ShopOfSportClothes.ui.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import Items.FavoriteItem;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<FavoriteItem> items;
    private final OnItemClickListener listener;
    private final DatabaseHelper dbHelper;
    private final boolean isFavoriteContext;

    public interface OnItemClickListener {
        void onItemClick(FavoriteItem item);
        void onRemoveClick(FavoriteItem item);
    }

    public FavoritesAdapter(OnItemClickListener listener, DatabaseHelper dbHelper, boolean isFavoriteContext) {
        this.listener = listener;
        this.dbHelper = dbHelper;
        this.isFavoriteContext = isFavoriteContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isFavoriteContext ? R.layout.item_favorite : R.layout.item_tshirt;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = items.get(position);
        if (holder.itemName != null) {
            holder.itemName.setText(item.getItemName() != null ? item.getItemName() : "Без названия");
        } else {
            Log.e("FavoritesAdapter", "itemName TextView is null");
        }


        if (holder.itemPrice != null) {
            holder.itemPrice.setText(String.format("%.2f", item.getItemPrice()));
        } else {
            Log.e("FavoritesAdapter", "itemPrice TextView is null");
        }

        String imageName = item.getItemImage();
        String baseImageName = imageName.replace(".jpg", "").replace(".webp", "");
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                baseImageName,
                "drawable",
                holder.itemView.getContext().getPackageName()
        );

        if (resId != 0) {
            holder.itemImage.setImageResource(resId);
        } else {
            Log.e("Adapter", "Image not found: " + imageName);
            holder.itemImage.setImageResource(R.drawable.placeholder);
        }

        if (holder.itemSize != null) {
            try {
                JSONObject jsonData = new JSONObject(item.getItemData());
                String size = jsonData.getString("size");
                holder.itemSize.setText("Размер: " + size);
            } catch (JSONException e) {
                holder.itemSize.setText("Размер: M");
            }
        }

        // Устанавливаем слушатель клика для перехода
        holder.itemView.setOnClickListener(v -> {
            navigateToActivity(holder.itemView.getContext(), item.getItemId());
            if (listener != null) listener.onItemClick(item);
        });

        // Устанавливаем слушатель для кнопки удаления только в избранном
        // В методе onBindViewHolder замените блок с удалением на:
        if (isFavoriteContext && holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> {
                // 1. Запускаем анимацию открытия мусорки
                holder.deleteButton.playAnimation();

                // 2. Вычисляем координаты для анимации
                int[] trashLocation = new int[2];
                holder.deleteButton.getLocationOnScreen(trashLocation);
                float trashCenterX = trashLocation[0] + holder.deleteButton.getWidth() / 2f;
                float trashCenterY = trashLocation[1] + holder.deleteButton.getHeight() / 2f;

                int[] itemLocation = new int[2];
                holder.itemView.getLocationOnScreen(itemLocation);
                float itemCenterX = itemLocation[0] + holder.itemView.getWidth() / 2f;
                float itemCenterY = itemLocation[1] + holder.itemView.getHeight() / 2f;

                float deltaX = trashCenterX - itemCenterX;
                float deltaY = trashCenterY - itemCenterY;

                // 3. Анимация "засасывания"
                holder.itemView.postDelayed(() -> {
                    holder.itemView.animate()
                            .scaleX(0f)
                            .scaleY(0f)
                            .translationX(deltaX)
                            .translationY(deltaY)
                            .setDuration(500)
                            .withEndAction(() -> {
                                // 4. Удаляем элемент после анимации
                                int currentPosition = holder.getAdapterPosition();
                                if (currentPosition != RecyclerView.NO_POSITION) {
                                    // Удаляем из списка и уведомляем адаптер
                                    items.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);

                                    // Обновляем оставшиеся элементы
                                    notifyItemRangeChanged(currentPosition, items.size());

                                    // Уведомляем слушателя об удалении
                                    if (listener != null) {
                                        listener.onRemoveClick(item);
                                    }
                                }
                            })
                            .start();
                }, 500);
            });
        }


        // Парсим JSON с размером только если itemSize существует
        if (holder.itemSize != null) {
            try {
                JSONObject jsonData = new JSONObject(item.getItemData());
                String size = jsonData.getString("size");
                holder.itemSize.setText("Размер: " + size);
            } catch (JSONException e) {
                holder.itemSize.setText("Размер: не указан");
            }
        }

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void setFavorites(List<FavoriteItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    private void navigateToActivity(Context context, String itemId) {
        Intent intent;
        switch (itemId) {
            case "tshirt_001":
                intent = new Intent(context, los_angeles_Tshirt.class);
                break;
            case "tshirt_002":
                intent = new Intent(context, new_york_Tshirt.class);
                break;
            case "tshirt_003":
                intent = new Intent(context, tokyo_Tshirt.class);
                break;
            default:
                intent = new Intent(context, los_angeles_Tshirt.class);
                break;
        }
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemSize;
        ImageView itemImage;
        LottieAnimationView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemSize = itemView.findViewById(R.id.itemSize);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            if (itemName == null) {
                Log.e("FavoritesAdapter", "Failed to find item_name in layout");
            }
            if (itemPrice == null) {
                Log.e("FavoritesAdapter", "Failed to find item_price in layout");
            }
            if (itemImage == null) {
                Log.e("FavoritesAdapter", "Failed to find item_image in layout");
            }
            if (deleteButton == null && itemView.findViewById(R.id.deleteButton) != null) {
                Log.e("FavoritesAdapter", "Failed to find delete_button in layout");
            }
            if (itemSize == null ){
                Log.e("FavoritesAdapter", "Failed to find item_size in layout");

            }
        }
    }
}