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

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {
    private List<ParentItem> parentItems;
    private ChildAdapter.OnChildClickListener childClickListener; // Добавляем поле для слушателя

    public ParentAdapter(List<ParentItem> parentItems, ChildAdapter.OnChildClickListener childClickListener) {
        this.parentItems = parentItems != null ? parentItems : new ArrayList<>();
        this.childClickListener = childClickListener; // Сохраняем слушатель
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        ParentItem item = parentItems.get(position);
        holder.textView.setText(item.getTitle());
        if (item.isExpanded()) {
            expand(holder.recyclerChild);
        } else {
            collapse(holder.recyclerChild);
        }

        // Настройка вложенного RecyclerView второго уровня
        ChildAdapter childAdapter = new ChildAdapter(item.getChildItems(), childClickListener); // Передаем слушатель
        holder.recyclerChild.setAdapter(childAdapter);
        holder.recyclerChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        // Управление видимостью вложенного списка
        holder.recyclerChild.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

        // Клик по родительскому элементу — раскрыть/скрыть
        holder.itemView.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return parentItems.size();
    }

    static class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RecyclerView recyclerChild;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textParentTitle);
            recyclerChild = itemView.findViewById(R.id.recyclerChild);
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