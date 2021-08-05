package com.ritek.freshwalls.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Category;
import com.ritek.freshwalls.entity.Color;
import com.ritek.freshwalls.ui.activities.CategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsn on 03/12/2017.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Category> categoryList;
    private List<Color> colorList =  new ArrayList<>();
    private Activity activity;
    private LinearLayoutManager linearLayoutManager;
    private ColorAdapter colorAdapter;

    public CategoryAdapter(List<Category> categoryList, Activity activity) {
        this.categoryList = categoryList;
        this.activity = activity;
    }
    public CategoryAdapter(List<Category> categoryList, List<Color> colorList, Activity activity) {
        this.colorList = colorList;
        this.categoryList = categoryList;
        this.activity = activity;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_category, parent, false);
                viewHolder = new CategoryAdapter.CategoryHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_colors, parent, false);
                viewHolder = new CategoryAdapter.TagsHolder(v2);
                break;
            }
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder_parent,final int position) {
        switch (getItemViewType(position)) {
            case 1: {
                final CategoryAdapter.CategoryHolder holder = (CategoryAdapter.CategoryHolder) holder_parent;
                Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
                holder.text_view_item_category_title.setTypeface(face);

                holder.getTextView().setText(categoryList.get(position).getTitle());

                Picasso.with(activity.getApplicationContext()).load(categoryList.get(position).getImage()).placeholder(R.drawable.placeholder).into(holder.image_view_item_category_image);
                holder.card_view_item_category.setOnClickListener(new View.OnClickListener() {
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
            case 2: {
                    final CategoryAdapter.TagsHolder holder = (CategoryAdapter.TagsHolder) holder_parent;
                    this.linearLayoutManager=  new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
                    this.colorAdapter =new ColorAdapter(colorList,activity);
                    holder.recycle_view_tags_items.setHasFixedSize(true);
                    holder.recycle_view_tags_items.setAdapter(colorAdapter);
                    holder.recycle_view_tags_items.setLayoutManager(linearLayoutManager);
                    colorAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private final ImageView image_view_item_category_image;
        private CardView card_view_item_category;
        private TextView text_view_item_category_title;
        public CategoryHolder(View itemView) {
            super(itemView);
            this.card_view_item_category=(CardView) itemView.findViewById(R.id.card_view_item_category);
            this.text_view_item_category_title=(TextView) itemView.findViewById(R.id.text_view_item_category_title);
            this.image_view_item_category_image=(ImageView) itemView.findViewById(R.id.image_view_item_category_image);
        }
        public TextView getTextView() {
            return text_view_item_category_title;
        }
    }
    public static class TagsHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_tags_items;
        public TagsHolder(View itemView) {
            super(itemView);
            this.recycle_view_tags_items=(RecyclerView) itemView.findViewById(R.id.recycle_view_tags_items);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return categoryList.get(position).getViewType();
    }
}
