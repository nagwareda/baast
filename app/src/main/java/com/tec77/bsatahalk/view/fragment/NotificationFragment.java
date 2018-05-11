package com.tec77.bsatahalk.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.NotificationAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.notification.PartModelNotification;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.ResponseNotification;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;


/**
 * display all notification and navigate to appropriate activity at item clicked
 */
public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ResponseNotification,
        NotificationAdapter.NotificationOnClickListener {

    private RecyclerView allNotificationAsView;
    private SwipeRefreshLayout refresh;
    private ArrayList<PartModelNotification> allNotificationList = new ArrayList<>();
    private NotificationAdapter adapterItemInNotification;
    private TextView numberOfNotification, noNotification;
    private RotateLoading loading;
    private int page = 1, maxPage = -1;
    private PartModelNotification mNotification;
    private TextView titlePage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initViews(view);
        if (isInternetAvailable())
            initData();
        else {
            loading.setVisibility(View.GONE);
            noNotification.setText(getString(R.string.no_internet));
            noNotification.setVisibility(View.VISIBLE);
        }
        actionViews();
        titlePage.setText(getString(R.string.notificationTitle));
        return view;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            page = 1;
            allNotificationList.clear();
            adapterItemInNotification.setCount(0);
            initData();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        mMessageReceiver.clearAbortBroadcast();
    }


    private void actionViews() {
        refresh.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapters();
    }

    private void setAdapters() {
        if (!numberOfNotification.getText().toString().isEmpty())
            adapterItemInNotification = new NotificationAdapter(allNotificationList, getActivity(), Integer.valueOf(numberOfNotification.getText().toString()), this);
        else {
            adapterItemInNotification = new NotificationAdapter(allNotificationList, getActivity(), 0, this);
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        allNotificationAsView.setLayoutManager(mLayoutManager);
        allNotificationAsView.setItemAnimator(new DefaultItemAnimator());
        allNotificationAsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    if (page <= maxPage && maxPage != -1) {
                        loading.setVisibility(View.VISIBLE);
                        initData();
                    }
                }
            }
        });
        allNotificationAsView.setAdapter(adapterItemInNotification);
    }

    public boolean isInternetAvailable() {
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            return true;
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initViews(View view) {
        allNotificationAsView = (RecyclerView) view.findViewById(R.id.NotificationFragment_RecyclerView_allNotification);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.NotificationFragment_SwipeRefreshLayout_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        //titlePage = (TextView) getActivity().findViewById(R.id.HomeActivity_TextView_titleActivity);
        loading = (RotateLoading) view.findViewById(R.id.NotificationFragment_RotateLoading_loading);
        numberOfNotification = (TextView) getActivity().findViewById(R.id.HomeActivity_TextView_numberOfNotification);
        noNotification = (TextView) view.findViewById(R.id.NotificationFragment_TextView_noNotification);
        titlePage = (TextView) getActivity().findViewById(R.id.HomeActivity_TextView_title);


    }

    @Override
    public void onRefresh() {
        page = 1;
        allNotificationList.clear();
        adapterItemInNotification.setCount(0);
        initData();
        refresh.setRefreshing(false);
    }

    private void initData() {
        FastNetworkManger manager = new FastNetworkManger(getActivity());
        String accessToken = new SharedPref(getActivity()).getString("token");
        int employeeId = new SharedPref(getActivity()).getInt("id");
        // manager.getAllNotification(accessToken, employeeId, page, 10, this, loading);
    }

    @Override
    public void allNotification(ArrayList<PartModelNotification> allNotification, int page) {

        maxPage = page;
        if (page == 1) {
            allNotificationList.clear();
        }
        allNotificationList.addAll(allNotification);
        adapterItemInNotification.notifyDataSetChanged();
        if (allNotification.isEmpty()) {
            noNotification.setText(getString(R.string.no_notification));
            noNotification.setVisibility(View.VISIBLE);
            allNotificationAsView.setVisibility(View.GONE);
        } else {
            noNotification.setVisibility(View.GONE);
            allNotificationAsView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * handel which activity or fragment will open, if notification related to provider balance the order will have id = 0 so after click must open balance fragment
     * otherwise will request order details
     *
     * @param notification
     */
    @Override
    public void onClick(PartModelNotification notification) {

        FastNetworkManger manager = new FastNetworkManger(getActivity());
        mNotification = notification;

    }


}
