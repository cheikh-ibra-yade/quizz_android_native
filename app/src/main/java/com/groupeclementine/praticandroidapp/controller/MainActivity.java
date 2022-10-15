package com.groupeclementine.praticandroidapp.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.androidgamesdk.GameActivity;
import com.groupeclementine.praticandroidapp.R;
import com.groupeclementine.praticandroidapp.model.User;

public class MainActivity extends AppCompatActivity {


    public static final String SHARED_PREFERENCES_USER_INFO_SCORE = "SHARED_PREFERENCES_USER_INFO_SCORE" ;
    TextView mTextView;
    TextView mTextViewScore;
    EditText mEditText;
    Button mButton;
    public static int GAME_ACTIVITY_REQUEST_CODE = 28;
    public static final String SHARED_PREFERENCES_USER_INFO="SHARED_PREFERENCES_USER_INFO";
    public static final String SHARED_PREFERENCES_USER_INFO_NAME="SHARED_PREFERENCES_USER_INFO_NAME";

    User mCurrentUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GAME_ACTIVITY_REQUEST_CODE && requestCode == RESULT_OK){
            int score = data.getIntExtra("BUNDLE_EXTRA_SCORE",0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mCurrentUser = new User();



        mTextView = findViewById(R.id.main_textView_sante);
        mTextViewScore = findViewById(R.id.main_textView_score);
        mEditText = findViewById(R.id.main_editText_name);
        mButton = findViewById(R.id.main_btn_go);

        this.checkUserInfos();

        mButton.setEnabled(false);

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mButton.setEnabled(!editable.toString().isEmpty());
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName= mEditText.getText().toString();
                getSharedPreferences(SHARED_PREFERENCES_USER_INFO, MODE_PRIVATE)
                        .edit()
                        .putString(SHARED_PREFERENCES_USER_INFO_NAME,userName)
                        .apply();
                mCurrentUser.setFullName(userName);

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(intent, GAME_ACTIVITY_REQUEST_CODE);


            }
        });
    }

    private void checkUserInfos(){
        String fullName =getSharedPreferences(SHARED_PREFERENCES_USER_INFO,MODE_PRIVATE)
                .getString(SHARED_PREFERENCES_USER_INFO_NAME,null);

        if(fullName!=null){
            mCurrentUser.setFullName(fullName);
            mTextView.setText("Salaam "+mCurrentUser.getFullName());
            mEditText.setText(mCurrentUser.getFullName());
            //mEditText.setFocusedByDefault(true);
        }else{
            //New User
            mTextView.setText("Bienvenue");
        }

        int score = getSharedPreferences(SHARED_PREFERENCES_USER_INFO,MODE_PRIVATE)
                .getInt(SHARED_PREFERENCES_USER_INFO_SCORE,0);

        mTextViewScore.setText("Votre score est "+Integer.toString(score));
    }
}