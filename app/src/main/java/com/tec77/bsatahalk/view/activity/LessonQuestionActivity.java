package com.tec77.bsatahalk.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.ContactUsRequest;
import com.tec77.bsatahalk.utils.CheckConnection;

public class LessonQuestionActivity extends AppCompatActivity {


    private EditText messageTxt;
    private Button sendBtn;
    private String lessonName = "";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_question);
        initViews();
    }


    private void initViews() {
        lessonName = getIntent().getStringExtra("lessonName");
        toolbar = findViewById(R.id.LessonQuestionActivity_Toolbar_toolbar);
        messageTxt = findViewById(R.id.LessonQuestionActivity_TextView_message);
        sendBtn = findViewById(R.id.LessonQuestionActivity_btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckConnection.getInstance().checkInternetConnection(LessonQuestionActivity.this)) {
                    if (messageTxt.getText().toString().isEmpty())
                        Toast.makeText(LessonQuestionActivity.this,getString(R.string.enter_your_message), Toast.LENGTH_SHORT).show();
                    else {
                        ContactUsRequest request = new ContactUsRequest();
                        request.setQuestion(lessonName + messageTxt.getText().toString());
                        new FastNetworkManger(LessonQuestionActivity.this).sendMessage(request, messageTxt);
                    }
                } else {
                    Toast.makeText(LessonQuestionActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }

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

}