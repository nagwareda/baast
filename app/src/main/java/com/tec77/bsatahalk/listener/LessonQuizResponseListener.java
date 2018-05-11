package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.Quiz.QuizModel;

import java.util.ArrayList;

/**
 * Created by Nagwa on 17/03/2018.
 */

public interface LessonQuizResponseListener {
    public void lessonQuizList(ArrayList<QuizModel> lessonQuizList);
}
