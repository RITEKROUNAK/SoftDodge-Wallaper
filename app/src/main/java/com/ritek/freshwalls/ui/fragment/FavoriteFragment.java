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

import com.orhanobut.hawk.Hawk;
import com.ritek.freshwalls.R;
import com.ritek.freshwalls.adapter.WallpaperAdapter;
import com.ritek.freshwalls.entity.Wallpaper;
import com.ritek.freshwalls.entity.Slide;
import com.ritek.freshwalls.manager.PrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private Integer page = 0;
    private Boolean loaded=false;

    private View view;
    private RelativeLayout relative_layout_favorites_fragment;
    private SwipeRefreshLayout swipe_refreshl_favorites_fragment;
    private ImageView image_view_empty;
    private RecyclerView recycle_view_favorites_fragment;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private GridLayoutManager gridLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> wallpaperList =new ArrayList<>();
    private List<Slide> slideList=new ArrayList<>();



    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private boolean tabletSize;
    private Boolean native_ads_enabled = false ;

    public FavoriteFragment() {



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        view= inflater.inflate(R.layout.fragment_favorite, container, false);
        initView();
        initAction();

        return view;
    }

    private void initAction() {
        swipe_refreshl_favorites_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFavorites();
            }
        });
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

        this.relative_layout_favorites_fragment=(RelativeLayout) view.findViewById(R.id.relative_layout_favorites_fragment);
        this.swipe_refreshl_favorites_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_favorites_fragment);
        this.image_view_empty=(ImageView) view.findViewById(R.id.image_view_empty);
        this.recycle_view_favorites_fragment=(RecyclerView) view.findViewById(R.id.recycle_view_favorites_fragment);
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) view.findViewById(R.id.button_try_again);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            this.gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4, GridLayoutManager.VERTICAL, false);
        } else {
            this.gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        }
        this.wallpaperAdapter =new WallpaperAdapter(wallpaperList,slideList,getActivity(),true);
        recycle_view_favorites_fragment.setHasFixedSize(true);
        recycle_view_favorites_fragment.setAdapter(wallpaperAdapter);
        recycle_view_favorites_fragment.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded)
                loadFavorites();
        }
        else{

        }
    }

    private void loadFavorites() {
        swipe_refreshl_favorites_fragment.setRefreshing(true);
        List<Wallpaper> favorites_list =Hawk.get("favorite");


        if (favorites_list ==null){
            favorites_list = new ArrayList<>();
        }
        if (favorites_list.size()!=0){
            wallpaperList.clear();
            for (int i = favorites_list.size() - 1 ; i>=0; i--){
                Wallpaper a= new Wallpaper();
                a = favorites_list.get(i) ;
                wallpaperList.add(a);
            }
            wallpaperAdapter.notifyDataSetChanged();
            image_view_empty.setVisibility(View.GONE);
            recycle_view_favorites_fragment.setVisibility(View.VISIBLE);
        }else{
            image_view_empty.setVisibility(View.VISIBLE);
            recycle_view_favorites_fragment.setVisibility(View.GONE);
        }
        swipe_refreshl_favorites_fragment.setRefreshing(false);

    }
}
