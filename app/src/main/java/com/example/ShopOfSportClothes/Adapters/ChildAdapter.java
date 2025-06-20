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

import Items.ChildItem;
import com.example.ShopOfSportClothes.R;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private List<ChildItem> childItems;
    private OnChildClickListener listener;
    public interface OnChildClickListener {
        void onChildClick(ChildItem childItem);
    }

    public ChildAdapter(List<ChildItem> childItems, OnChildClickListener listener) {
        this.childItems = childItems != null ? childItems : new ArrayList<>();
        this.listener = listener;
        android.util.Log.d("ChildAdapter", "Listener initialized: " + (listener != null));
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item_layout, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        ChildItem child = childItems.get(position);
        android.util.Log.d("ChildAdapter", "Binding item: " + child.getTitle());
        holder.textView.setText(child.getTitle());
        GrandchildAdapter grandchildAdapter = new GrandchildAdapter(child.getGrandchildItems());
        holder.recyclerGrandchildren.setAdapter(grandchildAdapter);
        holder.recyclerGrandchildren.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext()));
        if (child.isExpanded()) {
            expand(holder.recyclerGrandchildren);
        } else {
            collapse(holder.recyclerGrandchildren);
        }
        holder.recyclerGrandchildren.setVisibility(child.isExpanded() ? View.VISIBLE : View.GONE);

        // Обработка клика - объединенная логика
        holder.textView.setOnClickListener(v -> {
                    // 1. Переключение состояния раскрытия/скрытия
                    child.setExpanded(!child.isExpanded());
                    notifyItemChanged(position);
                });
        holder.textView.setOnClickListener(v -> {
            android.util.Log.d("ChildAdapter", "TextView clicked: " + child.getTitle());
            android.widget.Toast.makeText(holder.itemView.getContext(), "Clicked: " + child.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onChildClick(child);
            }
        });







        // Анимация можно добавить здесь, если нужно
    }

    @Override
    public int getItemCount() {
        return childItems.size();
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerGrandchildren; // Изменили с recyclerPhotos

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textChildTitle);
            recyclerGrandchildren = itemView.findViewById(R.id.recyclerGrandchildren);
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