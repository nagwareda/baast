package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.SerialListResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 03/05/2018.
 */

public class LessonsRecyclerAdapter extends RecyclerView.Adapter<LessonsRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SerialListResponse.LessonPartModel> lessonList = new ArrayList<>();
    private SerialListResponse.LessonPartModel lessonObj;
    private LessonsRecyclerAdapter.LessonOnClickListener callBack;


    public LessonsRecyclerAdapter(Context mContext, ArrayList<SerialListResponse.LessonPartModel> mClassNumberList,
                                  LessonsRecyclerAdapter.LessonOnClickListener listener) {
        this.context = mContext;
        this.lessonList = mClassNumberList;
        this.callBack = listener;
    }

    public interface LessonOnClickListener {
        void onLessonClick(SerialListResponse.LessonPartModel lesson,String type);
    }

    @Override
    public LessonsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson_list, parent, false);
        LessonsRecyclerAdapter.ViewHolder holder = new LessonsRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(LessonsRecyclerAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        lessonObj = lessonList.get(position);
        holder.lessonName.setText(lessonObj.getName());
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lessonName;
        private LinearLayout conclusionCardView, descriptionCardView;
        public  int term;
        //image

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            lessonName.setText("-");
        }

        private void initViews(final View itemView) {
            //final int term;
            lessonName = itemView.findViewById(R.id.itemLessonList_text_lesson);
            conclusionCardView = itemView.findViewById(R.id.itemLessonList_cardView_conclusion);
            descriptionCardView = itemView.findViewById(R.id.itemLessonList_cardView_description);
            conclusionCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selected_position = getAdapterPosition();
                    callBack.onLessonClick(lessonList.get(selected_position),"conclusion");
                }
            });
            descriptionCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selected_position = getAdapterPosition();
                    callBack.onLessonClick(lessonList.get(selected_position),"description");
                }
            });


        }


    }
}


