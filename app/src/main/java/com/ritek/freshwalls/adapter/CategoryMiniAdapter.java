package com.ritek.freshwalls.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Category;
import com.ritek.freshwalls.ui.activities.CategoryActivity;

import java.util.List;

public class CategoryMiniAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Category> categoryList;
    private Activity activity;

    public CategoryMiniAdapter(List<Category> categoryList, Activity activity) {
        this.categoryList = categoryList;
        this.activity = activity;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_category_mini, parent, false);
                viewHolder = new CategoryMiniAdapter.CategoryHolder(v1);
                break;
            }
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder_parent,final int position) {
        switch (getItemViewType(position)) {
            case 1: {
                final CategoryMiniAdapter.CategoryHolder holder = (CategoryMiniAdapter.CategoryHolder) holder_parent;
                Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
                holder.text_view_item_category_title.setTypeface(face);

                holder.getTextView().setText(categoryList.get(position).getTitle());
                holder.text_view_item_category_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent  =  new Intent(activity.getApplicationContext(), CategoryActivity.class);
                            intent.putExtra("id",categoryList.get(position).getId());
                            intent.putExtra("title",categoryList.get(position).getTitle());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                        }catch (IndexOutOfBoundsException e){

                        }

                    }
                });
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView text_view_item_category_title;
        public CategoryHolder(View itemView) {
            super(itemView);
            this.text_view_item_category_title=(TextView) itemView.findViewById(R.id.text_view_item_category_title);
        }
        public TextView getTextView() {
            return text_view_item_category_title;
        }
    }
    @Override
    public int getItemViewType(int position) {
        return categoryList.get(position).getViewType();
    }
}
