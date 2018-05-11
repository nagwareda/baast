package com.tec77.bsatahalk.listener;

import com.tec77.bsatahalk.api.response.ResponseLessonConclusion;

import java.util.ArrayList;

/**
 * Created by Nagwa on 04/05/2018.
 */

public interface LessonConclusionResponseListener {
    public void lessonConclusion(ArrayList<ResponseLessonConclusion.itemConclusion> mConclusionImgList);
}
