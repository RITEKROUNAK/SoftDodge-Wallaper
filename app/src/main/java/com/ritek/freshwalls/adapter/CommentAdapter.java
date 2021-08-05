package com.ritek.freshwalls.adapter;


import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Comment;
import com.ritek.freshwalls.ui.activities.UserActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<Comment> commentList= new ArrayList<>();
    private Activity activity;
    public CommentAdapter(List<Comment> commentList, Activity activity){
        this.activity=activity;
        this.commentList=commentList;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment
                        , null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new CommentAdapter.CommentHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder,final int position) {
        holder.text_view_time_item_comment.setText(commentList.get(position).getCreated());

        byte[] data = Base64.decode(commentList.get(position).getContent(), Base64.DEFAULT);
        String Comment_text = "";
        try {
            Comment_text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Comment_text = commentList.get(position).getContent();
        }

        holder.text_view_name_item_comment.setText(commentList.get(position).getUser());
        Picasso.with(activity).load(commentList.get(position).getImage()).placeholder(R.drawable.profile).into(holder.image_view_comment_iten);
        if (!commentList.get(position).getEnabled()){
            holder.text_view_content_item_comment.setText(activity.getResources().getString(R.string.comment_hidden));
            holder.text_view_content_item_comment.setTextColor(activity.getResources().getColor(R.color.gray_color));
        }else{
            holder.text_view_content_item_comment.setText(Comment_text);
        }
        if (commentList.get(position).getTrusted().equals("true")){
            holder.image_view_comment_item_verified.setVisibility(View.VISIBLE);
        }else{
            holder.image_view_comment_item_verified.setVisibility(View.GONE);
        }
        holder.image_view_comment_iten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,UserActivity.class);
                intent.putExtra("id",commentList.get(position).getUserid());
                intent.putExtra("name",commentList.get(position).getUser());
                intent.putExtra("trusted",commentList.get(position).getTrusted().equals("true"));
                intent.putExtra("image",commentList.get(position).getImage());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        holder.text_view_name_item_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,UserActivity.class);
                intent.putExtra("id",commentList.get(position).getUserid());
                intent.putExtra("name",commentList.get(position).getUser());
                intent.putExtra("trusted",commentList.get(position).getTrusted().equals("true"));
                intent.putExtra("image",commentList.get(position).getImage());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public static class CommentHolder extends RecyclerView.ViewHolder {
        private final TextView text_view_name_item_comment;
        private final TextView text_view_time_item_comment;
        private final TextView text_view_content_item_comment;
        private final CircleImageView image_view_comment_iten;
        private final ImageView image_view_comment_item_verified;

        public CommentHolder(View itemView) {
            super(itemView);
            this.image_view_comment_iten=(CircleImageView) itemView.findViewById(R.id.image_view_comment_iten);
            this.image_view_comment_item_verified=(ImageView) itemView.findViewById(R.id.image_view_comment_item_verified);
            this.text_view_name_item_comment=(TextView) itemView.findViewById(R.id.text_view_name_item_comment);
            this.text_view_time_item_comment=(TextView) itemView.findViewById(R.id.text_view_time_item_comment);
            this.text_view_content_item_comment=(TextView) itemView.findViewById(R.id.text_view_content_item_comment);
        }
    }
}
