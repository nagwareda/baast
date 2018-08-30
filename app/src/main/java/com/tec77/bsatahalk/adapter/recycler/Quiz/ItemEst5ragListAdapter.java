package com.tec77.bsatahalk.adapter.Quiz;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.model.StudentAnswerItem;
import com.tec77.bsatahalk.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nagwa on 08/04/2018.
 */

public class ItemEst5ragListAdapter extends RecyclerView.Adapter<ItemEst5ragListAdapter.ViewHolder>
        implements ItemEst5ragListAnsAdapter.OnCheckedListener {
    private Context context;
    private ArrayList<StudentAnswerItem> questionList = new ArrayList<>();
    private boolean mShowItem, ansCorrect;
    String[] stringAnsArray;
    ItemEst5ragListAnsAdapter adapter;
    int correctAnsCount = 0;


    public ItemEst5ragListAdapter(Context mContext, ArrayList<StudentAnswerItem> mQuiz) {
        this.context = mContext;
        this.questionList = mQuiz;
    }

    @Override
    public void isAnsCorrect(boolean isCorrect) {
        this.ansCorrect = isCorrect;


    }


    @Override
    public ItemEst5ragListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_est5rag_list1, parent, false);
        ItemEst5ragListAdapter.ViewHolder holder = new ItemEst5ragListAdapter.ViewHolder(itemView);
        return holder;
    }

    public void activateItemClick(boolean showItem) {
        mShowItem = showItem;
    }


    @Override
    public void onBindViewHolder(ItemEst5ragListAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        holder.questionWordTxt.setText(questionList.get(position).getQuestionValue());

        String answer = questionList.get(position).getAnswer();
        stringAnsArray = answer.split("/");
       // holder.ansWordTxt.setText(questionList.get(position).getAnswer());

        int ans_count = questionList.get(position).getAnswer_count();
        adapter = new ItemEst5ragListAnsAdapter(context, position,
                questionList.get(position).getAnswer(), ans_count, questionList.get(position).getQuestion_id(),
                questionList.get(position).getId(), this);
        holder.est5ragAnsRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.est5ragAnsRecycler.setAdapter(adapter);
        holder.est5ragAnsRecycler.setLayoutManager(layoutManager);
        if (mShowItem) {
            adapter.activateItemClick(true);
            String str = convertHashMapToArray(position);
            //to display the answer only if wrong answer exist
            if (!ansCorrect) {
                holder.ansWordTxt.setText(str);
                holder.ansWordTxt.setVisibility(View.VISIBLE);
                holder.ansWordTxt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
           }

        }
        adapter.notifyDataSetChanged();

    }

    private String convertHashMapToArray(int pos) {
        correctAnsCount = 0;
        String answer = questionList.get(pos).getAnswer();
        HashMap<String, String> hashMap = Const.staticEst5ragList.get(pos).est5ragStudentAnsHashMap;
        int ansSize = hashMap.size();
        for (String o : hashMap.keySet()) {
            questionList.get(pos).finalAnsArray.add(hashMap.get(o));

        }

        ArrayList<String> finalStudentAns = questionList.get(pos).finalAnsArray;
        for (int i = 0; i < ansSize; i++) {
            for (int j = 0; j < stringAnsArray.length; j++) {
                if (finalStudentAns.get(i).trim().contains(stringAnsArray[j].trim())) {
                    stringAnsArray[j] = "";
                    correctAnsCount++;
                }
            }
        }
        String str = "";
        for (int i = 0; i < stringAnsArray.length; i++) {
            if (stringAnsArray[i] != "")
                str = str + "/" + stringAnsArray[i];
        }
        return str;
    }

    private boolean validAnswer(String answer, String answerStudent) {

        String[] stringArray = answer.split("/");
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].trim().equals(answerStudent.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionWordTxt, ansWordTxt;
        public RecyclerView est5ragAnsRecycler;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            questionWordTxt.setText("");
            ansWordTxt.setText("");
            ansCorrect = false;
            //ansWordTxt.setVisibility(View.VISIBLE);
        }

        private void initViews(final View itemView) {
            questionWordTxt = itemView.findViewById(R.id.item_est5ragList_TextView_question);
            ansWordTxt = itemView.findViewById(R.id.item_est5ragList_TextView_ans);
            est5ragAnsRecycler = itemView.findViewById(R.id.item_est5ragList_TextView_recycler);


        }
    }
}
