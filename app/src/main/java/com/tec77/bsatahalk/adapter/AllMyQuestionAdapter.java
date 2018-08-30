package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.model.QuestionsModel;
import com.tec77.bsatahalk.view.activity.AnswerTeacher;

import java.util.ArrayList;

/**
 * Created by mac on 4/26/18.
 */

public class AllMyQuestionAdapter extends RecyclerView.Adapter<AllMyQuestionAdapter.ViewHolder> {


   private ArrayList<QuestionsModel> allQuestions;
   private Context context;

    public AllMyQuestionAdapter(Context context, ArrayList<QuestionsModel> allQuestions) {
        this.allQuestions=allQuestions;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_my_question, parent, false);
        return  new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.questionDetails.setText(allQuestions.get(position).getQuestion());
        final int  selected=position;
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, AnswerTeacher.class);
                intent.putExtra("id",allQuestions.get(selected).getId());
                intent.putExtra("question",allQuestions.get(selected).getQuestion());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView questionDetails;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AnswerTeacher
                    Intent intent=new Intent(context, AnswerTeacher.class);
                    context.startActivity(intent);
                }
            });
            questionDetails =itemView.findViewById(R.id.item_allQuestions_TextView_quest);
        }
    }
}
