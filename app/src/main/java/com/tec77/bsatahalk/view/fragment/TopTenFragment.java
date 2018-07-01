package com.tec77.bsatahalk.view.fragment;

import android.os.Bundle;
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
import com.tec77.bsatahalk.listener.TopTenListResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class TopTenFragment extends BaseFragment implements TopTenListResponseListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RecyclerView topTenRecycler;
    private TopTenListAdapter adapter;
    private ArrayList<TopTenResponse.TopTenModel> topTenStudentsList = new ArrayList<>();
    private View view;
    private TextView title, noStudents;
    private LinearLayout networkFailedLinearLayout;
    private RotateLoading loading;
    private SwipeRefreshLayout refreshStudentList;
    private Button refreshConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_top_ten, container, false);
        initView();
        return view;
    }

    private void initView() {
        topTenRecycler = view.findViewById(R.id.TopTenFragment_RecyclerView_topTenStudents);
        refreshStudentList = view.findViewById(R.id.TopTenFragment_SwipeRefreshLayout_refresh);
        refreshStudentList.setColorSchemeResources(R.color.colorPrimary);
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getString(R.string.nav_Home));
        networkFailedLinearLayout = view.findViewById(R.id.TopTenFragment_LinearLayout_NetworkFailed);
        refreshConnection = view.findViewById(R.id.TopTenFragment_btn_refreshConnection);
        refreshStudentList.setOnRefreshListener(this);
        noStudents = view.findViewById(R.id.TopTenFragment_TextView_noStudent);
        loading = view.findViewById(R.id.TopTenFragment_RotateLoading_loading);
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
            new FastNetworkManger(getActivity()).getTopTen(this, loading);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == refreshConnection.getId())
            callTopTenApi();
    }
}
