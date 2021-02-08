package by.supruniuk.alisa.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.PropertyResourceBundle;

public class CheatActivity extends AppCompatActivity {

    private  static final String TAG = "CheatActivity";

    private static final String EXTRA_ANSWER_IS_TRUE = "by.supruniuk.alisa.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOW = "by.supruniuk.alisa.geoquiz.answer_show";

    private boolean mAnswerIsTrue;

    private TextView mTvAnswer, mTvVersionAPI;
    private Button mBtnShowAnswer;

    private static  final String KEY_SHOW_ANSWER = "show_answer";
    private static  final String KEY_ANSWER = "answer";
    private boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle) called in CheatActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            result = savedInstanceState.getBoolean(KEY_SHOW_ANSWER, false);
            Log.d(TAG, "check onSaveInstanceState called in CheatActivity.Create");
            setAnswerShowResult(result);
        }

        mTvAnswer = (TextView) findViewById(R.id.tvAnswer);
        mTvVersionAPI = (TextView) findViewById(R.id.tvVersionAPI);
        mTvVersionAPI.setText("API Level " + String.valueOf(Build.VERSION.SDK_INT));
        if (savedInstanceState != null){
            mTvAnswer.setText((savedInstanceState.getString(KEY_ANSWER, "")).toString());
        }
        mBtnShowAnswer = (Button) findViewById(R.id.btnShowAnswer);
        mBtnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue){
                    mTvAnswer.setText(R.string.true_button);
                } else {
                    mTvAnswer.setText(R.string.false_button);
                }
                setAnswerShowResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mBtnShowAnswer.getWidth() / 2;
                    int cy = mBtnShowAnswer.getHeight() / 2;
                    float radius = mBtnShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mBtnShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mBtnShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mBtnShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public  void onSaveInstanceState(Bundle saveInstanceState) {

        Log.i(TAG, "onSaveInstanceState called in CheatActivity");
        saveInstanceState.putBoolean(KEY_SHOW_ANSWER, result);
        saveInstanceState.putString(KEY_ANSWER, mTvAnswer.getText().toString());
        super.onSaveInstanceState(saveInstanceState);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Log.d(TAG, "newIntent called in CheatActivity");
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShow(Intent result){
        Log.d(TAG, "wasAnswerShow called in CheatActivity");
        return result.getBooleanExtra(EXTRA_ANSWER_SHOW, false);
    }

    private void setAnswerShowResult(boolean isAnswerShow){
        Log.d(TAG, "setAnswerShowResult called in CheatActivity");
        result = isAnswerShow;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOW, result);
        setResult(RESULT_OK, data);

    }
}