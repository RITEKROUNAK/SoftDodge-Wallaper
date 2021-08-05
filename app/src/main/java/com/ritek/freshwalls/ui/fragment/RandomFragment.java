package com.ritek.freshwalls.ui.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ritek.freshwalls.R;
import com.ritek.freshwalls.adapter.WallpaperAdapter;
import com.ritek.freshwalls.api.apiClient;
import com.ritek.freshwalls.api.apiRest;
import com.ritek.freshwalls.entity.Tag;
import com.ritek.freshwalls.entity.Wallpaper;
import com.ritek.freshwalls.manager.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomFragment extends Fragment {

    private Integer page = 0;
    private Boolean loaded=false;
    private Integer type_ads = 0;

    private View view;
    private RelativeLayout relative_layout_random_fragment;
    private SwipeRefreshLayout swipe_refreshl_random_fragment;
    private ImageView image_view_empty;
    private RecyclerView recycle_view_random_fragment;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private GridLayoutManager gridLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> wallpaperList =new ArrayList<>();
    private List<Tag> tagList=new ArrayList<>();


    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private boolean tabletSize = false;
    private Boolean native_ads_enabled = false ;
    private PrefManager prefManager;

    public RandomFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


         prefManager= new PrefManager(getActivity().getApplicationContext());

        this.view =   inflater.inflate(R.layout.fragment_random, container, false);
        initView();
        initAction();

        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded)
                loadWallpapers();
        }
    }
    private void initView() {
        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());

        if (!prefManager.getString("ADMIN_NATIVE_TYPE").equals("FALSE")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(prefManager.getString("ADMIN_NATIVE_LINES"));
        }
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }

        this.relative_layout_random_fragment=(RelativeLayout) view.findViewById(R.id.relative_layout_random_fragment);
        this.swipe_refreshl_random_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_random_fragment);
        this.image_view_empty=(ImageView) view.findViewById(R.id.image_view_empty);
        this.recycle_view_random_fragment=(RecyclerView) view.findViewById(R.id.recycle_view_random_fragment);
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) view.findViewById(R.id.button_try_again);
        this.gridLayoutManager=  new GridLayoutManager(getActivity().getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return ((position + 1 ) % (lines_beetween_ads + 1 ) == 0 ) ? 4 : 1;
                }
            });
        } else {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return ((position + 1 ) % (lines_beetween_ads + 1) == 0) ? 2 : 1;
                }
            });
        }
        this.wallpaperAdapter =new WallpaperAdapter(wallpaperList,tagList,getActivity());
        recycle_view_random_fragment.setHasFixedSize(true);
        recycle_view_random_fragment.setAdapter(wallpaperAdapter);
        recycle_view_random_fragment.setLayoutManager(gridLayoutManager);
    }
    private void initAction() {
        recycle_view_random_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                }
            }
        });
        this.swipe_refreshl_random_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                page = 0;
                loading = true;
                tagList.clear();
                wallpaperList.clear();
                loadWallpapers();
                wallpaperAdapter.notifyDataSetChanged();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = 0;
                page = 0;
                loading = true;
                tagList.clear();
                wallpaperList.clear();
                loadWallpapers();
                wallpaperAdapter.notifyDataSetChanged();
            }
        });

    }
    private void loadNextWallpapers() {
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpapersRandom(page);
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        apiClient.FormatData(getActivity(),response);

                        for (int i=0;i<response.body().size();i++){
                            if (!exist(response.body().get(i))){
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
        recycle_view_random_fragment.setVisibility(View.GONE);
        image_view_empty.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_random_fragment.setRefreshing(true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpapersRandom(page);
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                apiClient.FormatData(getActivity(),response);
                if(response.isSuccessful()){
                    if (response.body().size()!=0){

                        for (int i=0;i<response.body().size();i++){
                            if (!exist(response.body().get(i))){
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
                        }
                        wallpaperAdapter.notifyDataSetChanged();
                        page++;
                        loaded=true;

                        recycle_view_random_fragment.setVisibility(View.VISIBLE);
                        image_view_empty.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        recycle_view_random_fragment.setVisibility(View.GONE);
                        image_view_empty.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }
                }else{
                    recycle_view_random_fragment.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_random_fragment.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Wallpaper>> call, Throwable t) {
                recycle_view_random_fragment.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_random_fragment.setRefreshing(false);
            }
        });
    }

    private boolean exist(Wallpaper wallpaper) {
        boolean exist = false;
        for (int i = 0; i < wallpaperList.size(); i++) {
            if (wallpaper.getId()==wallpaperList.get(i).getId()){
                exist=true;
            }
        }
        return exist;
    }

}
