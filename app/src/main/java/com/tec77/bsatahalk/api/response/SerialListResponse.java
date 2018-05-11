package com.tec77.bsatahalk.api.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nagwa on 01/03/2018.
 */

public class SerialListResponse {

    public void SerialListResponse(){

    }


    private boolean success;

    public boolean getSuccess() { return this.isSuccess(); }

    public void setSuccess(boolean success) { this.success = success; }

    private int code;

    public int getCode() { return this.code; }

    public void setCode(int code) { this.code = code; }

    private String message;

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    private ArrayList<LessonPartModel> data;

    public ArrayList<LessonPartModel> getData() { return this.data; }

    public void setData(ArrayList<LessonPartModel> data) { this.data = data; }

    private int page;

    public int getPage() { return this.page; }

    public void setPage(int page) { this.page = page; }

    private int pageCount;

    public int getPageCount() { return this.pageCount; }

    public void setPageCount(int pageCount) { this.pageCount = pageCount; }

    private int limit;

    public int getLimit() { return this.limit; }

    public void setLimit(int limit) { this.limit = limit; }

    private int totalCount;

    public int getTotalCount() { return this.totalCount; }

    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public boolean isSuccess() {
        return success;
    }

    public class LessonPartModel implements Serializable{

        public void LessonPartModel(){

        }
        private int id;

        public int getId() { return this.id; }

        public void setId(int id) { this.id = id; }

        private String name;

        public String getName() { return this.name; }

        public void setName(String name) { this.name = name; }

        private String lesson_Type;

        public String getLessonType() { return this.lesson_Type; }

        public void setLessonType(String lesson_Type) { this.lesson_Type = lesson_Type; }

        private String url;

        public String getUrl() { return this.url; }

        public void setUrl(String url) { this.url = url; }

        private String user_arrange;

        public String getUserArrange() { return this.user_arrange; }

        public void setUserArrange(String user_arrange) { this.user_arrange = user_arrange; }

        private String student_arrange;

        public String getStudentArrange() { return this.student_arrange; }

        public void setStudentArrange(String student_arrange) { this.student_arrange = student_arrange; }
    }
}
