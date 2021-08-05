package com.ritek.freshwalls.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Pack;
import com.ritek.freshwalls.ui.activities.PackActivity;

import java.util.List;

public class PackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Pack> packList;
    private Activity activity;

    public PackAdapter(List<Pack> packList, Activity activity) {
        this.packList = packList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_pack, parent, false);
                viewHolder = new PackHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_empty_pack, parent, false);
                viewHolder = new EmptyHolder(v2);
                break;
            }
        }
        return  viewHolder;
    }
    public class PackHolder extends RecyclerView.ViewHolder {
        private final ImageView image_view_item_pack_image_1;
        private final ImageView image_view_item_pack_image_2;
        private final ImageView image_view_item_pack_image_3;
        public TextView text_view_item_pack_title ;
        public CardView card_view_tag_item_global ;

        public PackHolder(View itemView) {
            super(itemView);
            this.image_view_item_pack_image_1=(ImageView) itemView.findViewById(R.id.image_view_item_pack_image_1);
            this.image_view_item_pack_image_2=(ImageView) itemView.findViewById(R.id.image_view_item_pack_image_2);
            this.image_view_item_pack_image_3=(ImageView) itemView.findViewById(R.id.image_view_item_pack_image_3);
            this.text_view_item_pack_title=(TextView) itemView.findViewById(R.id.text_view_item_pack_title);
        }
    }
    public class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_parent,final int position) {
        switch (getItemViewType(position)) {
            case 1: {
                PackHolder holder  =(PackHolder) holder_parent;
                Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
                holder.text_view_item_pack_title.setTypeface(face);
                holder.text_view_item_pack_title.setText(packList.get(position).getTitle());
                if (packList.get(position).getImages().size()>2)
                    Picasso.with(activity).load(packList.get(position).getImages().get(2)).into(holder.image_view_item_pack_image_3);
                else
                    Picasso.with(activity).load(packList.get(position).getImages().get(0)).into(holder.image_view_item_pack_image_3);

                if (packList.get(position).getImages().size()>1)
                    Picasso.with(activity).load(packList.get(position).getImages().get(1)).into(holder.image_view_item_pack_image_2);
                else
                    Picasso.with(activity).load(packList.get(position).getImages().get(0)).into(holder.image_view_item_pack_image_2);

                if (packList.get(position).getImages().size()>0)
                    Picasso.with(activity).load(packList.get(position).getImages().get(0)).into(holder.image_view_item_pack_image_1);

                holder.text_view_item_pack_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent  =  new Intent(activity.getApplicationContext(), PackActivity.class);
                            intent.putExtra("id",packList.get(position).getId());
                            intent.putExtra("title",packList.get(position).getTitle());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                        }catch (IndexOutOfBoundsException e){

                        }

                    }
                });
                break;
            }
            case 2: {
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return packList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return packList.get(position).getViewType();
    }
}
