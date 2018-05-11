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

public class StudentListActivity extends BaseActivity implements SerialListResponseListener, View.OnClickListener,
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
    private int category,year,semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        intiViews();
    }

    private void intiViews() {
        category = getIntent().getIntExtra("category",-1);
        year = getIntent().getIntExtra("year",-1);
        semester = getIntent().getIntExtra("semester",-1);
        serialListRecycler = findViewById(R.id.StudentListActivity_RecyclerView_lesseonRecycler);
        toolbar = findViewById(R.id.StudentListActivity_Toolbar_toolbar);
        title = findViewById(R.id.StudentListActivity_TextView_title);
        title.setText(getString(R.string.calss_serial));
        refreshBtn = findViewById(R.id.StudentListActivity_btn_refreshConnection);
        refreshBtn.setOnClickListener(this);
        networkFailedLinearLayout = findViewById(R.id.StudentListActivity_LinearLayout_NetworkFailed);
        noLesson = findViewById(R.id.StudentListActivity_TextView_noLessonAvailable);
        refresh = findViewById(R.id.StudentListActivity_swipeRefreshLayout_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        loading = findViewById(R.id.StudentListActivity_RotateLoading_loading);
        mRecyclerAdapter = new LessonsRecyclerAdapter(this, mSerialList,this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        setAdapters();
        initDataList();
    }

    private void initDataList() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(this).getYearList(page, limit, this,loading,category,year,semester);
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
                = new LinearLayoutManager(StudentListActivity.this, LinearLayoutManager.VERTICAL, false);
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
            intent = new Intent(StudentListActivity.this, LessonConclusionActivity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);

        } else if (type.contains("description")) {
            intent = new Intent(StudentListActivity.this, Lesson_Activity.class);
            intent.putExtra("lesson", lesson);
            startActivity(intent);

        }


    }
}
