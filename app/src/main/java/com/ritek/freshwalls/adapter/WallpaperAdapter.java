package com.ritek.freshwalls.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.entity.Category;
import com.ritek.freshwalls.entity.Pack;
import com.ritek.freshwalls.entity.Tag;
import com.ritek.freshwalls.entity.Wallpaper;
import com.ritek.freshwalls.entity.Slide;
import com.ritek.freshwalls.entity.User;
import com.ritek.freshwalls.manager.PrefManager;
import com.ritek.freshwalls.ui.activities.GifActivity;
import com.ritek.freshwalls.ui.activities.PacksActivity;
import com.ritek.freshwalls.ui.activities.VideoActivity;
import com.ritek.freshwalls.ui.activities.WallActivity;
import com.ritek.freshwalls.ui.view.ClickableViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by hsn on 27/11/2017.
 */

public class WallpaperAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private  List<Pack> packList = new ArrayList<>();
    private  List<Category> categoryList = new ArrayList<>();
    private  List<User> userList = new ArrayList<>();
    private  Runnable runnable;
    private boolean favorites = false;
    private boolean downloads =  false;

    private List<Wallpaper> wallpaperList;
    private List<Tag> tagList;
    private Activity activity;
    private List<Slide> slideList= new ArrayList<>();
    private SlideAdapter slide_adapter;
    private LinearLayoutManager linearLayoutManager;

    private TagAdapter tagAdapter;
    private FollowAdapter followAdapter;
    private LinearLayoutManager linearLayoutManagerTags;
    private LinearLayoutManager linearLayoutManagerSearch;
    private SearchUserAdapter searchUserAdapter;
    private LinearLayoutManager linearLayoutManagerPack;
    private PackAdapter packAdapter;
    private LinearLayoutManager linearLayoutManagerMiniCategory;
    private CategoryMiniAdapter categoryMiniAdapter;

    private InterstitialAd admobInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;

    private Integer position_selected;

    // private Timer mTimer;
    public WallpaperAdapter(
            List<Wallpaper> wallpaperList,
            List<Slide> slideList,
            Activity activity,
            Boolean favorites,
            Boolean downloads
    ) {
        this.wallpaperList = wallpaperList;
        this.activity=activity;
        this.slideList = slideList;
        this.downloads=downloads;
        this.favorites=favorites;
    }
    public WallpaperAdapter(
            List<Wallpaper> wallpaperList,
            List<Slide> slideList,
            List<Category> categoryList,
            List<Pack> packList,
            Activity activity
    ) {
        this.wallpaperList = wallpaperList;
        this.slideList = slideList;
        this.categoryList = categoryList;
        this.packList = packList;
        this.activity=activity;
    }
    public WallpaperAdapter(
            List<Wallpaper> wallpaperList,
            List<Tag> tagList,
            Activity activity
    ) {
        this.wallpaperList = wallpaperList;
        this.activity=activity;
        this.tagList = tagList;
    }

    public WallpaperAdapter(
            List<Wallpaper> wallpaperList,
            List<Slide> slideList,
            Activity activity,
            Boolean favorites
    ) {
        this.wallpaperList = wallpaperList;
        this.activity=activity;
        this.slideList = slideList;
        this.favorites=favorites;
    }
    public WallpaperAdapter(
            List<Wallpaper> wallpaperList,
            List<Slide> slideList,
            Activity activity,
            Boolean favorites,
            List<User> userList,
            List<Category> categoryList
    ) {
        this.wallpaperList = wallpaperList;
        this.activity=activity;
        this.slideList = slideList;
        this.favorites=favorites;

        this.userList = userList;
        this.categoryList = categoryList;
    }

    public WallpaperAdapter(
            List<Wallpaper> wallpaperList,
            List<Slide> slideList,
            Activity activity,
            Boolean favorites,
            List<User> userList
    ) {
        this.wallpaperList = wallpaperList;
        this.activity=activity;
        this.slideList = slideList;
        this.favorites=favorites;
        this.userList = userList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_wallpaper, parent, false);
                viewHolder = new WallpaperHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_slide, parent, false);
                viewHolder = new SlideHolder(v2);
                break;
            }
            case 3: {
                View v3 = inflater.inflate(R.layout.item_followings, parent, false);
                viewHolder = new FollowHolder(v3);
                break;
            }
            case 4:{
                View v4 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder = new FacebookNativeHolder(v4);
                break;
            }
            case 5:{
                View v5 = inflater.inflate(R.layout.item_categories_mini, parent, false);
                viewHolder = new CategoryMiniHolder(v5);
                break;
            }
            case 6:{
                View v6 = inflater.inflate(R.layout.item_tags, parent, false);
                viewHolder = new TagsHolder(v6);
                break;
            }
            case 7: {
                View v7 = inflater.inflate(R.layout.item_users_search, parent, false);
                viewHolder = new SearchUserListHolder(v7);
                break;
            }
            case 8: {
                View v8 = inflater.inflate(R.layout.item_packs, parent, false);
                viewHolder = new PacksHolder(v8);
                break;
            }
            case 9: {
                View v9 = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder = new AdmobNativeHolder(v9);
                break;
            }
        }
        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {
        if (wallpaperList.get(position) == null){
            return  1;
        }else{
            return wallpaperList.get(position).getViewType();
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_parent, final int position) {

        switch (wallpaperList.get(position).getViewType()) {
            case 1: {
                final WallpaperHolder holder = (WallpaperHolder) holder_parent;
                Picasso.with(activity).load(wallpaperList.get(position).getThumbnail()).placeholder(activity.getResources().getDrawable(R.drawable.placeholder)).error(activity.getResources().getDrawable(R.drawable.placeholder)).resize(400,500).centerCrop().into(holder.image_view_wallpaper_item);
                holder.linear_layout_wallpaper_item.setBackgroundColor(Color.parseColor("#" + wallpaperList.get(position).getColor().replace("#","")));
                holder.text_view_wallpaper_item_title.setText(wallpaperList.get(position).getTitle());
                holder.text_view_wallpaper_item_user.setText(wallpaperList.get(position).getUser());
                holder.linear_layout_wallpaper_item_global.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        PrefManager prefManager= new PrefManager(activity);




                        if(checkSUBSCRIBED()){
                            selectOperation(position);
                        }else{
                            if( prefManager.getString("ADMIN_INTERSTITIAL_TYPE").equals("ADMOB")){
                                requestAdmobInterstitial();
                                if(prefManager.getInt("ADMIN_INTERSTITIAL_CLICKS")==prefManager.getInt("ADMOB_INTERSTITIAL_COUNT_CLICKS")){
                                    if (admobInterstitialAd.isLoaded()) {
                                        prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",0);
                                        admobInterstitialAd.show();
                                        admobInterstitialAd.setAdListener(new AdListener() {
                                            @Override
                                            public void onAdClosed() {
                                                requestAdmobInterstitial();
                                                selectOperation(position);
                                            }
                                        });
                                    }else{
                                        selectOperation(position);
                                        requestAdmobInterstitial();
                                    }
                                }else{
                                    selectOperation(position);
                                    prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",prefManager.getInt("ADMOB_INTERSTITIAL_COUNT_CLICKS")+1);
                                }
                            }else if(prefManager.getString("ADMIN_INTERSTITIAL_TYPE").equals("FACEBOOK")){
                                requestFacebookInterstitial();
                                if(prefManager.getInt("ADMIN_INTERSTITIAL_CLICKS")==prefManager.getInt("ADMOB_INTERSTITIAL_COUNT_CLICKS")){
                                    if (facebookInterstitialAd.isAdLoaded()) {
                                        prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",0);
                                        facebookInterstitialAd.show();
                                        position_selected = holder.getAdapterPosition();
                                    }else{
                                        selectOperation(position);
                                        requestFacebookInterstitial();
                                    }
                                }else{
                                    selectOperation(position);
                                    prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",prefManager.getInt("ADMOB_INTERSTITIAL_COUNT_CLICKS")+1);
                                }
                            }else if(prefManager.getString("ADMIN_INTERSTITIAL_TYPE").equals("BOTH")){
                                requestAdmobInterstitial();
                                requestFacebookInterstitial();
                                if(prefManager.getInt("ADMIN_INTERSTITIAL_CLICKS")==prefManager.getInt("ADMOB_INTERSTITIAL_COUNT_CLICKS")) {
                                    if (prefManager.getString("AD_INTERSTITIAL_SHOW_TYPE").equals("ADMOB")){
                                        if (admobInterstitialAd.isLoaded()) {
                                            prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",0);
                                            prefManager.setString("AD_INTERSTITIAL_SHOW_TYPE","FACEBOOK");
                                            admobInterstitialAd.show();
                                            admobInterstitialAd.setAdListener(new AdListener(){
                                                @Override
                                                public void onAdClosed() {
                                                    super.onAdClosed();
                                                    selectOperation(position);
                                                    requestFacebookInterstitial();
                                                }
                                            });
                                        }else{
                                            selectOperation(position);
                                            requestFacebookInterstitial();
                                        }
                                    }else{
                                        if (facebookInterstitialAd.isAdLoaded()) {
                                            prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",0);
                                            prefManager.setString("AD_INTERSTITIAL_SHOW_TYPE","ADMOB");
                                            facebookInterstitialAd.show();
                                            position_selected = holder.getAdapterPosition();
                                        }else{
                                            selectOperation(position);
                                            requestFacebookInterstitial();
                                        }
                                    }
                                }else{
                                    selectOperation(position);
                                    prefManager.setInt("ADMOB_INTERSTITIAL_COUNT_CLICKS",prefManager.getInt("ADMOB_INTERSTITIAL_COUNT_CLICKS")+1);
                                }
                            }else{
                                selectOperation(position);
                            }
                        }

                    }
                });
                if (wallpaperList.get(position).getPremium()) {
                    holder.image_view_item_wallpaper_premium.setVisibility(View.VISIBLE);
                } else {
                    holder.image_view_item_wallpaper_premium.setVisibility(View.GONE);
                }
                if (wallpaperList.get(position).getReview()) {
                    holder.text_view_review_wallpaper_item.setVisibility(View.VISIBLE);
                } else {
                    holder.text_view_review_wallpaper_item.setVisibility(View.GONE);
                }
                if (wallpaperList.get(position).getKind().equals("video")) {
                    holder.relative_layout_wallpaper_item_video.setVisibility(View.VISIBLE);
                    holder.relative_layout_wallpaper_item_gif.setVisibility(View.GONE);
                } else if (wallpaperList.get(position).getKind().equals("gif")){
                    holder.relative_layout_wallpaper_item_video.setVisibility(View.GONE);
                    holder.relative_layout_wallpaper_item_gif.setVisibility(View.VISIBLE);
                }else{
                    holder.relative_layout_wallpaper_item_video.setVisibility(View.GONE);
                    holder.relative_layout_wallpaper_item_gif.setVisibility(View.GONE);
                }
                List<Wallpaper> favorites_list =Hawk.get("favorite");
                Boolean exist = false;
                if (favorites_list == null) {
                    favorites_list = new ArrayList<>();
                }

                for (int i = 0; i < favorites_list.size(); i++) {
                    if (favorites_list.get(i).getId().equals(wallpaperList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist){
                    holder.image_view_fav_wallpaper_item.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_done));
                }else{
                    holder.image_view_fav_wallpaper_item.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_empty));
                }
                holder.image_view_fav_wallpaper_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Wallpaper> favorites_list =Hawk.get("favorite");
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        int fav_position = -1;
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(wallpaperList.get(position).getId())) {
                                exist = true;
                                fav_position = i;
                            }
                        }
                        if (exist == false) {
                            favorites_list.add(wallpaperList.get(position));
                            Hawk.put("favorite",favorites_list);
                            holder.image_view_fav_wallpaper_item.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_done));

                        }else{
                            favorites_list.remove(fav_position);
                            Hawk.put("favorite",favorites_list);
                            holder.image_view_fav_wallpaper_item.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_empty));
                            if (favorites) {
                                wallpaperList.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                            }

                        }

                    }
                });
            }
            break;
            case 2: {
                final SlideHolder holder = (SlideHolder) holder_parent;

                slide_adapter = new SlideAdapter(activity, slideList);
                holder.view_pager_slide.setAdapter(slide_adapter);
                holder.view_pager_slide.setOffscreenPageLimit(1);

                holder.view_pager_slide.setClipToPadding(false);
                holder.view_pager_slide.setPageMargin(0);
                holder.view_pager_indicator.setupWithViewPager(holder.view_pager_slide);

                holder.view_pager_slide.setCurrentItem(slideList.size() / 2);
                slide_adapter.notifyDataSetChanged();
            }
            break;
            case 3: {
                final FollowHolder holder = (FollowHolder) holder_parent;
                this.linearLayoutManager=  new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
                this.followAdapter =new FollowAdapter(userList,activity);
                holder.recycle_view_follow_items.setHasFixedSize(true);
                holder.recycle_view_follow_items.setAdapter(followAdapter);
                holder.recycle_view_follow_items.setLayoutManager(linearLayoutManager);
                followAdapter.notifyDataSetChanged();
                break;
            }
            case 5:{
                final CategoryMiniHolder holder = (CategoryMiniHolder) holder_parent;

                this.linearLayoutManagerMiniCategory=  new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                this.categoryMiniAdapter =new CategoryMiniAdapter(categoryList,activity);
                holder.recycler_view_item_categories.setHasFixedSize(true);
                holder.recycler_view_item_categories.setAdapter(categoryMiniAdapter);
                holder.recycler_view_item_categories.setLayoutManager(linearLayoutManagerMiniCategory);
                categoryMiniAdapter.notifyDataSetChanged();
                break;
            }
            case 6: {
                final TagsHolder holder = (TagsHolder) holder_parent;
                this.linearLayoutManagerTags=  new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
                this.tagAdapter =new TagAdapter(tagList,activity);
                holder.recycle_view_tags_items.setHasFixedSize(true);
                holder.recycle_view_tags_items.setAdapter(tagAdapter);
                holder.recycle_view_tags_items.setLayoutManager(linearLayoutManagerTags);
                tagAdapter.notifyDataSetChanged();
                break;
            }
            case 7: {

                final SearchUserListHolder holder = (SearchUserListHolder) holder_parent;
                this.linearLayoutManagerSearch=  new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
                this.searchUserAdapter =new SearchUserAdapter(userList,activity);
                holder.recycle_view_users_items.setHasFixedSize(true);
                holder.recycle_view_users_items.setAdapter(searchUserAdapter);
                holder.recycle_view_users_items.setLayoutManager(linearLayoutManagerSearch);
                searchUserAdapter.notifyDataSetChanged();
                break;
            }
            case 8: {
                final PacksHolder holder = (PacksHolder) holder_parent;
                holder.text_view_item_packs_title.setText("More than " + (packList.size()-1) +" packs");
                Picasso.with(activity).load(packList.get(1).getImages().get(0)).into(holder.image_view_item_packs_cover);

                this.linearLayoutManagerPack=  new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
                this.packAdapter =new PackAdapter(packList,activity);
                holder.recycler_view_item_packs.setHasFixedSize(true);
                holder.recycler_view_item_packs.setAdapter(packAdapter);
                holder.recycler_view_item_packs.setLayoutManager(linearLayoutManagerPack);
                packAdapter.notifyDataSetChanged();
                holder.relative_layout_item_packs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(activity,PacksActivity.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
                break;
            }
            case 9:{
                final AdmobNativeHolder holder = (AdmobNativeHolder) holder_parent;

                holder.adLoader.loadAd(new AdRequest.Builder().build());

                break;
            }
        }
    }
    private class SlideHolder extends RecyclerView.ViewHolder {
        private final ViewPagerIndicator view_pager_indicator;
        private final ClickableViewPager view_pager_slide;
        public SlideHolder(View itemView) {
            super(itemView);
            this.view_pager_indicator=(ViewPagerIndicator) itemView.findViewById(R.id.view_pager_indicator);
            this.view_pager_slide=(ClickableViewPager) itemView.findViewById(R.id.view_pager_slide);


        }

    }
    public static class SearchUserListHolder extends  RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_users_items;
        public SearchUserListHolder(View view) {
            super(view);
            recycle_view_users_items = (RecyclerView) itemView.findViewById(R.id.recycle_view_users_items);
        }
    }
    public static class WallpaperHolder extends RecyclerView.ViewHolder {

        private final ImageView image_view_wallpaper_item;
        private final LinearLayout linear_layout_wallpaper_item_global;
        private final LinearLayout linear_layout_wallpaper_item;
        private final TextView text_view_review_wallpaper_item;
        private final ImageView image_view_item_wallpaper_premium;
        private final TextView text_view_wallpaper_item_title;
        private final TextView text_view_wallpaper_item_user;
        private final RelativeLayout relative_layout_wallpaper_item_video;
        public final ImageView image_view_fav_wallpaper_item;
        public final RelativeLayout relative_layout_wallpaper_item_gif;

        public WallpaperHolder(View itemView) {
            super(itemView);
            this.image_view_fav_wallpaper_item =  itemView.findViewById(R.id.image_view_fav_wallpaper_item);
            this.image_view_wallpaper_item =  itemView.findViewById(R.id.image_view_wallpaper_item);
            this.image_view_item_wallpaper_premium =  itemView.findViewById(R.id.image_view_item_wallpaper_premium);
            this.linear_layout_wallpaper_item =  itemView.findViewById(R.id.linear_layout_wallpaper_item);
            this.text_view_review_wallpaper_item =  itemView.findViewById(R.id.text_view_review_wallpaper_item);
            this.text_view_wallpaper_item_title =  itemView.findViewById(R.id.text_view_wallpaper_item_title);
            this.text_view_wallpaper_item_user =  itemView.findViewById(R.id.text_view_wallpaper_item_user);
            this.relative_layout_wallpaper_item_video =  itemView.findViewById(R.id.relative_layout_wallpaper_item_video);
            this.relative_layout_wallpaper_item_gif =  itemView.findViewById(R.id.relative_layout_wallpaper_item_gif);
            this.linear_layout_wallpaper_item_global =  itemView.findViewById(R.id.linear_layout_wallpaper_item_global);
        }
    }
    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class AdmobNativeHolder extends RecyclerView.ViewHolder {
        private final AdLoader adLoader;
        private UnifiedNativeAd nativeAd;
        private FrameLayout frameLayout;

        public AdmobNativeHolder(@NonNull View itemView) {
            super(itemView);
            PrefManager prefManager= new PrefManager(activity);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.fl_adplaceholder);
            AdLoader.Builder builder = new AdLoader.Builder(activity, prefManager.getString("ADMIN_NATIVE_ADMOB_ID"));

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;

                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }

            });

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            this.adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {


                }
            }).build();

        }
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }


    public  class FacebookNativeHolder extends  RecyclerView.ViewHolder {
        private final String TAG = "WALLPAPERADAPTER";
        private com.facebook.ads.NativeAdLayout nativeAdLayout;
        private LinearLayout adView;
        private NativeAd nativeAd;
        public FacebookNativeHolder(View view) {
            super(view);
            loadNativeAd(view);
        }

        private void loadNativeAd(final View view) {
            // Instantiate a NativeAd object.
            // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
            // now, while you are testing and replace it later when you have signed up.
            // While you are using this temporary code you will only get test ads and if you release
            // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
            PrefManager prefManager= new PrefManager(activity);

            nativeAd = new NativeAd(activity,prefManager.getString("ADMIN_NATIVE_FACEBOOK_ID"));
            NativeAdListener nativeAdListener=new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets
                    Log.e(TAG, "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Native ad failed to load
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                    // Race condition, load() called again before last ad was displayed
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                   /* NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                            .setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark))
                            .setTitleTextColor(Color.WHITE)
                            .setDescriptionTextColor(Color.WHITE)
                            .setButtonColor(Color.WHITE);

                    View adView = NativeAdView.render(activity, nativeAd, NativeAdView.Type.HEIGHT_300, viewAttributes);

                    LinearLayout nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
                    nativeAdContainer.addView(adView);*/
                    // Inflate Native Ad into Container
                    inflateAd(nativeAd,view);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!");
                }
            };

            // Request an ad
            nativeAd.loadAd(
                    nativeAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
        }

        private void inflateAd(NativeAd nativeAd,View view) {

            nativeAd.unregisterView();

            // Add the Ad view into the ad container.
            nativeAdLayout = view.findViewById(R.id.native_ad_container);
            LayoutInflater inflater = LayoutInflater.from(activity);
            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1, nativeAdLayout, false);
            nativeAdLayout.addView(adView);

            // Add the AdOptionsView
            LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
            AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);

            // Create native UI using the ad metadata.
            MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            // Create a list of clickable views
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(
                    adView, nativeAdMedia, nativeAdIcon, clickableViews);




        }

    }
    public static class FollowHolder extends  RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_follow_items;
        public FollowHolder(View view) {
            super(view);
            recycle_view_follow_items = (RecyclerView) itemView.findViewById(R.id.recycle_view_follow_items);
        }
    }
    public static class TagsHolder extends  RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_tags_items;
        public TagsHolder(View view) {
            super(view);
            recycle_view_tags_items = (RecyclerView) itemView.findViewById(R.id.recycle_view_tags_items);
        }
    }
    public  class CategoryMiniHolder extends RecyclerView.ViewHolder {
        public RecyclerView recycler_view_item_categories;

        public CategoryMiniHolder(View view) {
            super(view);
            this.recycler_view_item_categories = (RecyclerView) itemView.findViewById(R.id.recycler_view_item_categories);
        }
    }
    public  class PacksHolder extends RecyclerView.ViewHolder {


        private final RecyclerView recycler_view_item_packs;
        private final ImageView image_view_item_packs_cover;
        private final TextView text_view_item_packs_title;
        private final RelativeLayout relative_layout_item_packs;

        public PacksHolder(View view) {
            super(view);
            this.relative_layout_item_packs=(RelativeLayout) view.findViewById(R.id.relative_layout_item_packs);
            this.text_view_item_packs_title=(TextView) view.findViewById(R.id.text_view_item_packs_title);
            this.image_view_item_packs_cover=(ImageView) view.findViewById(R.id.image_view_item_packs_cover);
            this.recycler_view_item_packs=(RecyclerView) view.findViewById(R.id.recycler_view_item_packs);
        }
    }
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String secToTime(int sec) {
        int seconds = sec % 60;
        int minutes = sec / 60;

        if (minutes>0){
            return String.format("%01d m %02d s", minutes, seconds);
        }else{
            return String.format("%01d s", seconds);
        }
    }
    public boolean checkSUBSCRIBED(){
        PrefManager prefManager= new PrefManager(activity);
        if (!prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        return true;
    }
    private void requestFacebookInterstitial() {
        if (facebookInterstitialAd==null) {
            PrefManager prefManager= new PrefManager(activity);
            facebookInterstitialAd = new com.facebook.ads.InterstitialAd(activity, prefManager.getString("ADMIN_INTERSTITIAL_FACEBOOK_ID"));
        }
        if (!facebookInterstitialAd.isAdLoaded()){
            InterstitialAdListener interstitialAdListener =   new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {}
                @Override
                public void onInterstitialDismissed(Ad ad) {
                    selectOperation(position_selected);
                }
                @Override
                public void onError(Ad ad, AdError adError) {}
                @Override
                public void onAdLoaded(Ad ad) {}
                @Override
                public void onAdClicked(Ad ad) {}
                @Override
                public void onLoggingImpression(Ad ad) {}
            };
            facebookInterstitialAd.loadAd(
                    facebookInterstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());

        }
    }
    public void selectOperation(Integer position){
        Intent intent;
        if (wallpaperList.get(position).getKind().equals("video"))
            intent = new Intent(activity.getApplicationContext(), VideoActivity.class);
        else if (wallpaperList.get(position).getKind().equals("gif"))
            intent = new Intent(activity.getApplicationContext(), GifActivity.class);
        else
            intent = new Intent(activity.getApplicationContext(), WallActivity.class);
        intent.putExtra("id", wallpaperList.get(position).getId());
        intent.putExtra("title", wallpaperList.get(position).getTitle());
        intent.putExtra("description", wallpaperList.get(position).getDescription());
        intent.putExtra("color", wallpaperList.get(position).getColor());
        intent.putExtra("tags", wallpaperList.get(position).getTags());
        intent.putExtra("kind", wallpaperList.get(position).getKind());
        intent.putExtra("premium", wallpaperList.get(position).getPremium());
        intent.putExtra("review", wallpaperList.get(position).getReview());

        intent.putExtra("size", wallpaperList.get(position).getSize());
        intent.putExtra("resolution", wallpaperList.get(position).getResolution());
        intent.putExtra("created", wallpaperList.get(position).getCreated());
        intent.putExtra("sets", wallpaperList.get(position).getSets());
        intent.putExtra("shares", wallpaperList.get(position).getShares());
        intent.putExtra("views", wallpaperList.get(position).getViews());
        intent.putExtra("downloads", wallpaperList.get(position).getDownloads());
        intent.putExtra("type", wallpaperList.get(position).getType());
        intent.putExtra("extension", wallpaperList.get(position).getExtension());


        intent.putExtra("userid", wallpaperList.get(position).getUserid());
        intent.putExtra("username", wallpaperList.get(position).getUser());
        intent.putExtra("userimage", wallpaperList.get(position).getUserimage());
        intent.putExtra("trusted", wallpaperList.get(position).getTrusted());

        intent.putExtra("comment", wallpaperList.get(position).getComment());
        intent.putExtra("comments", wallpaperList.get(position).getComments());

        intent.putExtra("comments", wallpaperList.get(position).getComments());

        intent.putExtra("original", wallpaperList.get(position).getOriginal());
        intent.putExtra("thumbnail", wallpaperList.get(position).getThumbnail());
        intent.putExtra("image", wallpaperList.get(position).getImage());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
    }
    private void requestAdmobInterstitial() {
        if (admobInterstitialAd==null){
            PrefManager prefManager= new PrefManager(activity);
            admobInterstitialAd = new InterstitialAd(activity.getApplicationContext());
            admobInterstitialAd.setAdUnitId(prefManager.getString("ADMIN_INTERSTITIAL_ADMOB_ID"));
        }
        if (!admobInterstitialAd.isLoaded()){
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            admobInterstitialAd.loadAd(adRequest);
        }
    }
}
