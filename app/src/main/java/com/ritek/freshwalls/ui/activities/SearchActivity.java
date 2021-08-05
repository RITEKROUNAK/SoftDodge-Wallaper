package com.ritek.freshwalls.ui.activities;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.adapter.WallpaperAdapter;
import com.ritek.freshwalls.api.apiClient;
import com.ritek.freshwalls.api.apiRest;
import com.ritek.freshwalls.entity.User;
import com.ritek.freshwalls.entity.Wallpaper;
import com.ritek.freshwalls.entity.Slide;
import com.ritek.freshwalls.manager.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity {

    private Integer page = 0;
    private Boolean loaded=false;
    private Integer type_ads = 0;

    private RelativeLayout relative_layout_search_activity;
    private SwipeRefreshLayout swipe_refreshl_search_activity;
    private ImageView image_view_empty;
    private RecyclerView recycle_view_search_activity;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private GridLayoutManager gridLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> wallpaperList =new ArrayList<>();
    private List<Slide> slideList=new ArrayList<>();
    private List<User> userList =new ArrayList<>();

    private String query;




    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private boolean tabletSize;
    private Boolean native_ads_enabled = false ;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        setContentView(R.layout.activity_search);
        showAdsBanner();
        Bundle bundle = getIntent().getExtras() ;
        this.query =  bundle.getString("query");



        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(query);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initView();
        initAction();
        loadUsers();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
         prefManager= new PrefManager(getApplicationContext());

        if (!prefManager.getString("ADMIN_NATIVE_TYPE").equals("FALSE")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(prefManager.getString("ADMIN_NATIVE_LINES"));
        }
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }

        this.relative_layout_search_activity=(RelativeLayout) findViewById(R.id.relative_layout_search_activity);
        this.swipe_refreshl_search_activity=(SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_search_activity);
        this.image_view_empty=(ImageView) findViewById(R.id.image_view_empty);
        this.recycle_view_search_activity=(RecyclerView) findViewById(R.id.recycle_view_search_activity);
        this.relative_layout_load_more=(RelativeLayout) findViewById(R.id.relative_layout_load_more);
        this.linear_layout_page_error=(LinearLayout) findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) findViewById(R.id.button_try_again);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        this.gridLayoutManager=  new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);


        this.wallpaperAdapter =new WallpaperAdapter(wallpaperList,slideList,this,false,userList);
        recycle_view_search_activity.setHasFixedSize(true);
        recycle_view_search_activity.setAdapter(wallpaperAdapter);
        recycle_view_search_activity.setLayoutManager(gridLayoutManager);
    }

    private void initAction() {
        recycle_view_search_activity.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {

                    visibleItemCount    = gridLayoutManager.getChildCount();
                    totalItemCount      = gridLayoutManager.getItemCount();
                    pastVisiblesItems   = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadNextWallpapers();
                        }
                    }
                }else{

                }
            }
        });
        this.swipe_refreshl_search_activity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                page = 0;
                loading = true;
                slideList.clear();
                wallpaperList.clear();
                loadUsers();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = 0;
                page = 0;
                loading = true;
                slideList.clear();
                wallpaperList.clear();
                loadUsers();
            }
        });
    }

    private void loadNextWallpapers() {
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpapersBysearch(page,query);
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()!=0){

                        for (int i=0;i<response.body().size();i++){
                            wallpaperList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("ADMOB")) {
                                        wallpaperList.add(new Wallpaper().setViewType(9));
                                    }else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("FACEBOOK")){
                                        wallpaperList.add(new Wallpaper().setViewType(4));
                                    } else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("BOTH")){
                                        if (type_ads == 0) {
                                            wallpaperList.add(new Wallpaper().setViewType(9));
                                            type_ads = 1;
                                        }else if (type_ads == 1){
                                            wallpaperList.add(new Wallpaper().setViewType(4));
                                            type_ads = 0;
                                        }
                                    }
                                }
                            }
                        }
                        wallpaperAdapter.notifyDataSetChanged();
                        page++;
                        loading=true;
                    }
                }
                relative_layout_load_more.setVisibility(View.GONE);

            }
            @Override
            public void onFailure(Call<List<Wallpaper>> call, Throwable t) {
                relative_layout_load_more.setVisibility(View.GONE);
            }
        });
    }

    private void loadWallpapers() {
        recycle_view_search_activity.setVisibility(View.GONE);
        image_view_empty.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_search_activity.setRefreshing(true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpapersBysearch(page,query);
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                apiClient.FormatData(SearchActivity.this,response);
                if(response.isSuccessful()){
                    if (response.body().size()!=0){

                        for (int i=0;i<response.body().size();i++){
                            wallpaperList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("ADMOB")) {
                                        wallpaperList.add(new Wallpaper().setViewType(9));
                                    }else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("FACEBOOK")){
                                        wallpaperList.add(new Wallpaper().setViewType(4));
                                    } else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("BOTH")){
                                        if (type_ads == 0) {
                                            wallpaperList.add(new Wallpaper().setViewType(9));
                                            type_ads = 1;
                                        }else if (type_ads == 1){
                                            wallpaperList.add(new Wallpaper().setViewType(4));
                                            type_ads = 0;
                                        }
                                    }
                                }
                            }
                        }
                        wallpaperAdapter.notifyDataSetChanged();
                        page++;
                        loaded=true;

                        recycle_view_search_activity.setVisibility(View.VISIBLE);
                        image_view_empty.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        recycle_view_search_activity.setVisibility(View.GONE);
                        image_view_empty.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }
                }else{
                    recycle_view_search_activity.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_search_activity.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Wallpaper>> call, Throwable t) {
                recycle_view_search_activity.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_search_activity.setRefreshing(false);

            }
        });
    }
    public boolean checkSUBSCRIBED(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        if (!prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        return true;
    }

    public void showAdsBanner() {
        if (!checkSUBSCRIBED()) {
            PrefManager prefManager= new PrefManager(getApplicationContext());
            if (prefManager.getString("ADMIN_BANNER_TYPE").equals("FACEBOOK")){
                showFbBanner();
            }
            if (prefManager.getString("ADMIN_BANNER_TYPE").equals("ADMOB")){
                showAdmobBanner();
            }
            if (prefManager.getString("ADMIN_BANNER_TYPE").equals("BOTH")) {
                if (prefManager.getString("Banner_Ads_display").equals("FACEBOOK")) {
                    prefManager.setString("Banner_Ads_display", "ADMOB");
                    showAdmobBanner();
                } else {
                    prefManager.setString("Banner_Ads_display", "FACEBOOK");
                    showFbBanner();
                }
            }
        }

    }
    public void showAdmobBanner(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        LinearLayout linear_layout_ads =  (LinearLayout) findViewById(R.id.linear_layout_ads);
        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(prefManager.getString("ADMIN_BANNER_ADMOB_ID"));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        linear_layout_ads.addView(mAdView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }
    public void showFbBanner(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        LinearLayout linear_layout_ads =  (LinearLayout) findViewById(R.id.linear_layout_ads);
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, prefManager.getString("ADMIN_BANNER_FACEBOOK_ID"), com.facebook.ads.AdSize.BANNER_HEIGHT_90);
        linear_layout_ads.addView(adView);
        adView.loadAd();

    }

    public void loadUsers(){
        tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (native_ads_enabled){
            if (tabletSize) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return ((position +1) % (lines_beetween_ads + 1 ) == 0) ? 4 : 1;
                    }
                });
            } else {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return ((position +1 ) % (lines_beetween_ads + 1 ) == 0) ? 2 : 1;
                    }
                });
            }
        }else{
            if (tabletSize) {
                this.gridLayoutManager=  new GridLayoutManager(getApplicationContext(),4,GridLayoutManager.VERTICAL,false);
            } else {
                this.gridLayoutManager=  new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
            }
        }
        swipe_refreshl_search_activity.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<User>> call = service.searchUsers(query);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                wallpaperList.clear();
                userList.clear();
                if (response.isSuccessful()){
                    if (response.body().size()>0){
                        if (native_ads_enabled){
                            if (tabletSize) {
                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        return ((position ) % (lines_beetween_ads + 1 ) == 0 || position == 0) ? 4 : 1;
                                    }
                                });
                            } else {
                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        return ((position ) % (lines_beetween_ads + 1 ) == 0 || position == 0) ? 2 : 1;
                                    }
                                });
                            }
                        }else {
                            if (tabletSize) {
                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        return ( position == 0) ? 4 : 1;
                                    }
                                });
                            } else {
                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        return ( position == 0) ? 2 : 1;
                                    }
                                });
                            }
                        }
                        wallpaperList.add(new Wallpaper().setViewType(7));
                        for (int i=0;i<response.body().size();i++){
                            userList.add(response.body().get(i));
                        }
                        wallpaperAdapter.notifyDataSetChanged();
                    }
                }
                loadWallpapers();
                swipe_refreshl_search_activity.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                loadWallpapers();
                swipe_refreshl_search_activity.setRefreshing(false);

            }
        });
    }
}
