package com.ritek.freshwalls.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.User;
import com.ritek.freshwalls.ui.activities.UserActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hsn on 21/07/2018.
 */


public class FollowAdapter extends  RecyclerView.Adapter<FollowAdapter.ColorHolder>{
    private List<User> userList;
    private Activity activity;
    public FollowAdapter(List<User> userList, Activity activity) {
        this.userList = userList;
        this.activity = activity;
    }

    @Override
    public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following, null);
        ColorHolder mh = new ColorHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ColorHolder holder,final int position) {
        if (!userList.get(position).getImage().isEmpty()){
            Picasso.with(activity).load(userList.get(position).getImage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_follow_iten);
            Picasso.with(activity).load(userList.get(position).getThum()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_item_subscribe_thum);
        }else{
            Picasso.with(activity).load(R.drawable.profile).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_follow_iten);
            Picasso.with(activity).load(R.drawable.profile).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_item_subscribe_thum);
        }
        holder.text_view_follow_itme_label.setText(userList.get(position).getLabel());
        holder.image_view_follow_iten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(activity.getApplicationContext(), UserActivity.class);
                intent.putExtra("id",userList.get(position).getId());
                intent.putExtra("image",userList.get(position).getImage());
                intent.putExtra("name",userList.get(position).getName());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ColorHolder extends RecyclerView.ViewHolder {

        private final CircleImageView image_view_follow_iten;
        private final TextView text_view_follow_itme_label;
        private final ImageView image_view_item_subscribe_thum;

        public ColorHolder(View itemView) {
            super(itemView);
            this.image_view_item_subscribe_thum=(ImageView) itemView.findViewById(R.id.image_view_item_subscribe_thum);
            this.image_view_follow_iten=(CircleImageView) itemView.findViewById(R.id.image_view_follow_iten);
            this.text_view_follow_itme_label=(TextView) itemView.findViewById(R.id.text_view_follow_itme_label);
        }
    }
}
