package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.CategoryRecyclerAdapter;
import com.tec77.bsatahalk.adapter.ClassesRecyclerAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.PartModelYear;
import com.tec77.bsatahalk.api.response.SchoolClassesResponse;
import com.tec77.bsatahalk.listener.SchoolClassResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class CategoryActivity extends BaseActivity implements SchoolClassResponseListener,
        ClassesRecyclerAdapter.ClassOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView categoryRecycler;
    private Toolbar toolbar;
    private ArrayList<SchoolClassesResponse.Category> mCategoryList = new ArrayList<>();
    private CategoryRecyclerAdapter categoryRecyclerAdapter; // primary recycler
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnectionBtn;
    private int page = 1, maxPage = -1, limit = 10;
    private RotateLoading loading;
    private SwipeRefreshLayout refresh;
    private RadioButton firstSemesterRB, secondSemesterRB;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        intiViews();
    }

    private void intiViews() {
        categoryRecycler = findViewById(R.id.CategoryActivity_Recycler_category);
        // toolbar = findViewById(R.id.CategoryActivity_Toolbar_toolbar);
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(CategoryActivity.this, mCategoryList, this);
        networkFailedLinearLayout = findViewById(R.id.CategoryActivity_LinearLayout_NetworkFailed);
        firstSemesterRB = findViewById(R.id.CategoryActivity_radioBtn_first);
        secondSemesterRB = findViewById(R.id.CategoryActivity_radioBtn_second);
        radioGroup = findViewById(R.id.CategoryActivity_radioGroup);
        loading = findViewById(R.id.CategoryActivity_RotateLoading_loading);
        refresh = findViewById(R.id.CategoryActivity_swipRefresh_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        refreshConnectionBtn = findViewById(R.id.CategoryActivity_btn_refreshConnection);
        refreshConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDataList();
            }
        });
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(null);
        initDataList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initDataList() {
        setAdapters();
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(this).getSchoolClass(this, loading);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapters() {

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(CategoryActivity.this, LinearLayoutManager.VERTICAL, false);
        categoryRecycler.setLayoutManager(linearLayoutManager);
        categoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    if (page <= maxPage && maxPage != -1) {
                        loading.setVisibility(View.VISIBLE);
                        initDataList();
                    }
                }
            }
        });
        categoryRecycler.setAdapter(categoryRecyclerAdapter);
    }

    @Override
    public void categories(ArrayList<SchoolClassesResponse.Category> categoryList) {
        mCategoryList.clear();
        mCategoryList.addAll(categoryList);
        categoryRecyclerAdapter.notifyDataSetChanged();

    }

    private void checkEmptyList(ArrayList<PartModelYear> list) {
        PartModelYear noYear = new PartModelYear();
        noYear.setId(-1);
        noYear.setName(getString(R.string.no_year));
        if (list.isEmpty())
            list.add(noYear);
    }

    @Override
    public void onRefresh() {
        mCategoryList.clear();
        refresh.setRefreshing(false);
        initDataList();
    }

    @Override
    public void onClassClick(int category, int year) {
        Intent intent = new Intent(CategoryActivity.this, StudentListActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("year", year);
        int selectedSemester = radioGroup.getCheckedRadioButtonId();
        if (selectedSemester == firstSemesterRB.getId())
            intent.putExtra("semester", 1);
        else
            intent.putExtra("semester", 2);
        startActivity(intent);
    }
}