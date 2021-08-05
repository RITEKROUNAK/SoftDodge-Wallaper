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
import com.ritek.freshwalls.entity.Wallpaper;
import com.ritek.freshwalls.entity.Slide;
import com.ritek.freshwalls.manager.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserFragment extends Fragment {
    private Button button_try_again;
    private LinearLayout linear_layout_page_error;
    private SwipeRefreshLayout swipe_refreshl_user_activity;
    private ImageView image_view_empty;
    private RelativeLayout relative_layout_load_more;
    private RecyclerView recycler_view_user_fragment;
    private GridLayoutManager gridLayoutManager;
    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> wallpaperList =new ArrayList<>();
    private List<Slide> slideList=new ArrayList<>();
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private Integer type_ads = 0;

    private View view;
    private Boolean loaded=false;

    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private Integer page = 0;
    private boolean loading = true;

    private int me = -1;
    private Integer user;
    private String type;
    private PrefManager prefManager;

    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        user = getArguments().getInt("user");
        type = getArguments().getString("type");
        PrefManager prf= new PrefManager(getActivity().getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            me = Integer.parseInt(prf.getString("ID_USER"));
        }
        prefManager= new PrefManager(getActivity().getApplicationContext());

        this.view =   inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);
        initAction();
        loadSlide();
        return view;
    }

    private void loadSlide() {
        if (user==me) {
            loadRingtoneByMe();
        }else {
            loadRingtones();
        }
    }



    private void initView(View view) {
        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());

        if (!prefManager.getString("ADMIN_NATIVE_TYPE").equals("FALSE")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(prefManager.getString("ADMIN_NATIVE_LINES"));
        }
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }

        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) view.findViewById(R.id.button_try_again);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.swipe_refreshl_user_activity=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_user_fragment);
        this.image_view_empty=(ImageView) view.findViewById(R.id.image_view_empty);
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.recycler_view_user_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_user_fragment);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (native_ads_enabled){
            if (tabletSize) {
                this.gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 4, GridLayoutManager.VERTICAL, false);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return ((position +1) % (lines_beetween_ads + 1 ) == 0) ? 4 : 1;
                    }
                });
            } else {
                this.gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return ((position +1 ) % (lines_beetween_ads + 1 ) == 0) ? 2 : 1;
                    }
                });
            }
        }else{
            if (tabletSize) {
                this.gridLayoutManager=  new GridLayoutManager(getActivity().getApplicationContext(),4,GridLayoutManager.VERTICAL,false);
            } else {
                this.gridLayoutManager=  new GridLayoutManager(getActivity().getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
            }
        }


        this.wallpaperAdapter =new WallpaperAdapter(wallpaperList,slideList,getActivity(),false);

        recycler_view_user_fragment.setHasFixedSize(true);
        recycler_view_user_fragment.setAdapter(wallpaperAdapter);
        recycler_view_user_fragment.setLayoutManager(gridLayoutManager);

    }



    private void loadNextRingtones() {
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call;
        if (user == me){
            call = service.wallpaperssByMe(page,user);
        }else{
            call= service.wallpapersByUser(user,page);
        }
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
    private void initAction() {
        this.swipe_refreshl_user_activity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                item = 0;
                page = 0;
                loading = true;
                slideList.clear();
                wallpaperList.clear();

                loadSlide();
            }
        });
        this.button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = 0;
                page = 0;
                loading = true;
                slideList.clear();
                wallpaperList.clear();
                loadSlide();
            }
        });

        recycler_view_user_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                            if (user==me) {
                                loadRingtoneByMeNext();
                            }else {
                                loadNextRingtones();
                            }
                        }
                    }
                }else{

                }
            }
        });
    }

    private void loadRingtoneByMe() {
        recycler_view_user_fragment.setVisibility(View.GONE);
        image_view_empty.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_user_activity.setRefreshing(true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call;
        if (user == me){
            call = service.wallpaperssByMe(page,user);
        }else{
            call= service.wallpapersByUser(user,page);
        }
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                apiClient.FormatData(getActivity(),response);

                if(response.isSuccessful()){

                    wallpaperList.clear();
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

                        recycler_view_user_fragment.setVisibility(View.VISIBLE);
                        image_view_empty.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        recycler_view_user_fragment.setVisibility(View.GONE);
                        image_view_empty.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }
                }else{
                    recycler_view_user_fragment.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_user_activity.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Wallpaper>> call, Throwable t) {

                recycler_view_user_fragment.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_user_activity.setRefreshing(false);

            }
        });
    }
    private void loadRingtoneByMeNext() {
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpaperssByMe(page,user);
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
    private void loadRingtones() {
        recycler_view_user_fragment.setVisibility(View.GONE);
        image_view_empty.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_user_activity.setRefreshing(true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Wallpaper>> call = service.wallpapersByUser(page,user);
        call.enqueue(new Callback<List<Wallpaper>>() {
            @Override
            public void onResponse(Call<List<Wallpaper>> call, Response<List<Wallpaper>> response) {
                apiClient.FormatData(getActivity(),response);

                if(response.isSuccessful()){

                    wallpaperList.clear();
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

                        recycler_view_user_fragment.setVisibility(View.VISIBLE);
                        image_view_empty.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        recycler_view_user_fragment.setVisibility(View.GONE);
                        image_view_empty.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }
                }else{
                    recycler_view_user_fragment.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_user_activity.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Wallpaper>> call, Throwable t) {

                recycler_view_user_fragment.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_user_activity.setRefreshing(false);

            }
        });
    }

}
