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
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.LessonsRecyclerAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.SerialListResponse;
import com.tec77.bsatahalk.listener.SerialListResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.view.activity.LessonConclusionActivity;
import com.tec77.bsatahalk.view.activity.Lesson_Activity;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class Emla2SerialFragment extends BaseFragment implements SerialListResponseListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,LessonsRecyclerAdapter.LessonOnClickListener {
    private RecyclerView serialListRecycler;
    private Toolbar toolbar;
    private ArrayList<SerialListResponse.LessonPartModel> mSerialList = new ArrayList<>();
    private LessonsRecyclerAdapter mRecyclerAdapter;
    private Button refreshBtn;
    private TextView noLesson,title;
    private SwipeRefreshLayout refresh;
    private LinearLayout networkFailedLinearLayout;
    private RotateLoading loading;
    private int page = 1, limit = 10, mMaxPage = -1;
    private String listType;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_serial_list, container, false);
        intiViews();
        return view;
    }

    private void intiViews() {
        listType = "spellingList";
        serialListRecycler = view.findViewById(R.id.SerialListActivity_RecyclerView_lesseonRecycler);
        //toolbar = view.findViewById(R.id.SerialListActivity_Toolbar_toolbar);
        refreshBtn = view.findViewById(R.id.SerialListActivity_btn_refreshConnection);
        refreshBtn.setOnClickListener(this);
        networkFailedLinearLayout = view.findViewById(R.id.SerialListActivity_LinearLayout_NetworkFailed);
        noLesson = view.findViewById(R.id.SerialListActivity_TextView_noLessonAvailable);
        refresh = view.findViewById(R.id.SerialListActivity_swipeRefreshLayout_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        loading = view.findViewById(R.id.SerialListActivity_RotateLoading_loading);
        mRecyclerAdapter = new LessonsRecyclerAdapter(getActivity(), mSerialList,this);
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getActivity().getString(R.string.emlaa_grammer));
        setAdapters();
        initDataList();
    }

    private void initDataList() {
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(getActivity()).getSerialList(page, limit, this,loading,listType);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    private void setAdapters() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        serialListRecycler.setLayoutManager(linearLayoutManager);
        serialListRecycler.setItemAnimator(new DefaultItemAnimator());
        serialListRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    if (page <= mMaxPage && mMaxPage != -1) {
                        loading.setVisibility(View.VISIBLE);
                        initDataList();
                    }
                }
            }
        });
        serialListRecycler.setAdapter(mRecyclerAdapter);
        // highLevelRecycler.setAdapter(recyclerAdapter);
    }

    @Override
    public void serialList(ArrayList<SerialListResponse.LessonPartModel> serialList, int maxPage) {
        mMaxPage = maxPage;
        if (page == 1)
            mSerialList.clear();
        mSerialList.addAll(serialList);
        mRecyclerAdapter.notifyDataSetChanged();
        if (mSerialList.isEmpty()) {
            serialListRecycler.setVisibility(View.GONE);
            noLesson.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == refreshBtn.getId()) {
            initDataList();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mMaxPage = -1;
        initDataList();
        refresh.setRefreshing(false);
    }

    @Override
    public void onLessonClick(SerialListResponse.LessonPartModel lesson, String type) {
        Intent intent;
        if (type.contains("conclusion")) {
            intent = new Intent(getActivity(), LessonConclusionActivity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);

        } else if (type.contains("description")) {
            intent = new Intent(getActivity(), Lesson_Activity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);

        }


    }
}
