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

import Items.newParentItem;
import com.example.ShopOfSportClothes.R;

import java.util.ArrayList;
import java.util.List;

public class newParentAdapter extends RecyclerView.Adapter<newParentAdapter.ParentViewHolder> {
    private List<newParentItem> newParentItems; // Исправлено на newParentItem
    private ChildAdapter.OnChildClickListener childClickListener;

    public newParentAdapter(List<newParentItem> newParentItems, ChildAdapter.OnChildClickListener childClickListener) {
        this.newParentItems = newParentItems != null ? newParentItems : new ArrayList<>();
        this.childClickListener = childClickListener;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {
        newParentItem item = newParentItems.get(position); // Исправлено на newParentItem
        holder.textView.setText(item.getTitle());

        // Настройка вложенного RecyclerView
        ChildAdapter childAdapter = new ChildAdapter(item.getChildItems(), childClickListener);
        holder.recyclerChild.setAdapter(childAdapter);
        holder.recyclerChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        // Управление видимостью
        holder.recyclerChild.setVisibility(item.isExpanded() ? View.VISIBLE : View.GONE);

        // Обработка клика по родительскому элементу
        holder.itemView.setOnClickListener(v -> {
            item.setExpanded(!item.isExpanded());
            notifyItemChanged(position);
        });
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

    @Override
    public int getItemCount() {
        return newParentItems.size();
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