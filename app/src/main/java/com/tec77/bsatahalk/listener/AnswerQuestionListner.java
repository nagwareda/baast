package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.ResponseGetAnswer;

import java.util.ArrayList;

/**
 * Created by mac on 4/26/18.
 */

public interface AnswerQuestionListner {
   public void sendQuestion(ArrayList<ResponseGetAnswer.Answer> answer);

}
