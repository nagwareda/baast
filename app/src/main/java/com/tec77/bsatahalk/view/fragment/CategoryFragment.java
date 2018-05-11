package com.tec77.bsatahalk.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.CategoryRecyclerAdapter;
import com.tec77.bsatahalk.adapter.ClassesRecyclerAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.PartModelYear;
import com.tec77.bsatahalk.api.response.SchoolClassesResponse;
import com.tec77.bsatahalk.listener.SchoolClassResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.view.activity.CategoryActivity;
import com.tec77.bsatahalk.view.activity.StudentListActivity;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;


public class CategoryFragment extends BaseFragment implements SchoolClassResponseListener,
        ClassesRecyclerAdapter.ClassOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView categoryRecycler;
    private ArrayList<SchoolClassesResponse.Category> mCategoryList = new ArrayList<>();
    private CategoryRecyclerAdapter categoryRecyclerAdapter; // primary recycler
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnectionBtn;
    private int page = 1, maxPage = -1, limit = 10;
    private RotateLoading loading;
    private SwipeRefreshLayout refresh;
    private View view;
    private TextView title;
    private RadioButton firstSemesterRB, secondSemesterRB;
    private RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_category, container, false);
        intiViews();
        return view;
    }

    private void intiViews() {
        categoryRecycler = view.findViewById(R.id.CategoryActivity_Recycler_category);
       // toolbar = view.findViewById(R.id.CategoryActivity_Toolbar_toolbar);
        categoryRecyclerAdapter = new CategoryRecyclerAdapter(getActivity(),  mCategoryList, this);
        networkFailedLinearLayout = view.findViewById(R.id.CategoryActivity_LinearLayout_NetworkFailed);
        loading = view.findViewById(R.id.CategoryActivity_RotateLoading_loading);
        firstSemesterRB = view.findViewById(R.id.CategoryActivity_radioBtn_first);
        secondSemesterRB = view.findViewById(R.id.CategoryActivity_radioBtn_second);
        radioGroup = view.findViewById(R.id.CategoryActivity_radioGroup);
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getActivity().getString(R.string.classes_title));
        refresh = view.findViewById(R.id.CategoryActivity_swipRefresh_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        refreshConnectionBtn = view.findViewById(R.id.CategoryActivity_btn_refreshConnection);
        refreshConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDataList();
            }
        });

        initDataList();
    }


    private void initDataList() {
        setAdapters();
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(getActivity()).getSchoolClass(this,loading);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapters() {

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
        Intent intent = new Intent(getActivity(), StudentListActivity.class);
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
