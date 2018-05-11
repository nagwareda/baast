package com.tec77.bsatahalk.api.response.Quiz;

import java.io.Serializable;

/**
 * Created by Nagwa on 15/03/2018.
 */

public class OneQuestionModel implements Serializable{

    public OneQuestionModel() {
    }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private int question_id;

    private int answer_count;

    private String question_value;

    public String getQuestionValue() { return this.question_value; }

    public void setQuestionValue(String question_value) { this.question_value = question_value; }

    private String answer;

    public String getAnswer() { return this.answer; }

    public void setAnswer(String answer) { this.answer = answer; }

    private String mark_Answer;

    public String getMarkAnswer() { return this.mark_Answer; }

    public void setMarkAnswer(String mark_Answer) { this.mark_Answer = mark_Answer; }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public String getQuestion_value() {
        return question_value;
    }

    public void setQuestion_value(String question_value) {
        this.question_value = question_value;
    }

    public String getMark_Answer() {
        return mark_Answer;
    }

    public void setMark_Answer(String mark_Answer) {
        this.mark_Answer = mark_Answer;
    }
}

