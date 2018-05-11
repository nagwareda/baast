package com.tec77.bsatahalk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.ResponseLessonConclusion;
import com.tec77.bsatahalk.api.response.SerialListResponse;

import java.util.ArrayList;

/**
 * Created by Nagwa on 03/05/2018.
 */

public class LessonConclusionAdapter extends RecyclerView.Adapter<LessonConclusionAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ResponseLessonConclusion.itemConclusion> lessonConclusionList = new ArrayList<>();
    OnImgClickListener onImgClickListener;
//    private SerialListResponse.LessonPartModel lessonObj;
//    private TextRecyclerAdapter.LessonOnClickListener callBack;


    public LessonConclusionAdapter(Context mContext, ArrayList<ResponseLessonConclusion.itemConclusion> mLessonConclusionList
    ,OnImgClickListener callBack) {
        this.context = mContext;
        this.lessonConclusionList = mLessonConclusionList;
        onImgClickListener = callBack;

    }

    public interface OnImgClickListener {
        void onImgClick(String imgUrl);
    }

    @Override
    public LessonConclusionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conclusion_recycler, parent, false);
        LessonConclusionAdapter.ViewHolder holder = new LessonConclusionAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(LessonConclusionAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();

        Glide.with(context)
                .load(lessonConclusionList.get(position).getImg())
                .into(holder.img);



       // holder.img.showImage(Uri.parse(lessonConclusionList.get(position).getImg()));
//        Picasso.with(context)
//                .load(lessonConclusionList.get(position).getImg())
//                .placeholder(R.color.gray)
//                .error(R.color.blackColor)
//                .centerCrop()
//                .fit()
//                .into(holder.img);
       // holder.lessonName.setText(lessonObj.getName());
    }

    @Override
    public int getItemCount() {
        return lessonConclusionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        //image

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
        }

        private void initViews(final View itemView) {
            img = itemView.findViewById(R.id.itemLessonConclusion_imgView_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selected_position = getAdapterPosition();
                    onImgClickListener.onImgClick(lessonConclusionList.get(selected_position).getImg());
                }
            });


        }


    }
}

