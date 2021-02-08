package by.supruniuk.alisa.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.Gravity.TOP;

//Лаунчер на Андроид – это основное приложение в смартфоне,
// которое отвечает за взаимодействие между пользователем и устройством.
// Именно лаунчер (launcher) обеспечивает вывод всей необходимой информации на экран и
// от него в значительной степени зависит удобство управления телефоном.
// Стандартный лаунчер на Андроид имеет весьма ограниченный функционал,
// поэтому большинство производителей модифицируют его для расширения возможностей.

public class QuizActivity extends AppCompatActivity {

    private  static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_FLAG = "flag";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_PRESSED = "pressed";
    private static final String KEY_PRESSED_BUTTON = "pressed_button";
    private static final String KEY_HINT = "hint";

    private int countAnswer = 0;
    private int countCheat = 3;

    private  Button mBtnTrue, mBtnFalse, mBtnCheat;
    private ImageButton mBtnNext, mBtnBack;
    private  TextView mTvQuestion, mTvCountCheat;
    //создаем массив объектов
    //в более сложном проектк такой массив сохдавался бы в другом месте
    Question [] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia,true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private boolean mUserPressed;
    private int mUserPressedButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_FLAG, false);
            countCheat = savedInstanceState.getInt(KEY_HINT, 0);
        }
        mTvQuestion = (TextView) findViewById(R.id.tvQuestion);
        updateQuestion();
        mTvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTvCountCheat = (TextView) findViewById(R.id.tvCountCheat);
        mTvCountCheat.setText("You have " + countCheat + " hint(s).");
        //получаем ссылки на виджеты(кнопки)
        //возвращает объект View
        mBtnTrue = (Button) findViewById(R.id.btnTrue);
        //метод получает в аргументе слушателя, а точнее - объкт, реализующий OnClickListener
        mBtnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //в этом месте кода определен ананимный класс, где this обозначает View.OnClickListener.
                //поэтому в аргументе Context надо прописывать полностью QuizActivity.this
                mUserPressedButton = 1;
                checkAnswer(true);
                mBtnFalse.setEnabled(false);
            }
        });

        mBtnFalse = (Button) findViewById(R.id.btnFalse);
        mBtnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserPressedButton = 2;
                checkAnswer(false);
                mBtnTrue.setEnabled(false);
            }
        });

        if (savedInstanceState != null){
            if (mIsCheater){
                mUserPressed = savedInstanceState.getBoolean(KEY_PRESSED, false);
                if (mUserPressed){
                    mBtnFalse.setEnabled(false);
                } else {
                    mBtnTrue.setEnabled(false);
                }
            } else {
                mUserPressedButton = savedInstanceState.getInt(KEY_PRESSED_BUTTON, 0);
                if (mUserPressedButton == 1){
                    mBtnFalse.setEnabled(false);
                }
                if (mUserPressedButton == 2){
                    mBtnTrue.setEnabled(false);
                }
            }
        }

        mBtnNext = (ImageButton) findViewById(R.id.btnNext);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == (mQuestionBank.length - 1)) {
                    mTvQuestion.setText("Вы ответили на " + countAnswer + " вопросов верно.");
                    mBtnFalse.setVisibility(View.INVISIBLE);
                    mBtnTrue.setVisibility(View.INVISIBLE);
                    mBtnNext.setVisibility(View.INVISIBLE);
                    mBtnBack.setVisibility(View.INVISIBLE);
                    mBtnCheat.setVisibility(View.INVISIBLE);
                } else {
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                    mIsCheater = false;
                    updateQuestion();
                    mBtnTrue.setEnabled(true);
                    mBtnFalse.setEnabled(true);
                }
            }
        });
        mBtnBack = (ImageButton) findViewById(R.id.btnBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mBtnCheat = (Button) findViewById(R.id.btnCheat);
        mBtnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countCheat > 0){
                    //Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                    boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                    Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                    //startActivity(intent);
                    startActivityForResult(intent, REQUEST_CODE_CHEAT);
                    countCheat -= 1;
                }
                if (countCheat == 0){
                    mBtnCheat.setVisibility(View.INVISIBLE);
                }
                mTvCountCheat.setText("You have " + countCheat + " hint(s).");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShow(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public  void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        saveInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        saveInstanceState.putBoolean(KEY_FLAG, mIsCheater);
        saveInstanceState.putBoolean(KEY_PRESSED, mUserPressed);
        saveInstanceState.putInt(KEY_PRESSED_BUTTON, mUserPressedButton);
        saveInstanceState.putInt(KEY_HINT, countCheat);
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public  void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
    private void updateQuestion(){
        //Log.d(TAG, "Update question text", new Exception());
        int question  = mQuestionBank[mCurrentIndex].getTextResId();
        mTvQuestion.setText(question);
    }
    private  void  checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                countAnswer += 1;
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(TOP,0,0);
        toast.show();
    }
}