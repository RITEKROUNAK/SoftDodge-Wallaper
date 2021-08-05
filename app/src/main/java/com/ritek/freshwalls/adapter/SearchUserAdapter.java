package com.ritek.freshwalls.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.User;
import com.ritek.freshwalls.ui.activities.UserActivity;
import java.util.List;

public class SearchUserAdapter extends   RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder>{
        private List<User> userList;
        private Activity activity;
        public SearchUserAdapter(List<User> userList, Activity activity) {
            this.userList = userList;
            this.activity = activity;
        }

        @Override
        public SearchUserAdapter.SearchUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, null);
            SearchUserAdapter.SearchUserHolder mh = new SearchUserAdapter.SearchUserHolder(v);
            return mh;
        }

        @Override
        public void onBindViewHolder(SearchUserAdapter.SearchUserHolder holder, final int position) {
            if (!userList.get(position).getImage().isEmpty()){
                Picasso.with(activity).load(userList.get(position).getImage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_follow_iten);
            }else{
                Picasso.with(activity).load(R.drawable.profile).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.image_view_follow_iten);
            }
            holder.text_view_follow_itme_label.setText(userList.get(position).getName());
            holder.relative_layout_subscribte_item.setOnClickListener(new View.OnClickListener() {
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

        public class SearchUserHolder extends RecyclerView.ViewHolder {

            private final ImageView image_view_follow_iten;
            private final TextView text_view_follow_itme_label;
            private final RelativeLayout relative_layout_subscribte_item;

            public SearchUserHolder(View itemView) {
                super(itemView);
                this.relative_layout_subscribte_item=(RelativeLayout) itemView.findViewById(R.id.relative_layout_subscribte_item);
                this.image_view_follow_iten=(ImageView) itemView.findViewById(R.id.image_view_follow_iten);
                this.text_view_follow_itme_label=(TextView) itemView.findViewById(R.id.text_view_follow_itme_label);
            }
        }
    }


