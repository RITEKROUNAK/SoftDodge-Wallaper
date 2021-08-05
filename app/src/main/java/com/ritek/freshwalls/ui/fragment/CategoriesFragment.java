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
import com.ritek.freshwalls.adapter.CategoryAdapter;
import com.ritek.freshwalls.api.apiClient;
import com.ritek.freshwalls.api.apiRest;
import com.ritek.freshwalls.entity.Category;
import com.ritek.freshwalls.entity.Color;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {


    private View view;
    private boolean loaded;
    private RelativeLayout relative_layout_categories_fragment;
    private SwipeRefreshLayout swipe_refreshl_categories_fragment;
    private ImageView image_view_empty;
    private RecyclerView recycle_view_categories_fragment;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_page_error;
    private Button button_try_again;
    private GridLayoutManager gridLayoutManager;
    private CategoryAdapter categoryAdapter;

    private List<Category> categories=new ArrayList<>();
    private List<Color> colorList=new ArrayList<>();

    public CategoriesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view =   inflater.inflate(R.layout.fragment_categories, container, false);
        initView();
        initAction();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded){
                loadData();
            }
        }
    }
    private void loadData() {

            categories.clear();

            recycle_view_categories_fragment.setVisibility(View.VISIBLE);
            image_view_empty.setVisibility(View.GONE);
            linear_layout_page_error.setVisibility(View.GONE);
            swipe_refreshl_categories_fragment.setRefreshing(true);

        loadColor();
    }


    private void initAction() {
        swipe_refreshl_categories_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        this.button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    private void initView() {
        this.relative_layout_categories_fragment=(RelativeLayout) view.findViewById(R.id.relative_layout_categories_fragment);
        this.swipe_refreshl_categories_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_categories_fragment);
        this.image_view_empty=(ImageView) view.findViewById(R.id.image_view_empty);
        this.recycle_view_categories_fragment=(RecyclerView) view.findViewById(R.id.recycle_view_categories_fragment);
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.button_try_again=(Button) view.findViewById(R.id.button_try_again);

        this.gridLayoutManager=  new GridLayoutManager(getActivity().getApplicationContext(),1,GridLayoutManager.VERTICAL,false);
        this.categoryAdapter =new CategoryAdapter(categories,colorList,getActivity());
        recycle_view_categories_fragment.setHasFixedSize(true);
        recycle_view_categories_fragment.setAdapter(categoryAdapter);
        recycle_view_categories_fragment.setLayoutManager(gridLayoutManager);
    }
    private void loadColor() {

        colorList.clear();
        categories.clear();
        categoryAdapter.notifyDataSetChanged();

        recycle_view_categories_fragment.setVisibility(View.VISIBLE);
        image_view_empty.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_categories_fragment.setRefreshing(true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Color>> call = service.ColorsList();
        call.enqueue(new Callback<List<Color>>() {
            @Override
            public void onResponse(Call<List<Color>> call, Response<List<Color>> response) {
                apiClient.FormatData(getActivity(),response);
                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        for (int i=0;i<response.body().size();i++){
                            if (response.body().get(i).getTitle().length()>1)
                                colorList.add(response.body().get(i).setViewType(1));
                        }
                        categories.add(new Category().setViewType(2));
                    }
                    categoryAdapter.notifyDataSetChanged();

                    LoadCategories();

                }else{
                    recycle_view_categories_fragment.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    swipe_refreshl_categories_fragment.setRefreshing(false);
                }

            }
            @Override
            public void onFailure(Call<List<Color>> call, Throwable t) {
                recycle_view_categories_fragment.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_categories_fragment.setRefreshing(false);
            }
        });

    }

    private void LoadCategories() {
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Category>> call = service.categoryAll();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()!=0){

                        for (int i=0;i<response.body().size();i++){
                            Category category= new Category();
                            category.setId(response.body().get(i).getId());
                            category.setImage(response.body().get(i).getImage());
                            category.setTitle(response.body().get(i).getTitle());
                            category.setViewType(1);
                            categories.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                        recycle_view_categories_fragment.setVisibility(View.VISIBLE);
                        image_view_empty.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        recycle_view_categories_fragment.setVisibility(View.GONE);
                        image_view_empty.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }
                    loaded= true;
                }else{
                    recycle_view_categories_fragment.setVisibility(View.GONE);
                    image_view_empty.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_categories_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                recycle_view_categories_fragment.setVisibility(View.GONE);
                image_view_empty.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_categories_fragment.setRefreshing(false);

            }
        });
    }
}
