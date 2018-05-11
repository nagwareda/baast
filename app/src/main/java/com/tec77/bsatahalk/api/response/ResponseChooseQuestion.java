package com.tec77.bsatahalk.api.response;

import java.util.ArrayList;

/**
 * Created by Nagwa on 04/05/2018.
 */

public class ResponseChooseQuestion {

    public ResponseChooseQuestion() {
    }

    private boolean success;

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private ArrayList<ItemChooseQuestion> quiz;

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<ItemChooseQuestion> getQuiz() {
        return quiz;
    }

    public void setQuiz(ArrayList<ItemChooseQuestion> quiz) {
        this.quiz = quiz;
    }

    private int code;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class ItemChooseQuestion {
        private int id;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private int question_id;

        public int getQuestionId() {
            return this.question_id;
        }

        public void setQuestionId(int question_id) {
            this.question_id = question_id;
        }

        private String question_value;

        public String getQuestionValue() {
            return this.question_value;
        }

        public void setQuestionValue(String question_value) {
            this.question_value = question_value;
        }

        private String choose_one;

        public String getChooseOne() {
            return this.choose_one;
        }

        public void setChooseOne(String choose_one) {
            this.choose_one = choose_one;
        }

        private String choose_two;

        public String getChooseTwo() {
            return this.choose_two;
        }

        public void setChooseTwo(String choose_two) {
            this.choose_two = choose_two;
        }

        private String choose_three;

        public String getChooseThree() {
            return this.choose_three;
        }

        public void setChooseThree(String choose_three) {
            this.choose_three = choose_three;
        }

        private String choose_four;

        public String getChooseFour() {
            return this.choose_four;
        }

        public void setChooseFour(String choose_four) {
            this.choose_four = choose_four;
        }

        private String answer;

        public String getAnswer() {
            return this.answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        private int studentAnsRadioBtnId;

        public int getStudentAnsRadioBtnId() {
            return studentAnsRadioBtnId;
        }

        public void setStudentAnsRadioBtnId(int studentAnsRadioBtnId) {
            this.studentAnsRadioBtnId = studentAnsRadioBtnId;
        }

        private String studentAns;

        public String getStudentAns() {
            return studentAns;
        }

        public void setStudentAns(String studentAns) {
            this.studentAns = studentAns;
        }
    }
}
