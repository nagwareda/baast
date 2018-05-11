package com.tec77.bsatahalk.api.request;

/**
 * Created by Nagwa on 02/03/2018.
 */

public class LessonRateRequest {

    public void LessonRateRequest(){

    }
    private int lesson_id;

    public int getLessonId() { return this.lesson_id; }

    public void setLessonId(int lesson_id) { this.lesson_id = lesson_id; }

    private int student_id;

    public int getStudentId() { return this.student_id; }

    public void setStudentId(int student_id) { this.student_id = student_id; }

    private boolean degree;

    public boolean getDegree() { return this.degree; }

    public void setDegree(boolean degree) { this.degree = degree; }
}
