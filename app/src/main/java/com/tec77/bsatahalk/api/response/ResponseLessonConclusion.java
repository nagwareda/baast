package com.tec77.bsatahalk.api.response;

import java.util.ArrayList;

/**
 * Created by Nagwa on 04/05/2018.
 */

public class ResponseLessonConclusion {

    private int code;
    private ArrayList<itemConclusion>lessonPhoto= new ArrayList<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<itemConclusion> getLessonPhoto() {
        return lessonPhoto;
    }

    public void setLessonPhoto(ArrayList<itemConclusion> lessonPhoto) {
        this.lessonPhoto = lessonPhoto;
    }

    public class itemConclusion{
        public itemConclusion() {
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        private String img;
    }
}
