package com.tec77.bsatahalk.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nagwa on 29/03/2018.
 */

public class StudentAnswerItem {
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int question_id;

    private int answer_count;

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

    private String question_value;

    public String getQuestionValue() {
        return this.question_value;
    }

    public void setQuestionValue(String question_value) {
        this.question_value = question_value;
    }

    private String answer;

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String mark_Answer;

    public String getMarkAnswer() {
        return this.mark_Answer;
    }

    public void setMarkAnswer(String mark_Answer) {
        this.mark_Answer = mark_Answer;
    }

    private String studentAns;

    private String studentMarkAns;

    private float itemMark;

    public String getStudentAns() {
        return studentAns;
    }

    public void setStudentAns(String studentAns) {
        this.studentAns = studentAns;
    }

    public float getItemMark() {
        return itemMark;
    }

    public void setItemMark(float itemMark) {
        this.itemMark = itemMark;
    }

    public String getStudentMarkAns() {
        return studentMarkAns;
    }

    public void setStudentMarkAns(String studentMarkAns) {
        this.studentMarkAns = studentMarkAns;
    }

    private boolean ansCorrect;
    private boolean markAnsCorrect;

    public boolean isAnsCorrect() {
        return ansCorrect;
    }

    public void setAnsCorrect(boolean ansCorrect) {
        this.ansCorrect = ansCorrect;
    }

    public boolean isMarkAnsCorrect() {
        return markAnsCorrect;
    }

    public void setMarkAnsCorrect(boolean markAnsCorrect) {
        this.markAnsCorrect = markAnsCorrect;
    }

    public HashMap<String,String> est5ragStudentAnsHashMap = new HashMap<>();

    public HashMap<String, String> getEst5ragStudentAnsHashMap() {
        return est5ragStudentAnsHashMap;
    }

    public void setEst5ragStudentAnsHashMap(HashMap<String, String> est5ragStudentAnsHashMap) {
        this.est5ragStudentAnsHashMap = est5ragStudentAnsHashMap;
    }

    public ArrayList<String> finalAnsArray = new ArrayList<>() ;

    public ArrayList<String> getFinalAnsArray() {
        return finalAnsArray;
    }

    public void setFinalAnsArray(ArrayList<String> finalAnsArray) {
        this.finalAnsArray = finalAnsArray;
    }
}