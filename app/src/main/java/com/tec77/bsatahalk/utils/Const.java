package com.tec77.bsatahalk.utils;

import com.tec77.bsatahalk.model.StudentAnswerItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nagwa on 23/02/2018.
 */

public class Const {
    public final static String HOST = "https://basstnhalk-site.000webhostapp.com/api/v1/";
    public final static String YOUTUBE_API_KEY ="AIzaSyAbnnPCAI9DbAfUQbX9Hiixq76yPaVjmto";
    public final static String CLASS_LIST_RESPONSE_high ="ثانوي";
    public final static String CLASS_LIST_RESPONSE_intermediate ="اعدادي";
    public final static String CLASS_LIST_RESPONSE_primary ="ابتدائي";
    public static HashMap<Integer,String> staticQuestionMarkTxtList = new HashMap<>();
    public static HashMap<String,Boolean> staticStudentAns = new HashMap<>();
    public static ArrayList<StudentAnswerItem>staticE3rabList = new ArrayList<>();
    public static ArrayList<StudentAnswerItem>staticEst5ragList = new ArrayList<>();
    //public static ArrayList<StudentAnswerItem>staticChooseList = new ArrayList<>();
}
