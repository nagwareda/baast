package com.tec77.bsatahalk.api.response.Quiz;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nagwa on 15/03/2018.
 */

public class QuestionsModel implements Serializable {

    public QuestionsModel() {
    }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private String quiz_id;

    public String getQuizId() { return this.quiz_id; }

    public void setQuizId(String quiz_id) { this.quiz_id = quiz_id; }

    private String question_Type;

    public String getQuestionType() { return this.question_Type; }

    public void setQuestionType(String question_Type) { this.question_Type = question_Type; }

    private ArrayList<OneQuestionModel> questionName;

    public ArrayList<OneQuestionModel> getQuestionName() { return this.questionName; }

    public void setQuestionName(ArrayList<OneQuestionModel> questionName) { this.questionName = questionName; }
}
