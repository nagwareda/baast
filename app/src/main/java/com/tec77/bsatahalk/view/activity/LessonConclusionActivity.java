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

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.LessonConclusionAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.ResponseLessonConclusion;
import com.tec77.bsatahalk.api.response.SerialListResponse;
import com.tec77.bsatahalk.listener.LessonConclusionResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class LessonConclusionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LessonConclusionResponseListener,LessonConclusionAdapter.OnImgClickListener {

    private RecyclerView conclusionImgRecycler;
    private Toolbar toolbar;
    private ArrayList<ResponseLessonConclusion.itemConclusion> conclusionImgList = new ArrayList<>();
    private LessonConclusionAdapter mRecyclerAdapter;
    private Button refreshBtn;
    private TextView noLesson;
    private SwipeRefreshLayout refresh;
    private LinearLayout networkFailedLinearLayout;
    private RotateLoading loading;
    private SerialListResponse.LessonPartModel lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_conclusion);
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
        intiViews();
    }

    private void intiViews() {
        lesson = (SerialListResponse.LessonPartModel) getIntent().getSerializableExtra("lesson");
        conclusionImgRecycler = findViewById(R.id.LessonConclusionActivity_RecyclerView_conclusionRecycler);
        toolbar = findViewById(R.id.LessonConclusionActivity_Toolbar_toolbar);
        refreshBtn = findViewById(R.id.LessonConclusionActivity_btn_refreshConnection);
        refreshBtn.setOnClickListener(this);
        networkFailedLinearLayout = findViewById(R.id.LessonConclusionActivity_LinearLayout_NetworkFailed);
        noLesson = findViewById(R.id.LessonConclusionActivity_TextView_noConclusionAvailable);
        refresh = findViewById(R.id.LessonConclusionActivity_swipeRefreshLayout_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        loading = findViewById(R.id.LessonConclusionActivity_RotateLoading_loading);
        mRecyclerAdapter = new LessonConclusionAdapter(this, conclusionImgList,this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        setAdapters();
        callConclusionApi();
    }

    private void callConclusionApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(this).getLessonConclusion(lesson.getId(), this, loading);
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
                = new LinearLayoutManager(LessonConclusionActivity.this, LinearLayoutManager.VERTICAL, false);
        conclusionImgRecycler.setLayoutManager(linearLayoutManager);
        conclusionImgRecycler.setItemAnimator(new DefaultItemAnimator());
        conclusionImgRecycler.setAdapter(mRecyclerAdapter);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == refreshBtn.getId()) {
            callConclusionApi();
        }
    }

    @Override
    public void onRefresh() {
        noLesson.setVisibility(View.GONE);
        conclusionImgRecycler.setVisibility(View.VISIBLE);
        callConclusionApi();
        refresh.setRefreshing(false);
    }


    @Override
    public void lessonConclusion(ArrayList<ResponseLessonConclusion.itemConclusion> mConclusionImgList) {
        conclusionImgList.clear();
        conclusionImgList.addAll(mConclusionImgList);
        mRecyclerAdapter.notifyDataSetChanged();
        if (conclusionImgList.isEmpty()) {
            noLesson.setVisibility(View.VISIBLE);
            conclusionImgRecycler.setVisibility(View.GONE);
        }

    }

    @Override
    public void onImgClick(String imgUrl) {
        Intent intent = new Intent(LessonConclusionActivity.this,ImageDisplayActivity.class);
        intent.putExtra("imgUrl",imgUrl);
        startActivity(intent);

    }
}
