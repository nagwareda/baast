package com.tec77.bsatahalk.api.response;

import com.tec77.bsatahalk.model.QuestionsModel;

import java.util.ArrayList;

/**
 * Created by mac on 4/26/18.
 */

public class ResponseGetAnswer {


    QuestionsModel question;
    ArrayList<Answer> answer;
    int code;

    public ResponseGetAnswer() {
    }

    public ResponseGetAnswer(QuestionsModel question, ArrayList<Answer> answer, int code) {
        this.question = question;
        this.answer = answer;
        this.code = code;
    }

    public QuestionsModel getQuestion() {
        return question;
    }

    public void setQuestion(QuestionsModel question) {
        this.question = question;
    }

    public ArrayList<Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Answer> answer) {
        this.answer = answer;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

   public class Answer {
        int id;
        String answer;
        long created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public long getCreated_at() {
            return created_at;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }
    }
}
