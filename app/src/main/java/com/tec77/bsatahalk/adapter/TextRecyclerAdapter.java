package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.SerialListResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 01/03/2018.
 */

public class TextRecyclerAdapter extends RecyclerView.Adapter<TextRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SerialListResponse.LessonPartModel> lessonList = new ArrayList<>();
    private SerialListResponse.LessonPartModel lessonObj;
    private LessonOnClickListener callBack;


    public TextRecyclerAdapter(Context mContext, ArrayList<SerialListResponse.LessonPartModel> mClassNumberList,
                               LessonOnClickListener listener) {
        this.context = mContext;
        this.lessonList = mClassNumberList;
        this.callBack = listener;
    }

    public interface LessonOnClickListener {
        void onLessonClick(SerialListResponse.LessonPartModel lesson);
    }

    @Override
    public TextRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_recycler, parent, false);
        TextRecyclerAdapter.ViewHolder holder = new TextRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(TextRecyclerAdapter.ViewHolder holder, int position) {
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
        //image

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            lessonName.setText("-");
        }

        private void initViews(final View itemView) {
            lessonName = itemView.findViewById(R.id.itemTextRecycler_text_lesson);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selected_position = getAdapterPosition();
                   callBack.onLessonClick(lessonList.get(selected_position));
                }
            });


        }


    }
}

