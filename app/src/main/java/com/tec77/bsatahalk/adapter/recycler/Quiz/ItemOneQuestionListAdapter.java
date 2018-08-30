package com.tec77.bsatahalk.adapter.Quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.Quiz.QuestionsModel;
import com.tec77.bsatahalk.api.response.Quiz.QuizModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nagwa on 26/03/2018.
 */

public class ItemOneQuestionBtnAdapter extends RecyclerView.Adapter<ItemOneQuestionBtnAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuizModel> quizList = new ArrayList<>();
    private HashMap<Integer, String> questionMarkTextList = new HashMap<>();
    private ArrayList<QuestionsModel> questionsModelList = new ArrayList<>();
    private boolean setDataInTxt = false;
    private SelectedQuestionList callBack;
    private int quizId;

    public ItemOneQuestionBtnAdapter(Context mContext, ArrayList<QuizModel> mQuiz,
                                     HashMap<Integer, String> mQuestionMarkTextList, SelectedQuestionList selectedQuestionList) {
        this.context = mContext;
        this.quizList = mQuiz;
        this.questionMarkTextList = mQuestionMarkTextList;
        this.callBack = selectedQuestionList;
    }

    public interface SelectedQuestionList {
        public void questionList(ArrayList<QuestionsModel> questionsModelList, int selectedPosition);
    }

    @Override
    public ItemOneQuestionBtnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_list_recycler, parent, false);
        ItemOneQuestionBtnAdapter.ViewHolder holder = new ItemOneQuestionBtnAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.emptyViewHolder();
        holder.questionNameTxt.setText(context.getString(R.string.quize_title) + " " + (position + 1));
        if (quizList.get(position).isTakeQuedtion()) {
            //int totalMark = quizList.get(position).getQuizQuestions().
            holder.questionNameTxt.setTextColor(context.getResources().getColor(R.color.gray));
            if (quizList.get(position).getDegree() != -1) {
                holder.questionMarkTxt.setText(quizList.get(position).getDegree() + "");
                holder.questionMarkTxt.setTextColor(context.getResources().getColor(R.color.gray));
            }
        } else {
            holder.questionMarkTxt.setText(quizList.get(position).getTotal_degree() + "");
        }
//        holder.answeredBtn.setText(context.getString(R.string.quize_title) + " " + (position + 1));
//        quizId = quizList.get(position).getId();
//        if (!questionMarkTextList.isEmpty()){
//            if (questionMarkTextList.containsKey(quizId)){
//                holder.questionMarkTxt.setText(questionMarkTextList.get(quizId)+ " " );
////                holder.questionBtn.setVisibility(View.GONE);
////                holder.answeredBtn.setVisibility(View.VISIBLE);
//            }
//
//        }

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionNameTxt, solvedQuestionNameTxt, questionMarkTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            questionNameTxt.setText("");
            questionMarkTxt.setText("");

        }

        private void initViews(final View itemView) {

            questionNameTxt = itemView.findViewById(R.id.ItemQuizList_textView_quizNo);
            questionMarkTxt = itemView.findViewById(R.id.ItemQuizList_txt_totalDegreeTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    questionsModelList = quizList.get(getAdapterPosition()).getQuizQuestions();
                    callBack.questionList(questionsModelList, getAdapterPosition());
                }
            });


        }

    }

//    public void setResultOnText(){
//
//    }
}

