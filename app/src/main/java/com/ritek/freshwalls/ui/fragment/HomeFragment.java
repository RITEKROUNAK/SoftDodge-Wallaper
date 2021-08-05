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
import com.ritek.freshwalls.entity.Box;
import com.ritek.freshwalls.entity.Category;
import com.ritek.freshwalls.entity.Pack;
import com.ritek.freshwalls.entity.Wallpaper;
import com.ritek.freshwalls.entity.Slide;
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
public class HomeFragment extends Fragment {
    private Integer page = 0;
    private Integer type_ads = 0;

    private Boolean loaded=false;

    private View view;
    private RelativeLayout relative_layout_home_fragment;
    private SwipeRefreshLayout swipe_refreshl_home_fragment;
    private ImageView image_view_empty;
    private RecyclerView recycle_view_home_fragment;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private GridLayoutManager gridLayoutManager;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> wallpaperList =new ArrayList<>();
    private List<Slide> slideList=new ArrayList<>();
    private List<Pack> packList=new ArrayList<>();
    private List<Category> categoryList =new ArrayList<>();



    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private boolean tabletSize =  false;

    public HomeFragment() {





    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        this.view =   inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initAction();
        if (!loaded)
            loadData();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded){
                //loadData();
            }
        }
        else{

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

        this.relative_layout_home_fragment=(RelativeLayout) view.findViewById(R.id.relative_layout_home_fragment);
        this.swipe_refreshl_home_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_home_fragment);
        this.image_view_empty=(ImageView) view.findViewById(R.id.image_view_empty);
        this.recycle_view_home_fragment=(RecyclerView) view.findViewById(R.id.recycle_view_home_fragment);
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) view.findViewById(R.id.button_try_again);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        this.gridLayoutManager=  new GridLayoutManager(getActivity().getApplicationContext(),2,GridLayoutManager.VERTICAL,false);



        this.wallpaperAdapter =new WallpaperAdapter(wallpaperList,slideList,categoryList,packList,getActivity());
        recycle_view_home_fragment.setHasFixedSize(true);
        recycle_view_home_fragment.setAdapter(wallpaperAdapter);
        recycle_view_home_fragment.setLayoutManager(gridLayoutManager);

    }

    private void initAction() {

        recycle_view_home_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
        this.swipe_refreshl_home_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                page = 0;
                loading = true;
                slideList.clear();
                wallpaperList.clear();
                categoryList.clear();
                packList.clear();
                wallpaperAdapter.notifyDataSetChanged();
                loadData();
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
                categoryList.clear();
                packList.clear();
                wallpaperAdapter.notifyDataSetChanged();
                loadData();
            }
        });

    }


    private void loadNextWallpapers() {
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpapersAll("created",page);
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                if(response.isSuccessful()){
                    apiClient.FormatData(getActivity(),response);
                    if (response.body().size()!=0){
                        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());

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


    private void loadData() {
        recycle_view_home_fragment.setVisibility(View.VISIBLE);
        image_view_empty.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_home_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Box> call = service.loadData();
        call.enqueue(new Callback<Box>() {
            @Override
            public void onResponse(Call<Box> call, Response<Box> response) {
                if (response.isSuccessful()){
                    if (response.body().getCategories().size()==0 && response.body().getSlides().size()==0 && response.body().getWallpapers().size()==0 && response.body().getPacks().size()==0 ){
                        recycle_view_home_fragment.setVisibility(View.GONE);
                        image_view_empty.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                        swipe_refreshl_home_fragment.setRefreshing(false);
                    }else{
                        if (response.body().getCategories().size()!=0) {
                            for (int i = 0; i < response.body().getCategories().size(); i++) {
                                categoryList.add(response.body().getCategories().get(i));
                            }
                            wallpaperList.add(new Wallpaper().setViewType(5));
                        }
                        if (response.body().getSlides().size()!=0) {
                            for (int i = 0; i < response.body().getSlides().size(); i++) {
                                if (i<15)
                                    slideList.add(response.body().getSlides().get(i));

                            }
                            wallpaperList.add(new Wallpaper().setViewType(2));
                        }
                        if (response.body().getPacks().size()!=0) {
                            packList.add(new Pack().setViewType(2));
                            for (int i = 0; i < response.body().getPacks().size(); i++) {
                                packList.add(response.body().getPacks().get(i));
                            }
                            wallpaperList.add(new Wallpaper().setViewType(8));
                        }

                        if (categoryList.size()==0 && slideList.size()==0  && slideList.size()==0){
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
                        }
                        if (
                                (categoryList.size()==0 && slideList.size()==0 && packList.size() !=0) ||
                                        (categoryList.size()==0 && slideList.size()!=0 && packList.size() ==0) ||
                                        (categoryList.size()!=0 && slideList.size()==0 && packList.size() ==0)
                                ){
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
                        }
                        if(
                                (categoryList.size()==0 && slideList.size()!=0 && packList.size()!=0) ||
                                        (categoryList.size()!=0 && slideList.size()==0 && packList.size()!=0) ||
                                        (categoryList.size()!=0 && slideList.size()!=0 && packList.size()==0)
                                ){
                            if (native_ads_enabled) {
                                if (tabletSize) {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ((position - 1) % (lines_beetween_ads + 1) == 0 || position == 0 || position == 1) ? 4 : 1;
                                        }
                                    });
                                } else {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ((position - 1) % (lines_beetween_ads + 1) == 0 || position == 0 || position == 1) ? 2 : 1;
                                        }
                                    });
                                }
                            }else {
                                if (tabletSize) {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ( position == 0 || position == 1) ? 4 : 1;
                                        }
                                    });
                                } else {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ( position == 0 || position == 1) ? 2 : 1;
                                        }
                                    });
                                }
                            }
                        }
                        if(slideList.size()!=0 && categoryList.size()!=0 && packList.size()!=0 ){
                            if (native_ads_enabled) {
                                if (tabletSize) {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ((position - 2) % (lines_beetween_ads + 1) == 0 || position == 0 || position == 1 || position == 2) ? 4 : 1;
                                        }
                                    });
                                } else {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ((position - 2) % (lines_beetween_ads + 1) == 0 || position == 0 || position == 1 || position == 2) ? 2 : 1;
                                        }
                                    });
                                }
                            }else {
                                if (tabletSize) {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ( position == 0 || position == 1 || position == 2 ) ? 4 : 1;
                                        }
                                    });
                                } else {
                                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                        @Override
                                        public int getSpanSize(int position) {
                                            return ( position == 0 || position == 1 || position == 2) ? 2 : 1;
                                        }
                                    });
                                }
                            }
                        }
                        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());

                        if (response.body().getWallpapers().size()!=0) {
                            for (int i = 0; i < response.body().getWallpapers().size(); i++) {
                                wallpaperList.add(response.body().getWallpapers().get(i).setViewType(1));
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
                        page++;
                        loaded=true;
                        wallpaperAdapter.notifyDataSetChanged();
                        recycle_view_home_fragment.setVisibility(View.VISIBLE);
                        image_view_empty.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                        swipe_refreshl_home_fragment.setRefreshing(false);
                    }
                }else{
                    recycle_view_home_fragment.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    swipe_refreshl_home_fragment.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<Box> call, Throwable t) {
                recycle_view_home_fragment.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_home_fragment.setRefreshing(false);
            }
        });
    }
}
