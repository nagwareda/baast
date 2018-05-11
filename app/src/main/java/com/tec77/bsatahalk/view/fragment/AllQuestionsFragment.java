package com.tec77.bsatahalk.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tec77.bsatahalk.adapter.recycler.AllMyQuestionAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.listener.AllQuestionsListner;
import com.tec77.bsatahalk.model.QuestionsModel;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

/**
 * Created by mac on 4/26/18.
 */

public class AllQuestionsFragment extends Fragment implements AllQuestionsListner, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private ArrayList<QuestionsModel> allQuestions = new ArrayList<>();
    private LinearLayout networkFailedLinearLayout, totalMarkLinear;
    private Button refreshConnectionBtn;
    private TextView noQestionsTxt;
    private RecyclerView questionsBtnRecycler;
    private AllMyQuestionAdapter adapter;
    private View view;
    private RotateLoading loading;
    private int mMaxPage = 0, page = 1;
    private SwipeRefreshLayout refresh;
    private TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_my_questions, container, false);
        initViews();
        setAdapters();
        callAllQuestionsApi();
        return view;
    }

    private void initViews() {
        title = getActivity().findViewById(R.id.HomeActivity_TextView_title);
        title.setText(getActivity().getString(R.string.nav_Quesiotn));
        networkFailedLinearLayout = view.findViewById(R.id.AllMyQuestions_LinearLayout_NetworkFailed);
        refreshConnectionBtn = view.findViewById(R.id.AllMyQuestions_btn_refreshConnection);
        refreshConnectionBtn.setOnClickListener(this);
        loading = view.findViewById(R.id.AllMyQuestions_RotateLoading_loading);
        questionsBtnRecycler = view.findViewById(R.id.AllMyQuestions_RecyclerView_questionRecycler);
        noQestionsTxt = view.findViewById(R.id.AllMyQuestions_TextView_noLessonAvailable);
        adapter = new AllMyQuestionAdapter(getActivity(), allQuestions);
        refresh = view.findViewById(R.id.AllMyQuestions_swipRefresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
    }


    private void setAdapters() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        questionsBtnRecycler.setLayoutManager(linearLayoutManager);
        questionsBtnRecycler.setItemAnimator(new DefaultItemAnimator());
        questionsBtnRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    if (page <= mMaxPage && mMaxPage != -1) {
                        loading.setVisibility(View.VISIBLE);
                        callAllQuestionsApi();
                    }
                }
            }
        });
        questionsBtnRecycler.setAdapter(adapter);
    }

    private void callAllQuestionsApi() {
        if (CheckConnection.getInstance().checkInternetConnection(getActivity())) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            noQestionsTxt.setVisibility(View.GONE);
            questionsBtnRecycler.setVisibility(View.VISIBLE);
           // loading.setVisibility(View.VISIBLE);
            new FastNetworkManger(getActivity()).getAllQuestions(loading, page, this);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void allQuestions(ArrayList<QuestionsModel> allQuestions1, int totalPage) {
        if (page == 1)
            allQuestions.clear();
        mMaxPage=totalPage;
        allQuestions.addAll(allQuestions1);
        if(allQuestions.isEmpty()){
            noQestionsTxt.setVisibility(View.VISIBLE);
            questionsBtnRecycler.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        mMaxPage = -1;
       callAllQuestionsApi();
        refresh.setRefreshing(false);
    }
}
