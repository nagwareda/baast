package com.tec77.bsatahalk.adapter.Quiz;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.Quiz.QuestionsModel;
import com.tec77.bsatahalk.api.response.Quiz.QuizModel;

import java.util.ArrayList;

/**
 * Created by Nagwa on 15/03/2018.
 */

public class ItemLessonQuizRecyclerAdapter extends RecyclerView.Adapter<ItemLessonQuizRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QuizModel> quizList = new ArrayList<>();
    private ArrayList<QuestionsModel>questionsList = new ArrayList<>();
    private ItemQuestionsRecyclerAdapter lessonQuestionAdapter;

    public ItemLessonQuizRecyclerAdapter(Context mContext, ArrayList<QuizModel> mQuiz) {
        this.context = mContext;
        this.quizList = mQuiz;
        // this.classOnClickListener = callBack;
    }

    @Override
    public ItemLessonQuizRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quize_image_question_recycler, parent, false);
        ItemLessonQuizRecyclerAdapter.ViewHolder holder = new ItemLessonQuizRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemLessonQuizRecyclerAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        String questionUrl = quizList.get(position).getQuizImages();
        if(!questionUrl.isEmpty())
            Picasso.with(context)
                    .load(questionUrl)
                    .placeholder(R.color.blackColor)
                    .error(R.color.blackColor)
                    .centerCrop().fit()
                    .into(holder.questionImg);
        lessonQuestionAdapter = new ItemQuestionsRecyclerAdapter(context,questionsList);
        holder.lessonQuestionsRecycler.setAdapter(lessonQuestionAdapter);
        holder.lessonQuestionsRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.lessonQuestionsRecycler.setLayoutManager(layoutManager);
        questionsList.addAll(quizList.get(position).getQuizQuestions());
        lessonQuestionAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView questionImg;
        public  RecyclerView lessonQuestionsRecycler;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {

        }

        private void initViews(final View itemView) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int selected_position = getAdapterPosition();
//                    // classOnClickListener.onClassClick(level,classList.get(selected_position));
//                    Intent intent = new Intent(context, StudentListActivity.class);
//                    intent.putExtra("category", level);
//                    intent.putExtra("year", classList.get(selected_position).getId());
//                    context.startActivity(intent);
//                }
//            });

            questionImg = itemView.findViewById(R.id.item_quizeRecyclerImage_imageView);
            lessonQuestionsRecycler = itemView.findViewById(R.id.item_quizeRecyclerImage_questionTypeRecycler);


        }


    }
}

