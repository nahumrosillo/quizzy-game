package com.quizzy.nahum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizzyActivity extends AppCompatActivity {

    private static final String TAG = "QuizzyActivity";
    private static final String KEY_INDEX = "index";

    private int corrects = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_spain, true),
            new Question(R.string.question_france, true),
            new Question(R.string.question_portugal, false)
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzy);

        if (savedInstanceState != null)
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);

        mTrueButton = (Button) findViewById(R.id.true_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        
        mFalseButton = (Button) findViewById(R.id.false_button);

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                if (mCurrentIndex == mQuestionBank.length-1) {
                    showPercentage();
                }

                updateQuestion();
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();
    }


    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        checkAnswered();
    }


    private void checkAnswered() {

        if (mQuestionBank[mCurrentIndex].isAnswered()) {
            mFalseButton.setEnabled(false);
            mTrueButton.setEnabled(false);
        } else {
            mFalseButton.setEnabled(true);
            mTrueButton.setEnabled(true);
        }
    }


    private void checkAnswer(boolean userPressedTrue) {

        mQuestionBank[mCurrentIndex].setAnswered(true);
        checkAnswered();

        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerTrue) {
            messageResId = R.string.correct_toast;
            corrects++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void showPercentage() {
        int total = mQuestionBank.length;

        Toast.makeText(QuizzyActivity.this, corrects+"/"+total, Toast.LENGTH_SHORT).show();

    }
}

