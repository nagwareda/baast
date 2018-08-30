package com.tec77.bsatahalk.adapter.Quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.utils.Const;

import java.util.HashMap;

/**
 * Created by Nagwa on 03/04/2018.
 */

public class ItemEst5ragListAnsAdapter extends RecyclerView.Adapter<ItemEst5ragListAnsAdapter.ViewHolder> {
    private Context context;
    private int itemPosition, totalSize, questionId, questionItemId;
    private boolean ansMark, mShowItem;
    private String itemAns;
    private HashMap<String, String> answerCorrectenceHashMap = new HashMap<>();
    private HashMap<String, String> studentAnswer = new HashMap<>();
    private OnCheckedListener callBack;
    boolean isDuplicatedAns = false;
    private boolean changeFocuse = false;

    public ItemEst5ragListAnsAdapter(Context mContext, int position, String ans,
                                     int size, int mQuestionId, int mQuestionItemId,
                                     OnCheckedListener onCheckedListener) {
        this.context = mContext;
        this.itemPosition = position;
        this.itemAns = ans;
        this.totalSize = size;
        this.questionId = mQuestionId;
        this.questionItemId = mQuestionItemId;
        this.callBack = onCheckedListener;
    }


    @Override
    public ItemEst5ragListAnsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_est5rag_list_ans, parent, false);
        ItemEst5ragListAnsAdapter.ViewHolder holder = new ItemEst5ragListAnsAdapter.ViewHolder(itemView);
        return holder;
    }

    public void changeFocuse() {
        changeFocuse = true;
    }


    public void activateItemClick(boolean showItem) {
        mShowItem = showItem;
        boolean isAnsCorrect = true;
        for(int i=0;i<totalSize;i++)
          isAnsCorrect = isAnsCorrect& Const.staticStudentAns.get( questionId + "_" + questionItemId + "_" + i);
//        for (String i : Const.staticStudentAns.keySet()) {
//            isAnsCorrect = isAnsCorrect & Const.staticStudentAns.get(i);
//        }
        callBack.isAnsCorrect(isAnsCorrect);
    }

    public interface OnCheckedListener {
        public void isAnsCorrect(boolean isCorrect);
    }

    @Override
    public void onBindViewHolder(final ItemEst5ragListAnsAdapter.ViewHolder holder, final int position) {
        holder.emptyViewHolder();
        String ans;
        if (changeFocuse = true) {
            holder.ansEditTxt.setEnabled(false);
            holder.ansEditTxt.setEnabled(true);
        }
        answerCorrectenceHashMap = Const.staticEst5ragList.get(itemPosition).getEst5ragStudentAnsHashMap();
        if (answerCorrectenceHashMap.containsKey(itemPosition + "_" + position)) {
            ans = answerCorrectenceHashMap.get(itemPosition + "_" + position);
            if (mShowItem) {
                holder.ansEditTxt.setEnabled(false);
                if (validAnswer(itemAns, ans) && ans != "") {
                    // holder.ansEditTxt.setVisibility(View.GONE);
                    //holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                } else
                    holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
            }
            holder.ansEditTxt.setText(answerCorrectenceHashMap.get(itemPosition + "_" + position));
        }

        holder.ansEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                holder.ansEditTxt.setError(null);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!studentAnswer.isEmpty()) {
                    if (checkDuplicatedAnswer(holder.ansEditTxt, position)) {
                        studentAnswer.put(itemPosition + "_" + position, holder.ansEditTxt.getText().toString());
                        if (validAnswer(itemAns, holder.ansEditTxt.getText().toString()))
                            ansMark = true;
                        else
                            ansMark = false;

                        Const.staticStudentAns.put(questionId + "_" + questionItemId + "_" + position, ansMark);
                        Const.staticEst5ragList.get(itemPosition).est5ragStudentAnsHashMap
                                .put(itemPosition + "_" + position, holder.ansEditTxt.getText().toString());
                    }else{
                        holder.ansEditTxt.setError(context.getString(R.string.daplicate_answer));
                    }
                } else {
                    if (validAnswer(itemAns, holder.ansEditTxt.getText().toString().trim())) {
                        ansMark = true;
                    } else {
                        ansMark = false;
                    }
                    studentAnswer.put(itemPosition + "_" + position,holder.ansEditTxt.getText().toString());
                    Const.staticStudentAns.put(questionId + "_" + questionItemId + "_" + position, ansMark);
                    Const.staticEst5ragList.get(itemPosition).est5ragStudentAnsHashMap
                            .put(itemPosition + "_" + position, holder.ansEditTxt.getText().toString());


                }
            }
        });

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

    public boolean checkDuplicatedAnswer(final EditText view, final int position) {
        //boolean isDuplicatedAns = false;
        for (String o : studentAnswer.keySet())
            if (!o.equals(itemPosition + "_" + position))
                if (studentAnswer.get(o).trim().equals(view.getText().toString().trim()))
                    return false;

        return true;
    }
//        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!view.getText().toString().isEmpty()) {
//                    if (studentAnswer.containsKey(view.getText().toString()) &&
//                            (!studentAnswer.get(view.getText().toString()).equals(itemPosition + "_" + position))) {
//                        view.setError(context.getString(R.string.daplicate_answer));
//                        isDuplicatedAns = true;
//                    } else {
//                        studentAnswer.put(view.getText().toString(), "value");
//                        if (validAnswer(itemAns, view.getText().toString())) {
//                            ansMark = true;
//                        } else {
//                            ansMark = false;
//
//                        }
//
//                        Const.staticStudentAns.put(questionId + "_" + questionItemId + "_" + position, ansMark);
//                        Const.staticEst5ragList.get(itemPosition).est5ragStudentAnsHashMap
//                                .put(itemPosition + "_" + position, view.getText().toString());
//                    }
//                }
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return totalSize;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText ansEditTxt;
        private float ansMark;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            ansEditTxt.setText("");
            ansEditTxt.setVisibility(View.VISIBLE);
            /// ansEditTxt.setError(null);
        }

        private void initViews(final View itemView) {
            ansEditTxt = itemView.findViewById(R.id.item_EditText_est5rag);
        }

    }
}