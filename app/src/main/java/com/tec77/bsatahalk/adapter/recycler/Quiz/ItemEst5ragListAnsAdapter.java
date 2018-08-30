package com.tec77.bsatahalk.adapter.recycler.Quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nagwa on 03/04/2018.
 */

public class ItemEst5ragListAnsAdapter extends RecyclerView.Adapter<ItemEst5ragListAnsAdapter.ViewHolder> {
    private Context context;
    private int itemPosition, totalSize, questionId, questionItemId;
    private boolean ansMark, mShowItem;
    private ArrayList<String> itemAns, tempItemAns;
    private HashMap<String, String> answerCorrectenceHashMap = new HashMap<>();
    private HashMap<String, String> studentAnswer = new HashMap<>();
    private OnCheckedListener callBack;
    private TextView finalAnsTxtView;
    int correctAnsCount = 0;
    boolean isDuplicatedAns = false;
    private boolean changeFocuse = false;
    boolean isAnsCorrect = true;
    String str = "";
    public ItemEst5ragListAnsAdapter(Context mContext, int position, ArrayList<String> ans,
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

//    public void changeFocuse() {
//        changeFocuse = true;
//    }


    public void activateItemClick(boolean showItem, TextView textView) {
        mShowItem = showItem;
        finalAnsTxtView = textView;

    }

    public interface OnCheckedListener {
        public void isAnsNotCorrect(boolean isNotCorrect);
    }

    @Override
    public void onBindViewHolder(final ItemEst5ragListAnsAdapter.ViewHolder holder, final int position) {
        holder.emptyViewHolder();
        String ans;
//        if (changeFocuse = true) {
//            holder.ansEditTxt.setEnabled(false);
//            holder.ansEditTxt.setEnabled(true);
//        }
        answerCorrectenceHashMap = Const.staticEst5ragList.get(itemPosition).getEst5ragStudentAnsHashMap();
        if (answerCorrectenceHashMap.containsKey(itemPosition + "_" + position)) {
            ans = answerCorrectenceHashMap.get(itemPosition + "_" + position);
            if (mShowItem) {

                holder.ansEditTxt.setEnabled(false);
                if (validAnswer(itemAns, ans) && ans != "") {
                    isAnsCorrect = isAnsCorrect & true;
                    // holder.ansEditTxt.setVisibility(View.GONE);
                    //holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                } else {
                    isAnsCorrect = isAnsCorrect & false;
                    holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
                }

//                if (Const.staticStudentAns.size() == totalSize) {
//                    //boolean isAnsCorrect = true;
//                    for (int i = 0; i < totalSize; i++)
//                        isAnsCorrect = isAnsCorrect & Const.staticStudentAns.get(questionId + "_" + questionItemId + "_" + i);
                if (!isAnsCorrect && position == totalSize-1) {
                    finalAnsTxtView.setText(prepareFinalAns(itemPosition));
                    finalAnsTxtView.setVisibility(View.VISIBLE);
                    finalAnsTxtView.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
                    //  }
                    //callBack.isAnsNotCorrect(!isAnsNotCorrect);
                }
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
                        Const.staticEst5ragList.get(itemPosition).est5ragStudentAnsHashMap
                                .put(itemPosition + "_" + position, holder.ansEditTxt.getText().toString());

                        if (validAnswer(itemAns, holder.ansEditTxt.getText().toString())) {
                            ansMark = true;
                            // holder.ansEditTxt.setVisibility(View.GONE);
                            //holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                        } else {
                            ansMark = false;

                        }
                        Const.staticStudentAns.put(questionId + "_" + questionItemId + "_" + position, ansMark);

                    } else {
                        holder.ansEditTxt.setError(context.getString(R.string.daplicate_answer));
                    }
                } else {

                    studentAnswer.put(itemPosition + "_" + position, holder.ansEditTxt.getText().toString());
                    Const.staticEst5ragList.get(itemPosition).est5ragStudentAnsHashMap
                            .put(itemPosition + "_" + position, holder.ansEditTxt.getText().toString());

                    if (validAnswer(itemAns, holder.ansEditTxt.getText().toString())) {
                        ansMark = true;
                        // holder.ansEditTxt.setVisibility(View.GONE);
                        //holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                    } else {
                        ansMark = false;
                        // holder.ansEditTxt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
                    }
                    Const.staticStudentAns.put(questionId + "_" + questionItemId + "_" + position, ansMark);


                }
            }
        });

    }

    private boolean validAnswer(ArrayList<String> ansArr, String answerStudent) {
        String[] stringArray;
        int ansFoundFlag = 0;
        ;
        for (int j = 0; j < ansArr.size(); j++) {
            // if(!ansArr.get(j).equals("")){

            stringArray = ansArr.get(j).split("/");
            for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].trim().equals(answerStudent.trim())) {
                    ansFoundFlag++;
                    break;
                    // return true;
                    //       }
                }
            }
            if (ansFoundFlag > 0)
                break;
        }
        if (ansFoundFlag > 0)
            return true;
        else
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

    public String prepareFinalAns(int pos) {
        correctAnsCount = 0;
        ArrayList<String> finalStudentAns = new ArrayList<>();
        // String answer = questionList.get(pos).getAnswer();
        HashMap<String, String> hashMap = Const.staticEst5ragList.get(pos).est5ragStudentAnsHashMap;
        int ansSize = hashMap.size();
        for (String o : hashMap.keySet()) {
            finalStudentAns.add(hashMap.get(o));

        }

        //  ArrayList<String> finalStudentAns = questionList.get(pos).finalAnsArray;
        String[] stringAnsArray;
        boolean ansFoundFlag;
        for (int i = 0; i < finalStudentAns.size(); i++) {
            ansFoundFlag = false;
            for (int k = 0; k < itemAns.size(); k++) {
                if (!itemAns.get(k).equals("")) {
                    stringAnsArray = itemAns.get(k).split("/");

                    for (int j = 0; j < stringAnsArray.length; j++) {
                        if (finalStudentAns.get(i).trim().equals(stringAnsArray[j].trim())) {
                            itemAns.set(k, "");
                            //correctAnsCount++;
                            ansFoundFlag = true;
                            break;
                        }
                    }
                    if (ansFoundFlag)
                        break;
                }
            }
        }

        for (int i = 0; i < itemAns.size(); i++) {
            if (itemAns.get(i) != "") {
                if (str.equals(""))
                    str = itemAns.get(i).split("/")[0];
                else
                    str = str + "/" + itemAns.get(i).split("/")[0];
            }
        }
        return str;

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