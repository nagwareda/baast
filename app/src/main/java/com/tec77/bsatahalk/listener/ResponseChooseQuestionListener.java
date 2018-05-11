package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.ResponseChooseQuestion;

import java.util.ArrayList;

/**
 * Created by Nagwa on 04/05/2018.
 */

public interface ResponseChooseQuestionListener {
    public void chooseQuestionList(ArrayList<ResponseChooseQuestion.ItemChooseQuestion>chooseQList);
}
