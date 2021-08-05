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
import com.ritek.freshwalls.entity.Tag;
import com.ritek.freshwalls.ui.activities.SearchActivity;

import java.util.List;

/**
 * Created by hsn on 05/04/2018.
 */

public class TagAdapter extends  RecyclerView.Adapter<TagAdapter.TagHolder>{
    private List<Tag> tagList;
    private Activity activity;
    private Boolean tags = false;
    public TagAdapter(List<Tag> tagList, Activity activity) {
        this.tagList = tagList;
        this.activity = activity;
    }

    public TagAdapter(List<Tag> tagList, Activity activity, Boolean tags) {
        this.tagList = tagList;
        this.activity = activity;
        this.tags = tags;
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag , null);
        TagHolder mh = new TagHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(TagHolder holder,final int position) {
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
        holder.text_view_item_tag_item.setTypeface(face);

        holder.text_view_item_tag_item.setText(tagList.get(position).getName());
        if (!tags) {
            holder.text_view_item_tag_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent_video  =  new Intent(activity, SearchActivity.class);
                        intent_video.putExtra("query",tagList.get(position).getName());
                        activity.startActivity(intent_video);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }catch (IndexOutOfBoundsException e){

                    }
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class TagHolder extends RecyclerView.ViewHolder {
        public TextView text_view_item_tag_item ;

        public TagHolder(View itemView) {
            super(itemView);
            this.text_view_item_tag_item=(TextView) itemView.findViewById(R.id.text_view_item_tag_item);
        }
    }
}
