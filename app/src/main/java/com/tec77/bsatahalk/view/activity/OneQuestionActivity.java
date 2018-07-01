package com.tec77.bsatahalk.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.adapter.Quiz.ItemChooseRecyclerAdapter;
import com.tec77.bsatahalk.adapter.Quiz.ItemE3rabRecyclerAdapter;
import com.tec77.bsatahalk.adapter.Quiz.ItemEst5ragListAdapter;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.AddQuizDegreeRequest;
import com.tec77.bsatahalk.api.response.Quiz.OneQuestionModel;
import com.tec77.bsatahalk.api.response.ResponseChooseQuestion;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.listener.QuizDegreeResponseListener;
import com.tec77.bsatahalk.listener.ResponseChooseQuestionListener;
import com.tec77.bsatahalk.model.StudentAnswerItem;
import com.tec77.bsatahalk.utils.CheckConnection;
import com.tec77.bsatahalk.utils.Const;
import com.tec77.bsatahalk.view.dialog.QuestionImgDialog;
import com.tec77.bsatahalk.view.dialog.SolveE3rabQuestionDialog;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tec77.bsatahalk.utils.Const.staticE3rabList;
import static com.tec77.bsatahalk.utils.Const.staticEst5ragList;


public class OneQuestionActivity extends BaseActivity implements View.OnClickListener,
        ItemE3rabRecyclerAdapter.TotalQuestionMarkListener, QuizDegreeResponseListener,
        ResponseChooseQuestionListener {

    private RecyclerView e3rabRecycler, est5ragRecycler, chooseRecycler;
    private TextView totalMarkTxt, howSolveE3rabTxt, quizNoTxt;
    private ImageView questionImg;
    private FrameLayout est5ragFrame, e3rabFrame, chooseFrame;
    private Button checkBtn;
    private Toolbar toolbar;
    private ArrayList<OneQuestionModel> e3rabList = new ArrayList<>();
    private ArrayList<OneQuestionModel> est5ragList = new ArrayList<>();
    private ArrayList<ResponseChooseQuestion.ItemChooseQuestion> chooseList = new ArrayList<>();
    private ItemE3rabRecyclerAdapter e3rabAdapter;
    private ItemEst5ragListAdapter est5ragAdapter;
    private ItemChooseRecyclerAdapter chooseAdapter;
    private String imgUrl;
    private int questionId ,qTotalMark;
    private float finalResult;
    private HashMap<String, Float> questionsResultList = new HashMap<>();
    private LinearLayout questionTotalMarkLinear;
    private RotateLoading loading;
    private SwipeRefreshLayout refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_question);
        initView();
        actionView();
       // putDataInView();
    }

    private void initView() {
//        checkEditTxt = findViewById(R.id.OneQuestion_editText_check);
        e3rabList = (ArrayList<OneQuestionModel>) getIntent().getSerializableExtra("e3rabList");
        est5ragList = (ArrayList<OneQuestionModel>) getIntent().getSerializableExtra("est5ragList");
        questionId = getIntent().getIntExtra("qId", -1);
        imgUrl = getIntent().getStringExtra("imgUrl");
        loading = findViewById(R.id.OneQuestion_loading);
        quizNoTxt = findViewById(R.id.OneQuestion_TextView_questionNo);
        quizNoTxt.setText(getIntent().getStringExtra("quizNo"));
        questionImg = findViewById(R.id.OneQuestion_imageView_quizImage);
        e3rabFrame = findViewById(R.id.OneQuestion_frameLayout_e3raab);
        est5ragFrame = findViewById(R.id.OneQuestion_frameLayout_est5rag);
        e3rabRecycler = findViewById(R.id.OneQuestion_Recycler_e3raab);
        est5ragRecycler = findViewById(R.id.OneQuestion_Recycler_Est5rag);
        chooseRecycler = findViewById(R.id.OneQuestion_Recycler_choose);
        chooseFrame = findViewById(R.id.OneQuestion_frameLayout_choose);
        totalMarkTxt = findViewById(R.id.OneQuestion_txt_totalDegreeTxt);
        questionTotalMarkLinear = findViewById(R.id.OneQuestion_LinearLayout_totalQuestionMark);
        checkBtn = findViewById(R.id.OneQuestion_btn_check);
        e3rabAdapter = new ItemE3rabRecyclerAdapter(this, staticE3rabList, this);
        est5ragAdapter = new ItemEst5ragListAdapter(this, staticEst5ragList);
        chooseAdapter = new ItemChooseRecyclerAdapter(this, chooseList);
        toolbar = findViewById(R.id.OneQuestion_Toolbar_toolbar);
        //refresh = findViewById(R.id.OneQuestion_swipeRefreshLayout_refresh);
       // refresh.setColorSchemeResources(R.color.colorPrimary);
       // refresh.setOnRefreshListener(this);

        howSolveE3rabTxt = findViewById(R.id.OneQuestion_textView_howSolveQuiz);
        callChooseQuestionApi();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        setAdapters();
    }

    private void callChooseQuestionApi() {
        if (CheckConnection.getInstance().checkInternetConnection(this)) {
            new FastNetworkManger(this).getChooseQuestion(questionId, this, loading);
        } else {
            Toast.makeText(this, getString(R.string.no_year), Toast.LENGTH_SHORT).show();
        }
    }


    private void actionView() {
        checkBtn.setOnClickListener(this);
        questionImg.setOnClickListener(this);
        howSolveE3rabTxt.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAdapters() {
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(OneQuestionActivity.this, LinearLayoutManager.VERTICAL,
                false);
        e3rabRecycler.setLayoutManager(linearLayoutManager);
        e3rabRecycler.setItemAnimator(new DefaultItemAnimator());
        e3rabRecycler.setAdapter(e3rabAdapter);

        LinearLayoutManager linearLayoutManager1
                = new LinearLayoutManager(OneQuestionActivity.this, LinearLayoutManager.VERTICAL,
                false);
        est5ragRecycler.setLayoutManager(linearLayoutManager1);
        est5ragRecycler.setItemAnimator(new DefaultItemAnimator());
        est5ragRecycler.setAdapter(est5ragAdapter);
        // highLevelRecycler.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager2
                = new LinearLayoutManager(OneQuestionActivity.this, LinearLayoutManager.VERTICAL,
                false);
        chooseRecycler.setLayoutManager(linearLayoutManager2);
        chooseRecycler.setItemAnimator(new DefaultItemAnimator());
        chooseRecycler.setAdapter(chooseAdapter);
    }

    private void putDataInView() {
        if (!imgUrl.isEmpty())
            Picasso.with(this)
                    .load(imgUrl)
                    .placeholder(R.drawable.defult_img)
                    .error(R.color.blackColor)

                    .into(questionImg);
//            Glide.with(this)
//                    .load(imgUrl)
//                    .into(questionImg);

        if (!e3rabList.isEmpty()) {
            e3rabFrame.setVisibility(View.VISIBLE);
            handelRecyclerList(e3rabList, "e3rab");
        } else {
            e3rabFrame.setVisibility(View.GONE);
        }

        if (!est5ragList.isEmpty()) {
            est5ragFrame.setVisibility(View.VISIBLE);
            handelRecyclerList(est5ragList, "est5rag");
        } else {
            est5ragFrame.setVisibility(View.GONE);
        }
    }

    private void handelRecyclerList(ArrayList<OneQuestionModel> list, String type) {
        if (type.contains("e3rab")) {
            StudentAnswerItem item;
            staticE3rabList.clear();
            for (int i = 0; i < list.size(); i++) {
                item = new StudentAnswerItem();
                item.setAnswer(list.get(i).getAnswer());
                item.setAnswer_count(list.get(i).getAnswer_count());
                item.setId(list.get(i).getId());
                item.setQuestion_id(list.get(i).getQuestion_id());
                item.setQuestionValue(list.get(i).getQuestionValue());
                item.setMarkAnswer(list.get(i).getMarkAnswer());
                item.setStudentAns("");
                item.setItemMark(0);
                staticE3rabList.add(item);
            }
           e3rabAdapter.notifyDataSetChanged();
        } else {
            StudentAnswerItem item;
            staticEst5ragList.clear();
            for (int i = 0; i < list.size(); i++) {
                item = new StudentAnswerItem();
                item.setAnswer(list.get(i).getAnswer());
                item.setAnswer_count(list.get(i).getAnswer_count());
                item.setId(list.get(i).getId());
                item.setQuestion_id(list.get(i).getQuestion_id());
                item.setQuestionValue(list.get(i).getQuestionValue());
                item.setMarkAnswer(list.get(i).getMarkAnswer());
                item.setStudentAns("");
                item.setItemMark(0);
                staticEst5ragList.add(item);
            }
            est5ragAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == checkBtn.getId()) {
            displayAletDailog();
//            if (!est5ragList.isEmpty()) {
//                est5ragAdapter.changeFocuse();
//                est5ragAdapter.notifyDataSetChanged();
//                est5ragTxt.setVisibility(View.VISIBLE);
//            }
//            if (!e3rabList.isEmpty())
//                e3rabAdapter.notifyDataSetChanged();

        } else if (view.getId() == questionImg.getId()) {
            if (imgUrl != null) {

                Intent intent = new Intent(OneQuestionActivity.this,ImageActivity.class);
                intent.putExtra("imgUrl",imgUrl);
                startActivity(intent);

//                QuestionImgDialog dialog = new QuestionImgDialog(imgUrl);
//                //dialog.getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2; //style id
//                dialog.show(getSupportFragmentManager(), "questionImage");
            }
        } else if (view.getId() == howSolveE3rabTxt.getId()) {
            SolveE3rabQuestionDialog dialog = new SolveE3rabQuestionDialog();
            dialog.show(getSupportFragmentManager(), "solveQuestionDialog");
        }
    }

    private void calculateResult() {
        if (checkEmptyField()) {
            if (!e3rabList.isEmpty())
                for (int i = 0; i < staticE3rabList.size(); i++) {
                    if (staticE3rabList.get(i).isAnsCorrect() && staticE3rabList.get(i).isMarkAnsCorrect())
                        // finalResult += Float.valueOf(staticE3rabList.get(i).getAnswer_count());
                        finalResult += 1;
                }
            if (!est5ragList.isEmpty()) {
                for (String o : Const.staticStudentAns.keySet()) {
                    if (Const.staticStudentAns.get(o.toString()))
                        finalResult += 1;
                }
            }

            if (!chooseList.isEmpty())
                for (int i = 0; i < chooseList.size(); i++) {
                    if (chooseList.get(i).getAnswer().equals(chooseList.get(i).getStudentAns()))
                        // finalResult += Float.valueOf(staticE3rabList.get(i).getAnswer_count());
                        finalResult += 1;
                }
            callSendQuizResult();
        } else {
            displayAletDailog();
        }
    }

    private void displayAletDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = new TextView(this);
        textView.setText("");
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setPadding(33, 50, 10, 10);
        builder.setCustomTitle(textView);
        builder.setMessage(getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (!checkEmptyField())
                            Toast.makeText(OneQuestionActivity.this, getString(R.string.answer_all_item), Toast.LENGTH_SHORT).show();
                        else
                            calculateResult();

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void callSendQuizResult() {
        SharedPref pref = SharedPref.getInstance(this);
        AddQuizDegreeRequest request = new AddQuizDegreeRequest();
        request.setUser_id(pref.getInt("id"));
        request.setQuiz_id(questionId);
        request.setDegree(finalResult);
        new FastNetworkManger(this).addQuizDegree(request, this);
    }

    @Override
    public void oneQuestionMark(String qId, float result) {
        if (!questionsResultList.containsKey(qId))
            questionsResultList.put(qId, result);

        if (est5ragList.isEmpty() || e3rabList.isEmpty()) {
            if (questionsResultList.size() == 1) {
                finalResult = result;
                afterResultCalculation();
            }
        } else {
            if (questionsResultList.size() == 2) {
                finalResult = questionsResultList.get(questionId + "_1") + questionsResultList.get(questionId + "_2");
                afterResultCalculation();
            }
        }
    }

    private void afterResultCalculation() {
        checkBtn.setVisibility(View.GONE);
        questionTotalMarkLinear.setVisibility(View.VISIBLE);
        totalMarkTxt.setText((int)finalResult + "/"+qTotalMark);
    }

    @Override
    public void quizDegreeResponse(int answeredQuestionId) {
        Const.staticQuestionMarkTxtList.put(answeredQuestionId, String.valueOf(finalResult));
        afterResultCalculation();
        if (!est5ragList.isEmpty()) {
            est5ragAdapter.activateItemClick(true);
            est5ragAdapter.notifyDataSetChanged();
        }
        if (!e3rabList.isEmpty()) {
            e3rabAdapter.activateItemClick(true);
            e3rabAdapter.notifyDataSetChanged();
        }
        if (!chooseList.isEmpty()) {
            chooseAdapter.activateItemClick(true);
            chooseAdapter.notifyDataSetChanged();
        }
        //finish();
    }

    private boolean checkEmptyField() {
        //checkE3rabAns
        int flagE3rab = 0;
        if (!e3rabList.isEmpty()) {
            if (staticE3rabList.size() < e3rabList.size())
                return false;
            else
                for (int i = 0; i < staticE3rabList.size(); i++) {
                    if (staticE3rabList.get(i).getStudentAns().toString().isEmpty() ||
                            staticE3rabList.get(i).getStudentMarkAns().toString().isEmpty())
                        flagE3rab++;
                }
        }
        if (flagE3rab > 0) {
            return false;
        }


        //checkEst5ragAns
        if (!est5ragList.isEmpty()) {
            int totalAnsCount = 0;
            for (int i = 0; i < est5ragList.size(); i++) {
                totalAnsCount += est5ragList.get(i).getAnswer_count();
            }

            if (Const.staticStudentAns.size() < totalAnsCount)
                return false;
        }

        //checkChooseAns
        boolean flagChooseEmpty = true;
        if (!chooseList.isEmpty())
            for (int i = 0; i < chooseList.size(); i++) {
                if (chooseList.get(i).getStudentAns().isEmpty())
                    flagChooseEmpty = false;
            }

        if (!flagChooseEmpty)
            return false;


        return true;
    }

    @Override
    public void chooseQuestionList(ArrayList<ResponseChooseQuestion.ItemChooseQuestion> chooseQList) {
        qTotalMark = chooseQList.size()+ e3rabList.size()+ est5ragList.size();
        totalMarkTxt.setText("/"+qTotalMark);
        chooseList.clear();
        chooseList.addAll(chooseQList);
        putDataInView();
        chooseAdapter.notifyDataSetChanged();
        if (chooseList.isEmpty())
            chooseFrame.setVisibility(View.GONE);
    }

//    @Override
//    public void onRefresh() {
//        callChooseQuestionApi();
//        refresh.setRefreshing(false);
//
//    }
}