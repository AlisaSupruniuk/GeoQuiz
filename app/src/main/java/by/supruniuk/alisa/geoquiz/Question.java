package by.supruniuk.alisa.geoquiz;

public class Question {
    private  int mTextResId;//int т.к. здесть будет храниться идентификатор ресурса(он всегда int) строкового рес. с текстом вопроса
    private  boolean mAnswerTrue;
    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
