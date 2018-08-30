package com.tec77.bsatahalk.adapter.recycler.Quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.response.ResponseChooseQuestion;

import java.util.ArrayList;

/**
 * Created by Nagwa on 04/05/2018.
 */

public class ItemChooseRecyclerAdapter extends RecyclerView.Adapter<ItemChooseRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ResponseChooseQuestion.ItemChooseQuestion> chooseList = new ArrayList<>();
    private boolean mShowItem;

    public ItemChooseRecyclerAdapter(Context mContext, ArrayList<ResponseChooseQuestion.ItemChooseQuestion> mChooseList) {
        this.context = mContext;
        this.chooseList = mChooseList;
    }

    @Override
    public ItemChooseRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_choose_question, parent, false);
        ItemChooseRecyclerAdapter.ViewHolder holder = new ItemChooseRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    public void activateItemClick(boolean showItem) {
        mShowItem = showItem;
    }

    @Override
    public void onBindViewHolder(ItemChooseRecyclerAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        ResponseChooseQuestion.ItemChooseQuestion obj = new ResponseChooseQuestion().new ItemChooseQuestion();
        obj = chooseList.get(position);
        holder.questionTxt.setText(obj.getQuestionValue());
        holder.ansTxt.setText(obj.getAnswer());
        holder.radioButton1.setText(obj.getChooseOne());
        holder.radioButton2.setText(obj.getChooseTwo());
        holder.radioButton3.setText(obj.getChooseThree());
        holder.radioButton4.setText(obj.getChooseFour());

        if (mShowItem) {

            //handel view as student ans
            int selectedRBId = obj.getStudentAnsRadioBtnId();

            if (holder.radioButton1.getId() == selectedRBId) {
                holder.chooseRadioGroup.check(holder.radioButton1.getId());

            } else if (holder.radioButton2.getId() == selectedRBId) {
                holder.chooseRadioGroup.check(holder.radioButton2.getId());

            } else if (holder.radioButton3.getId() == selectedRBId) {
                holder.chooseRadioGroup.check(holder.radioButton3.getId());
            }
             else if (holder.radioButton4.getId() == selectedRBId) {
                holder.chooseRadioGroup.check(holder.radioButton4.getId());

            }

            if (chooseList.get(position).getAnswer().trim().equals(chooseList.get(position).getStudentAns().trim())) {
                holder.ansTxt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                holder.ansTxt.setVisibility(View.GONE);
            } else {
                holder.ansTxt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
                holder.ansTxt.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chooseList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTxt, ansTxt;
        public RadioGroup chooseRadioGroup;
        public RadioButton radioButton1, radioButton2, radioButton3,radioButton4;
        String studentAns = "";

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            chooseRadioGroup.clearCheck();
            questionTxt.setText("");
            ansTxt.setText("");
            ansTxt.setVisibility(View.GONE);
        }

        private void initViews(final View itemView) {
            questionTxt = itemView.findViewById(R.id.item_chhoseList_TextView_question);
            ansTxt = itemView.findViewById(R.id.item_chooseList_TextView_ans);
            chooseRadioGroup = itemView.findViewById(R.id.item_chooseList_radioGroup);
            radioButton1 = itemView.findViewById(R.id.item_chooseList_radioBtn1);
            radioButton2 = itemView.findViewById(R.id.item_chooseList_radioBtn2);
            radioButton3 = itemView.findViewById(R.id.item_chooseList_radioBtn3);
            radioButton4 = itemView.findViewById(R.id.item_chooseList_radioBtn4);

            chooseRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    if (i == radioButton1.getId())
                        studentAns = radioButton1.getText().toString();
                    else if (i == radioButton2.getId())
                        studentAns = radioButton2.getText().toString();
                    else if (i == radioButton3.getId())
                        studentAns = radioButton3.getText().toString();
                    else if (i == radioButton4.getId())
                        studentAns = radioButton4.getText().toString();

                    if (!mShowItem){
                        chooseList.get(getAdapterPosition()).setStudentAnsRadioBtnId(i);
                        chooseList.get(getAdapterPosition()).setStudentAns(studentAns);
                    }else{
                        for (int j = 0; j < chooseRadioGroup.getChildCount(); j++) {
                            chooseRadioGroup.getChildAt(j).setEnabled(false);
                        }
                    }
                }


            });

        }
    }

}

