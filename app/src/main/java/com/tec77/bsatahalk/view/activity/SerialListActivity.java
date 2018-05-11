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
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.LessonsRecyclerAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.SerialListResponse;
import com.tec77.bsatahalk.listener.SerialListResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class SerialListActivity extends BaseActivity implements SerialListResponseListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, LessonsRecyclerAdapter.LessonOnClickListener {
    private RecyclerView serialListRecycler;
    private Toolbar toolbar;
    private ArrayList<SerialListResponse.LessonPartModel> mSerialList = new ArrayList<>();
    private LessonsRecyclerAdapter mRecyclerAdapter;
    private Button refreshBtn;
    private TextView noLesson;
    private SwipeRefreshLayout refresh;
    private LinearLayout networkFailedLinearLayout;
    private RotateLoading loading;
    private int page = 1, limit = 10, mMaxPage = -1;
    private String listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_list);
        intiViews();
    }

    private void intiViews() {
        listType = getIntent().getStringExtra("listType");
        serialListRecycler = findViewById(R.id.SerialListActivity_RecyclerView_lesseonRecycler);
        //   toolbar = findViewById(R.id.SerialListActivity_Toolbar_toolbar);
        refreshBtn = findViewById(R.id.SerialListActivity_btn_refreshConnection);
        refreshBtn.setOnClickListener(this);
        networkFailedLinearLayout = findViewById(R.id.SerialListActivity_LinearLayout_NetworkFailed);
        noLesson = findViewById(R.id.SerialListActivity_TextView_noLessonAvailable);
        refresh = findViewById(R.id.SerialListActivity_swipeRefreshLayout_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        loading = findViewById(R.id.SerialListActivity_RotateLoading_loading);
        mRecyclerAdapter = new LessonsRecyclerAdapter(this, mSerialList, this);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(null);
        setAdapters();
        initDataList();
    }

    private void initDataList() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(this).getSerialList(page, limit, this, loading, listType);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAdapters() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(SerialListActivity.this, LinearLayoutManager.VERTICAL, false);
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
            intent = new Intent(SerialListActivity.this, LessonConclusionActivity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);

        } else if (type.contains("description")) {
            intent = new Intent(SerialListActivity.this, Lesson_Activity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);

        }


    }

}
