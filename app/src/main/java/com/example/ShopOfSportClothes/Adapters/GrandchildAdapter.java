package com.example.ShopOfSportClothes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Items.GrandchildItem;
import com.example.ShopOfSportClothes.R;

import java.util.ArrayList;
import java.util.List;

public class GrandchildAdapter extends RecyclerView.Adapter<GrandchildAdapter.GrandchildViewHolder> {
    private List<GrandchildItem> grandchildItems;

    public GrandchildAdapter(List<GrandchildItem> grandchildItems) {
        this.grandchildItems = grandchildItems != null ? grandchildItems : new ArrayList<>();;
    }

    @NonNull
    @Override
    public GrandchildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grandchild_item_layout, parent, false);
        return new GrandchildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrandchildViewHolder holder, int position) {
        GrandchildItem item = grandchildItems.get(position);
        holder.textView.setText(item.getTitle());
        if (item.isExpanded()) {
            expand(holder.recyclerPhotos);
        } else {
            collapse(holder.recyclerPhotos);
        }

        // Настройка RecyclerView для фото
        PhotoAdapter photoAdapter = new PhotoAdapter(item.getPhotos());
        holder.recyclerPhotos.setAdapter(photoAdapter);
        holder.recyclerPhotos.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(),
                        LinearLayoutManager.HORIZONTAL, false));

        // Управление видимостью
        holder.recyclerPhotos.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return grandchildItems.size();
    }

    static class GrandchildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerPhotos;

        public GrandchildViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textGrandchildTitle);
            recyclerPhotos = itemView.findViewById(R.id.recyclerGrandchildPhotos);
        }
    }
    private void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(300);
        view.startAnimation(animation);
    }

    private void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(300);
        view.startAnimation(animation);
    }
}