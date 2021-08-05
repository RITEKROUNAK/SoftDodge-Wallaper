package com.ritek.freshwalls.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamim on 29/06/2018.
 */



public class ColorSelectAdapter extends RecyclerView.Adapter implements SelectableColorViewHolder.OnItemSelectedListener {

    private final List<Color> mValues;
    private boolean isMultiSelectionEnabled = false;
    SelectableColorViewHolder.OnItemSelectedListener listener;
    Activity activity;
    public ColorSelectAdapter(SelectableColorViewHolder.OnItemSelectedListener listener,
                              List<Color> items, boolean isMultiSelectionEnabled, Activity activity) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;
        this.activity=activity;
        mValues = new ArrayList<>();
        for (Color item : items) {
            mValues.add(item);
        }
    }

    @Override
    public SelectableColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_select, parent, false);

        return new SelectableColorViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableColorViewHolder holder = (SelectableColorViewHolder) viewHolder;
        Color selectableItem = mValues.get(position);
        String name = selectableItem.getTitle();
        holder.text_view_item_category_item_select.setText(name);
        if (!name.toLowerCase().equals("white") && !selectableItem.getCode().toLowerCase().contains("ffffff") )
            holder.card_view_category_item_select.setCardBackgroundColor(android.graphics.Color.parseColor(mValues.get(position).getCode()));

        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
            int checkMarkDrawableResId = value.resourceId;
            holder.textView.setCheckMarkDrawable(checkMarkDrawableResId);
        }

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Color> getSelectedItems() {

        List<Color> selectedItems = new ArrayList<>();
        for (Color item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableColorViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableColorViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(Color item) {

        if (!isMultiSelectionEnabled) {
            for (Color selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);

    }
}
