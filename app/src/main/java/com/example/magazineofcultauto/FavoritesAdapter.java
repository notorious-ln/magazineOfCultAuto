package com.example.magazineofcultauto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<FavoriteItem> favorites;
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(FavoriteItem item);
    }

    public FavoritesAdapter(OnFavoriteClickListener listener) {
        this.listener = listener;
        this.favorites = new ArrayList<>();
    }

    public void setFavorites(List<FavoriteItem> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = favorites.get(position);
        holder.carName.setText(item.getCarName());
        // Загрузка изображения с помощью Glide/Picasso
        Glide.with(holder.itemView.getContext())
                .load(item.getCarImage())
                .into(holder.carImage);

        holder.itemView.setOnClickListener(v -> listener.onFavoriteClick(item));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.carImage);
            carName = itemView.findViewById(R.id.carName);
        }
    }
}
