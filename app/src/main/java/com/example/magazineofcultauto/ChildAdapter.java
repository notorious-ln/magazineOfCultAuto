package com.example.magazineofcultauto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {
    private List<ChildItem> childItems;

    public ChildAdapter(List<ChildItem> childItems) {
        this.childItems = childItems != null ? childItems : new ArrayList<>();;
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
        holder.textView.setText(child.getTitle());
        if (child.isExpanded()) {
            expand(holder.recyclerGrandchildren);
        } else {
            collapse(holder.recyclerGrandchildren);
        }



        // Настройка вложенного RecyclerView для фото
        // Настройка RecyclerView для GrandchildItems
        GrandchildAdapter grandchildAdapter = new GrandchildAdapter(child.getGrandchildItems());
        holder.recyclerGrandchildren.setAdapter(grandchildAdapter);
        holder.recyclerGrandchildren.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext()));

        // Управление видимостью
        holder.recyclerGrandchildren.setVisibility(child.isExpanded() ? View.VISIBLE : View.GONE);


        // Клик по элементу - переключение раскрытия
        holder.itemView.setOnClickListener(v -> {
            child.setExpanded(!child.isExpanded());
            notifyItemChanged(position);
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
