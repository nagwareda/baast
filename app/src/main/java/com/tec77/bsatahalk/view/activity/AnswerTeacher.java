package com.tec77.bsatahalk.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.response.ResponseGetAnswer;
import com.tec77.bsatahalk.listener.AnswerQuestionListner;

import java.util.ArrayList;

public class AnswerTeacher extends BaseActivity implements AnswerQuestionListner {

    int id;
    String questionStudent;
    private Toolbar toolbar;
    private TextView question;
    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_teacher);

        initViews();
        if(getIntent()!=null){
            id=getIntent().getIntExtra("id",-1) ;
            questionStudent=getIntent().getStringExtra("question") ;
        }
        question.setText(questionStudent);

        new FastNetworkManger(this).getAnswerQuestion(id,this);
    }

    private void initViews() {
        question=findViewById(R.id.answerTeacher_TextView_quest);
        answer=findViewById(R.id.answerTeacher_TextView_ans);
        toolbar = findViewById(R.id.AnswerTeacher_Toolbar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void sendQuestion(ArrayList<ResponseGetAnswer.Answer> answer) {
        String full ="";
       for (int i=0;i<answer.size();i++){
           full+=answer.get(i).getAnswer()+"\n";
       }
       if(full.isEmpty()){
           full="بانتظار رد الاستاذ...";
       }
        this.answer.setText(full);
    }


}
