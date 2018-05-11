package com.tec77.bsatahalk.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.TopTenListAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.TopTenResponse;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.TopTenListResponseListener;
import com.tec77.bsatahalk.model.MainCategoryRecyclerModel;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.view.activity.CategoryActivity;
import com.tec77.bsatahalk.view.activity.SerialListActivity;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements View.OnClickListener, TopTenListResponseListener,
        SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView topTenRecycler;
    private TopTenListAdapter adapter;
    private ArrayList<TopTenResponse.TopTenModel> topTenStudentsList = new ArrayList<>();
    private SwipeRefreshLayout refreshStudentList;
    private TextView noStudents, title;
    private LinearLayout networkFailedLinearLayout, btnLinear, txtLinear;
    private Button refreshConnection, serialBtn, schoolClassBtn, emlaaRuleBtn;
    private SharedPref sharedPref;
    private View view;
    private RecyclerView topicRecyclerView;
    private ArrayList<MainCategoryRecyclerModel> categoryList = new ArrayList<>();
    private LinearLayout e3rabLinear, emla2Linear, classLinear;
    private RotateLoading loading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        actionViews();
        return view;
    }

    private void initView() {
        topTenRecycler = view.findViewById(R.id.TopTenFragment_RecyclerView_topTenStudents);
        refreshStudentList = view.findViewById(R.id.TopTenFragment_SwipeRefreshLayout_refresh);
        refreshStudentList.setColorSchemeResources(R.color.colorPrimary);
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getString(R.string.nav_Home));
        btnLinear = view.findViewById(R.id.TopTenFragment_linear_btns);
        txtLinear = view.findViewById(R.id.TopTenFragment_linear_txts);
        networkFailedLinearLayout = view.findViewById(R.id.TopTenFragment_LinearLayout_NetworkFailed);
        refreshConnection = view.findViewById(R.id.TopTenFragment_btn_refreshConnection);
        schoolClassBtn = view.findViewById(R.id.TopTenFragment_Btn_classes);
        serialBtn = view.findViewById(R.id.TopTenFragment_Btn_serial);
        emlaaRuleBtn = view.findViewById(R.id.TopTenFragment_Btn_emlaa);
        noStudents = view.findViewById(R.id.TopTenFragment_TextView_noStudent);
        topicRecyclerView = view.findViewById(R.id.AboutUs_recycler_mainTopic);
        e3rabLinear = view.findViewById(R.id.HomeFragment_linear_e3raab);
        emla2Linear = view.findViewById(R.id.HomeFragment_linear_emlaa);
        classLinear = view.findViewById(R.id.HomeFragment_linear_class);
        loading = view.findViewById(R.id.TopTenFragment_RotateLoading_loading);

        initCategoryList();

    }

    private void initCategoryList() {
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.earaab, getString(R.string.serial)));
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.emlaa, getString(R.string.emlaa_grammer)));
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.classes, getString(R.string.classes_title)));
        categoryList.add(new MainCategoryRecyclerModel(R.drawable.top_ten, getString(R.string.topTen)));
        // initCategoryListAdapter();

    }

    private void initCategoryListAdapter() {
        // MainTopicHomeRecyclerAdapter adapter = new MainTopicHomeRecyclerAdapter(categoryList, getActivity(), this);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        topicRecyclerView.setLayoutManager(horizontalLayoutManager);
        // topicRecyclerView.setAdapter(adapter);
    }

    private void actionViews() {
        refreshConnection.setOnClickListener(this);
        serialBtn.setOnClickListener(this);
        schoolClassBtn.setOnClickListener(this);
        refreshStudentList.setOnRefreshListener(this);
        emlaaRuleBtn.setOnClickListener(this);
        emla2Linear.setOnClickListener(this);
        e3rabLinear.setOnClickListener(this);
        classLinear.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapters();
    }

    private void setAdapters() {
        adapter = new TopTenListAdapter(topTenStudentsList, getActivity());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        topTenRecycler.setLayoutManager(mLayoutManager);
        topTenRecycler.setItemAnimator(new DefaultItemAnimator());
        topTenRecycler.setAdapter(adapter);
        callTopTenApi();
    }

    private void callTopTenApi() {
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            btnLinear.setVisibility(View.VISIBLE);
            txtLinear.setVisibility(View.VISIBLE);
            new FastNetworkManger(getActivity()).getTopTen(this,loading);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
            btnLinear.setVisibility(View.GONE);
            txtLinear.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == refreshConnection.getId())
            callTopTenApi();
        else if (view.getId() == schoolClassBtn.getId()) {
            Intent intent = new Intent(getActivity(), CategoryActivity.class);
            getActivity().startActivity(intent);
        } else if (view.getId() == serialBtn.getId()) {
            Intent intent = new Intent(getActivity(), SerialListActivity.class);
            intent.putExtra("listType", "generalList");
            getActivity().startActivity(intent);
        } else if (view.getId() == emlaaRuleBtn.getId()) {
            Intent intent = new Intent(getActivity(), SerialListActivity.class);
            intent.putExtra("listType", "spellingList");
            getActivity().startActivity(intent);
        } else if (view.getId() == e3rabLinear.getId()) {
            replaceFragmentFromCategory(new SerialListFragment(), "SerialListFragment");

        } else if (view.getId() == emla2Linear.getId()) {
            replaceFragmentFromCategory(new Emla2SerialFragment(), "Emla2SerialFragment");

        } else if (view.getId() == classLinear.getId()) {
            replaceFragmentFromCategory(new CategoryFragment(), "Emla2SerialFragment");

        }

    }


    @Override
    public void topTenList(ArrayList<TopTenResponse.TopTenModel> topTenList) {
        topTenStudentsList.clear();
        topTenStudentsList.addAll(topTenList);
        adapter.notifyDataSetChanged();
        if (topTenStudentsList.isEmpty()) {
            topTenRecycler.setVisibility(View.GONE);
            noStudents.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        callTopTenApi();
        refreshStudentList.setRefreshing(false);

    }


    public void replaceFragmentFromCategory(Fragment fragment, String tag) {
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.HomeActivity_FrameLayout_container, fragment)
                .addToBackStack(tag)
                .commit();
    }
}