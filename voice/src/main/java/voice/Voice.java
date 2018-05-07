package voice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class Voice {
    private static final int PERMISSION_RECORD_AUDIO = 0;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private Callback mOnResults = null;
    private Activity mActivity;

    public Voice(Activity activity) {
        mActivity = activity;

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 700);
        mSpeechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
        );

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mActivity);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                final ArrayList<String> matches = bundle.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION
                );
                if (mOnResults != null) {
                    mOnResults.response(matches);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    public void checkPermission() {
        int selfPermission = ContextCompat.checkSelfPermission(
                mActivity, Manifest.permission.RECORD_AUDIO
        );
        if (selfPermission == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    mActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_RECORD_AUDIO
            );
        }
    }

    public void onResults(Callback callback){
        mOnResults = callback;
    }

    public void startListening() {
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    public void stopListening() {
        mSpeechRecognizer.stopListening();
    }

    public interface Callback {
        void response(@NonNull ArrayList<String> matches);
    }
}
