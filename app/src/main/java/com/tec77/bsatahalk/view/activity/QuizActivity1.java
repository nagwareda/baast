package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.Quiz.QuestionsModel;
import com.tec77.bsatahalk.api.response.Quiz.QuizModel;
import com.tec77.bsatahalk.listener.LessonQuizResponseListener;
import com.tec77.bsatahalk.utils.CheckConnection;

import java.util.ArrayList;

public class QuizActivity1 extends BaseActivity implements LessonQuizResponseListener {

    private Toolbar toolbar;
    private ArrayList<QuizModel> quizList = new ArrayList<>();
    private LinearLayout networkFailedLinearLayout;
    private Button refreshConnectionBtn;
    private int lessonId;
    private LinearLayout linearLayout;
    private Button newBtn;
    private ArrayList<View.OnClickListener> btnListenerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz1);
        initViews();
        callQuizApi();
    }

    private void initViews() {
        lessonId = getIntent().getIntExtra("lessonId", -1);
        toolbar = findViewById(R.id.QuizActivity1_Toolbar_toolbar);
        linearLayout = findViewById(R.id.QuizActivity1_linearLayout_btnLinear);
        networkFailedLinearLayout = findViewById(R.id.QuizActivity1_LinearLayout_NetworkFailed);
        refreshConnectionBtn = findViewById(R.id.QuizActivity1_btn_refreshConnection);
        refreshConnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callQuizApi();
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void callQuizApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            networkFailedLinearLayout.setVisibility(View.GONE);
            //new FastNetworkManger(this).getLessonQuiz(this, lessonId);
        } else {
            networkFailedLinearLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void lessonQuizList(ArrayList<QuizModel> lessonQuizList) {
        quizList.clear();
        quizList.addAll(lessonQuizList);
        if (!quizList.isEmpty()) {
            handelBtnOnClickList();
            addBtn();
        }
    }

    private void addBtn() {
        int marginLeftRight = (int) getResources().getDimension(R.dimen.xx_large_margin);
        int marginTopBtm = (int) getResources().getDimension(R.dimen.small_margin);
        for (int i = 0; i < quizList.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(marginLeftRight, marginTopBtm, marginLeftRight, marginTopBtm);
            params.gravity = Gravity.CENTER;
            Button btn = new Button(this);
            btn.setId(i);
            final int id_ = btn.getId();
            btn.setText(getString(R.string.question) + " " + (i + 1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn.setBackground(getResources().getDrawable(R.drawable.btn_login_shape));
            } else {
                btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            btn.setPadding(0, (int) getResources().getDimension(R.dimen.tiny_padding),
                    (int) getResources().getDimension(R.dimen.tiny_padding), 0);
            btn.setTextColor(getResources().getColor(R.color.whiteColor));
            btn.setTextSize(getResources().getDimension(R.dimen.medium_text_size));
            //btn.setGravity(View.TEXT_ALIGNMENT_CENTER);
            linearLayout.addView(btn, params);
            newBtn = ((Button) findViewById(id_));
            newBtn.setOnClickListener(btnListenerList.get(newBtn.getId()));
        }
    }

    private void handelBtnOnClickList() {
        final Intent intent = new Intent(QuizActivity1.this, OneQuestionActivity.class);
        ArrayList<QuestionsModel> questionsModelList = new ArrayList<>();
        for (int i = 0; i < quizList.size(); i++) {
            final int finalI = i;
            questionsModelList = quizList.get(finalI).getQuizQuestions();
            final ArrayList<QuestionsModel> finalQuestionsModelList = questionsModelList;

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!finalQuestionsModelList.isEmpty()) {
                        intent.putExtra("e3rabList", quizList.get(finalI).getQuizQuestions()
                                .get(0).getQuestionName());
                        if (finalQuestionsModelList.size() > 1) {
                            intent.putExtra("est5ragList", quizList.get(finalI).getQuizQuestions()
                                    .get(1).getQuestionName());
                        }
                        intent.putExtra("imgUrl", quizList.get(finalI).getQuizImages());
                        startActivity(intent);
                    }
                }
            };
            btnListenerList.add(listener);
        }
    }

}
