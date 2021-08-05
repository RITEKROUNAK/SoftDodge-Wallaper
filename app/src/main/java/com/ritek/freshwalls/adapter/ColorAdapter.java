package com.ritek.freshwalls.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Color;
import com.ritek.freshwalls.ui.activities.ColorActivity;

import java.util.List;

public class ColorAdapter extends  RecyclerView.Adapter<ColorAdapter.ColorHolder>{
    private List<Color> colorList;
    private Activity activity;
    public ColorAdapter(List<Color> colorList, Activity activity) {
        this.colorList = colorList;
        this.activity = activity;
    }

    @Override
    public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ColorHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_color_tag, parent, false);
                viewHolder = new ColorHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_color_tag_mini, parent, false);
                viewHolder = new ColorHolder(v2);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ColorHolder holder,final int position) {
        Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
        holder.text_view_item_tag_item.setTypeface(face);

        holder.text_view_item_tag_item.setText(colorList.get(position).getTitle());
        if (!colorList.get(position).getTitle().toLowerCase().equals("white") && !colorList.get(position).getCode().toLowerCase().contains("ffffff") )
            holder.card_view_tag_item_global.setCardBackgroundColor(android.graphics.Color.parseColor(colorList.get(position).getCode()));
        else
            holder.card_view_tag_item_global.setCardBackgroundColor(android.graphics.Color.parseColor("#CCCCCC"));

        holder.card_view_tag_item_global.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent_video  =  new Intent(activity, ColorActivity.class);
                        intent_video.putExtra("id",colorList.get(position).getId());
                        intent_video.putExtra("title",colorList.get(position).getTitle());
                        intent_video.putExtra("code",colorList.get(position).getCode());
                        activity.startActivity(intent_video);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }catch (IndexOutOfBoundsException e){

                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }
    public class ColorHolder extends RecyclerView.ViewHolder {
        public TextView text_view_item_tag_item ;
        public CardView card_view_tag_item_global ;

        public ColorHolder(View itemView) {
            super(itemView);
            this.card_view_tag_item_global=(CardView) itemView.findViewById(R.id.card_view_tag_item_global);
            this.text_view_item_tag_item=(TextView) itemView.findViewById(R.id.text_view_item_tag_item);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return colorList.get(position).getViewType();
    }
}
