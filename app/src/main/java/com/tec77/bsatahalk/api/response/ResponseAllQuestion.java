package com.tec77.bsatahalk.api.response;

import com.tec77.bsatahalk.model.QuestionsModel;

import java.util.ArrayList;

/**
 * Created by mac on 4/26/18.
 */

public class ResponseAllQuestion {


    private boolean success;
    private int code;
    private String message;

    private ArrayList<QuestionsModel> data = new ArrayList<QuestionsModel>();
    private int page;
    private int pageCount;
    private int limit;
    private int totalCount;


    public ResponseAllQuestion() {
    }

    public ResponseAllQuestion(boolean success, int code, String message, ArrayList<QuestionsModel> data, int page, int pageCount, int limit, int totalCount) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.page = page;
        this.pageCount = pageCount;
        this.limit = limit;
        this.totalCount = totalCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<QuestionsModel> getData() {
        return data;
    }

    public void setData(ArrayList<QuestionsModel> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
