package com.tec77.bsatahalk.model;

/**
 * Created by mac on 4/26/18.
 */

public class QuestionsModel {
    private int id;
    private String question;
    private long created_at;

    public QuestionsModel(int id, String question, long created_at) {
        this.id = id;
        this.question = question;
        this.created_at = created_at;
    }

    public QuestionsModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
