package com.tec77.bsatahalk.adapter.recycler.Quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.model.StudentAnswerItem;

import java.util.ArrayList;

import static com.tec77.bsatahalk.utils.Const.staticE3rabList;

/**
 * Created by Nagwa on 16/03/2018.
 */

public class ItemE3rabRecyclerAdapter extends RecyclerView.Adapter<ItemE3rabRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<StudentAnswerItem> questionList = new ArrayList<>();
    private boolean mShowItem = false;


    public interface TotalQuestionMarkListener {
        void oneQuestionMark(String qId, float result);
    }

    public ItemE3rabRecyclerAdapter(Context mContext, ArrayList<StudentAnswerItem> mQuestionList,
                                    TotalQuestionMarkListener callback) {
        this.context = mContext;
        this.questionList = mQuestionList;
    }

    @Override
    public ItemE3rabRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiez_recycler_answer, parent, false);
        ItemE3rabRecyclerAdapter.ViewHolder holder = new ItemE3rabRecyclerAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemE3rabRecyclerAdapter.ViewHolder holder, int position) {
        holder.emptyViewHolder();
        holder.questionTypeTxt.setText(questionList.get(position).getQuestionValue());

        ArrayList<String> answer = questionList.get(position).getAnswer();
        String answerMark = questionList.get(position).getMarkAnswer();

        String[] stringAnsArray = answer.get(0).split("/");
        String[] stringAnsMarkArray = answerMark.split("/");

        holder.answer1_1_ET.setText(questionList.get(position).getStudentAns());
        holder.answer1_2_ET.setText(questionList.get(position).getStudentMarkAns());
        holder.correct1_1Txt.setText(stringAnsArray[0]);
        holder.correct1_2Txt.setText(stringAnsMarkArray[0]);


        if (mShowItem) {
            if (staticE3rabList.get(position).isAnsCorrect())
                holder.correct1_1Txt.setText("");

            if (staticE3rabList.get(position).isMarkAnsCorrect())
                holder.correct1_2Txt.setText("");

            holder.correct1_1Txt.setVisibility(View.VISIBLE);
            holder.correct1_2Txt.setVisibility(View.VISIBLE);
            holder.answer1_1_ET.setEnabled(false);
            holder.answer1_2_ET.setEnabled(false);
        }
    }

    public void activateItemClick(boolean showItem) {
        mShowItem = showItem;
        //Toast.makeText(context, context.getString(R.string.press_to_display_ans), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTypeTxt, correct1_1Txt, correct1_2Txt;
        public EditText answer1_1_ET, answer1_2_ET;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void emptyViewHolder() {
            questionTypeTxt.setText("");
            correct1_1Txt.setText("");
            correct1_2Txt.setText("");
            correct1_1Txt.setVisibility(View.GONE);
            correct1_2Txt.setVisibility(View.GONE);
            answer1_1_ET.setEnabled(true);
            answer1_2_ET.setEnabled(true);
        }

        private void initViews(final View itemView) {
            questionTypeTxt = itemView.findViewById(R.id.item_quize_answer_TextView_question);
            correct1_1Txt = itemView.findViewById(R.id.item_quize_answer_EditText_correct1_1);
            correct1_2Txt = itemView.findViewById(R.id.item_quize_answer_EditText_correct1_2);
            answer1_1_ET = itemView.findViewById(R.id.item_quize_answer_EditText_answer1_1);
            answer1_2_ET = itemView.findViewById(R.id.item_quize_answer_EditText_answer1_2);


            answer1_1_ET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!TextUtils.isEmpty(answer1_1_ET.getText())) {
                        staticE3rabList.get(getAdapterPosition()).setStudentAns(answer1_1_ET.getText().toString());
                        if (validAnswer(questionList.get(getAdapterPosition()).getAnswer(), answer1_1_ET.getText().toString())) {
                            correct1_1Txt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                            staticE3rabList.get(getAdapterPosition()).setAnsCorrect(true);
                        } else {
                            correct1_1Txt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
                            staticE3rabList.get(getAdapterPosition()).setAnsCorrect(false);
                        }
                    }
                }
            });

            answer1_2_ET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!TextUtils.isEmpty(answer1_2_ET.getText())) {
                        //                      if (questionType.contains("e3rab")) {
                        staticE3rabList.get(getAdapterPosition()).setStudentMarkAns(answer1_2_ET.getText().toString());
                        if (validAnswer(questionList.get(getAdapterPosition()).getMarkAnswer(), answer1_2_ET.getText().toString())) {
                            // if (questionList.get(getAdapterPosition()).getMarkAnswer().contains(answer1_2_ET.getText().toString())) {
                            correct1_2Txt.setTextColor(context.getResources().getColor(R.color.green_color_Dark));
                            staticE3rabList.get(getAdapterPosition()).setMarkAnsCorrect(true);
                        } else {
                            correct1_2Txt.setTextColor(context.getResources().getColor(R.color.red_color_Dark));
                            staticE3rabList.get(getAdapterPosition()).setMarkAnsCorrect(false);
                        }
                    }
                }
            });
        }
    }

    private boolean validAnswer(ArrayList<String> answerArr, String answerStudent) {
        String answer = answerArr.get(0);
        String[] stringArray = answer.split("/");
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].trim().equals(answerStudent.trim())) {
                return true;
            }
        }
        return false;
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
}