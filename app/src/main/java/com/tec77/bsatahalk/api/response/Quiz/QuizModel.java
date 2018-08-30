package com.tec77.bsatahalk.api.response.Quiz;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nagwa on 15/03/2018.
 */

public class QuizModel implements Serializable{
    public QuizModel() {
    }

    private int id;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    private String lesson_id;

    public String getLessonId() { return this.lesson_id; }

    public void setLessonId(String lesson_id) { this.lesson_id = lesson_id; }

    private String quiz_images;

    public String getQuizImages() { return this.quiz_images; }

    public void setQuizImages(String quiz_images) { this.quiz_images = quiz_images; }

    private ArrayList<QuestionsModel> quiz_questions;

    private boolean takeQuedtion;

    private int degree;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public boolean isTakeQuedtion() {
        return takeQuedtion;
    }

    public void setTakeQuedtion(boolean takeQuedtion) {
        this.takeQuedtion = takeQuedtion;
    }

    public ArrayList<QuestionsModel> getQuizQuestions() { return this.quiz_questions; }

    public void setQuizQuestions(ArrayList<QuestionsModel> quiz_questions) { this.quiz_questions = quiz_questions; }

    private int total_degree;

    public int getTotal_degree() {
        return total_degree;
    }

    public void setTotal_degree(int total_degree) {
        this.total_degree = total_degree;
    }
}
