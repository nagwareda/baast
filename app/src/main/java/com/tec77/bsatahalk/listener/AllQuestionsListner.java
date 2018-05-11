package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.model.QuestionsModel;

import java.util.ArrayList;

/**
 * Created by mac on 4/26/18.
 */

public interface AllQuestionsListner {
     void allQuestions(ArrayList<QuestionsModel> allQuestions,int totalPage);
}
