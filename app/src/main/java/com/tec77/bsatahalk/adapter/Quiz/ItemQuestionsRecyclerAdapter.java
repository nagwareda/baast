package com.tec77.bsatahalk.adapter.Quiz;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.Quiz.OneQuestionModel;
import com.tec77.bsatahalk.api.response.Quiz.QuestionsModel;

import java.util.ArrayList;

/**
 * Created by Nagwa on 17/03/2018.
 */

public class ItemQuestionsRecyclerAdapter extends RecyclerView.Adapter<ItemQuestionsRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuestionsModel> questionsList = new ArrayList<>();
    private ArrayList<OneQuestionModel> questionNameList = new ArrayList<>();
    private ItemE3rabRecyclerAdapter questionAdapter;

    public ItemQuestionsRecyclerAdapter(Context mContext, ArrayList<QuestionsModel> mQuiz) {
        this.context = mContext;
        this.questionsList = mQuiz;
        // this.classOnClickListener = callBack;
    }

    @Override
    public ItemQuestionsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quize_question_item, parent, false);
        ItemQuestionsRecyclerAdapter.ViewHolder holder = new ItemQuestionsRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemQuestionsRecyclerAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        holder.questionTypeTxt.setText(questionsList.get(position).getQuestionType());
        //questionAdapter = new ItemE3rabRecyclerAdapter(context,questionsList.get(position).getQuestionType(),questionNameList);
        holder.questionsRecycler.setAdapter(questionAdapter);
        holder.questionsRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.questionsRecycler.setLayoutManager(layoutManager);
        questionNameList.addAll(questionsList.get(position).getQuestionName());
        questionAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTypeTxt, est5ragAnsTxt;
        public RecyclerView questionsRecycler;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            questionTypeTxt.setText("");

        }

        private void initViews(final View itemView) {
            questionTypeTxt = itemView.findViewById(R.id.itemQuizeQuestionItem_TextView_questionType);
            questionsRecycler = itemView.findViewById(R.id.itemQuizeQuestionItem_Recycler_questionAns);


        }


    }
}

