package com.tec77.bsatahalk.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.Quiz.ItemLessonQuizRecyclerAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.Quiz.QuizModel;
import com.tec77.bsatahalk.listener.LessonQuizResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;

import java.util.ArrayList;

public class QuizActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
        , LessonQuizResponseListener {

    private RecyclerView quizRecycler;
    private Toolbar toolbar;
    private ArrayList<QuizModel> quizList = new ArrayList<>();
    private ItemLessonQuizRecyclerAdapter quizRecyclerAdapter; // primary recycler
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnectionBtn;
    private int page = 1, maxPage = -1, limit = 10;
    private SwipeRefreshLayout refresh;
    private int lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiez);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAdapter();
        callQuizApi();
    }

    private void initViews() {
        //  lessonId = getIntent().getIntExtra("lessonId",-1);
        quizRecycler = findViewById(R.id.QuiezActivity_RecyclerView_quezzRecycler);
        toolbar = findViewById(R.id.QuiezActivity_Toolbar_toolbar);
        networkFailedLinearLayout = findViewById(R.id.QuiezActivity_LinearLayout_NetworkFailed);
        refreshConnectionBtn = findViewById(R.id.QuiezActivity_btn_refreshConnection);
        refresh = findViewById(R.id.QuiezActivity_SwipeRefreshLayout_refresh);
        quizRecyclerAdapter = new ItemLessonQuizRecyclerAdapter(this, quizList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(QuizActivity.this, LinearLayoutManager.VERTICAL, false);
        quizRecycler.setLayoutManager(linearLayoutManager);
        quizRecycler.setItemAnimator(new DefaultItemAnimator());
//        quizRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (!recyclerView.canScrollVertically(1)) {
//                    page++;
//                    if (page <= maxPage && maxPage != -1) {
//                        loading.setVisibility(View.VISIBLE);
//                        callQuizApi();
//                    }
//                }
//            }
//        });
        quizRecycler.setAdapter(quizRecyclerAdapter);
    }

    private void callQuizApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
           // new FastNetworkManger(this).getLessonQuiz(this, lessonId);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {
        quizList.clear();
        callQuizApi();
    }

    @Override
    public void lessonQuizList(ArrayList<QuizModel> lessonQuizList) {
        quizList.clear();
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
        quizList.addAll(lessonQuizList);
        quizRecyclerAdapter.notifyDataSetChanged();
    }
}
