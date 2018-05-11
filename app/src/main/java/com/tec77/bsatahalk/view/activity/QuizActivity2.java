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
import com.tec77.bsatahalk.adapter.Quiz.ItemOneQuestionBtnAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.Quiz.QuestionsModel;
import com.tec77.bsatahalk.api.response.Quiz.QuizModel;
import com.tec77.bsatahalk.listener.LessonQuizResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.Const;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import static com.tec77.bsatahalk.utils.Const.staticQuestionMarkTxtList;

public class QuizActivity2 extends BaseActivity implements LessonQuizResponseListener,
        ItemOneQuestionBtnAdapter.SelectedQuestionList,View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private ArrayList<QuizModel> quizList = new ArrayList<>();
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnectionBtn;
    private TextView noQuizTxt;
    private int lessonId;
    private RecyclerView questionsBtnRecycler;
    private ItemOneQuestionBtnAdapter adapter;
    private RotateLoading loading;
    private SwipeRefreshLayout refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz2);
        Const.staticE3rabList.clear();
        Const.staticEst5ragList.clear();
        Const.staticStudentAns.clear();
        initViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        callQuizApi();
        setAdapters();
    }

    private void initViews() {
        //questionMarkTxt.addAll(staticQuestionMarkTxtList);
        lessonId = getIntent().getIntExtra("lessonId", -1);
        toolbar = findViewById(R.id.QuizActivity2_Toolbar_toolbar);
        loading = findViewById(R.id.QuizActivity2_RotateLoading_loading);
        networkFailedLinearLayout = findViewById(R.id.QuizActivity2_LinearLayout_NetworkFailed);
        refreshConnectionBtn = findViewById(R.id.QuizActivity2_btn_refreshConnection);
        refreshConnectionBtn.setOnClickListener(this);
        refresh = findViewById(R.id.QuizActivity2_swipeRefreshLayout_refresh);
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(this);
        questionsBtnRecycler = findViewById(R.id.QuizActivity2_RecyclerView_questionRecycler);
        noQuizTxt = findViewById(R.id.QuizActivity2_TextView_noLessonAvailable);
        adapter = new ItemOneQuestionBtnAdapter(this, quizList, staticQuestionMarkTxtList, this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAdapters() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(QuizActivity2.this, LinearLayoutManager.VERTICAL,
                false);
        questionsBtnRecycler.setLayoutManager(linearLayoutManager);
        questionsBtnRecycler.setItemAnimator(new DefaultItemAnimator());
        questionsBtnRecycler.setAdapter(adapter);
    }

    private void callQuizApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            new FastNetworkManger(this).getLessonQuiz(this, lessonId,loading);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void lessonQuizList(ArrayList<QuizModel> lessonQuizList) {
        quizList.clear();
        quizList.addAll(lessonQuizList);
        if (!quizList.isEmpty()) {
            adapter.notifyDataSetChanged();
        }else{
            noQuizTxt.setVisibility(View.VISIBLE);
            questionsBtnRecycler.setVisibility(View.GONE);
        }
    }

    @Override
    public void questionList(ArrayList<QuestionsModel> questionsModelList, int selectedPosition) {
        Intent intent = new Intent(QuizActivity2.this, OneQuestionActivity.class);
        intent.putExtra("quizNo",getString(R.string.quize_title) + " " + (selectedPosition+ 1));

        if (!questionsModelList.isEmpty()) {
            intent.putExtra("e3rabList", questionsModelList.get(0).getQuestionName());
            if (questionsModelList.size() > 1) {
                intent.putExtra("est5ragList", questionsModelList.get(1).getQuestionName());
            }
            intent.putExtra("imgUrl", quizList.get(selectedPosition).getQuizImages());
            intent.putExtra("qId",quizList.get(selectedPosition).getId());
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
      if(view.getId() == refreshConnectionBtn.getId()){
            callQuizApi();
        }
    }

    @Override
    public void onRefresh() {
        callQuizApi();
        refresh.setRefreshing(false);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RETURN_MARK_REQUEST_CODE && requestCode == Activity.RESULT_OK) {
//            if (data != null)
//                staticQuestionMarkTxtList.add(data.getStringExtra("questionResult"));
//            questionMarkTxt.clear();
//            questionMarkTxt.addAll(staticQuestionMarkTxtList);
//            adapter.notifyDataSetChanged();
//        }
//    }
}
