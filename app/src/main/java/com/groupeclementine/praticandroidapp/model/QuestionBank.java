package com.groupeclementine.praticandroidapp.model;

import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionBank(List<Question> questionList) {

        this.mQuestionList = questionList;
        Collections.shuffle(mQuestionList);
        this.mNextQuestionIndex = 0;
    }

    public Question getNextQuestion() {
        this.mNextQuestionIndex++;
        return this.getCurrentQuestion() ;
    }
    public Question getCurrentQuestion() {
        return this.mQuestionList.get(this.mNextQuestionIndex);
    }
}
