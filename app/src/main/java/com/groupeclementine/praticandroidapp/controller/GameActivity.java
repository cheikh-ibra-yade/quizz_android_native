package com.groupeclementine.praticandroidapp.controller;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.groupeclementine.praticandroidapp.R;
import com.groupeclementine.praticandroidapp.model.Question;
import com.groupeclementine.praticandroidapp.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAnswer1;Button btnAnswer2;Button btnAnswer3;Button btnAnswer4;
    TextView textVQuestion;
    int mReminingQuestionCount;
    int score;
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    boolean mEnableTouchEvent = true;


    QuestionBank mQuestionBank = this.generateQuestionBank();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        textVQuestion = findViewById(R.id.activity_game_textV_question);
        btnAnswer1 = findViewById(R.id.activity_game_btnAnswer1);
        btnAnswer2 = findViewById(R.id.activity_game_btnAnswer2);
        btnAnswer3 = findViewById(R.id.activity_game_btnAnswer3);
        btnAnswer4 = findViewById(R.id.activity_game_btnAnswer4);

        btnAnswer1.setOnClickListener(this);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3.setOnClickListener(this);
        btnAnswer4.setOnClickListener(this);

        this.displayQuestion(this.mQuestionBank.getCurrentQuestion());

        this.mReminingQuestionCount=3;
        this.score=0;

        if(savedInstanceState != null){
            mReminingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
            score = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
        }

    }

    private QuestionBank generateQuestionBank(){
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0
        );

        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3
        );

        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3
        );

        return new QuestionBank(Arrays.asList(question1,
                    question2,question3));
    }


    private void displayQuestion(final Question question){
        textVQuestion.setText(question.getQuestion());
        btnAnswer1.setText(question.getChoiceList().get(0));
        btnAnswer2.setText(question.getChoiceList().get(1));
        btnAnswer3.setText(question.getChoiceList().get(2));
        btnAnswer4.setText(question.getChoiceList().get(3));
    }

    @Override
    public void onClick(View v) {
        mEnableTouchEvent=false;
        int index;
        if (v == btnAnswer1) {
            index = 0;
        } else if (v == btnAnswer2) {
            index = 1;
        } else if (v == btnAnswer3) {
            index = 2;
        } else if (v == btnAnswer4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unexpected value: " + v);
        }

        if(this.mQuestionBank.getCurrentQuestion().getAnswerIndex() ==  index){
            Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
            this.score++;
        }else{
            Toast.makeText(this,"Faux",Toast.LENGTH_SHORT).show();

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvent=true;
                if(--mReminingQuestionCount>0){
                    displayQuestion(mQuestionBank.getNextQuestion());
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(GameActivity.this);
                    dialog.setTitle("Well doone")
                            .setMessage("Ton Score est "+score)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(GameActivity.this,MainActivity.class);
                                    intent.putExtra(BUNDLE_EXTRA_SCORE,score);
                                    setResult(RESULT_OK, intent);

                                    startActivity(intent);
                                    //finish();
                                }
                            }).create().show();
                }
            }
        },2000);


    }

    private void saveScore(){
        getSharedPreferences(MainActivity.SHARED_PREFERENCES_USER_INFO, MODE_PRIVATE)
                .edit()
                .putInt(MainActivity.SHARED_PREFERENCES_USER_INFO_SCORE,score)
                .apply();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvent && super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, score);
        outState.putInt(BUNDLE_STATE_QUESTION, mReminingQuestionCount);
    }
}