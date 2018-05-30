package voice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import voice.voice.R;

import static android.speech.SpeechRecognizer.ERROR_AUDIO;
import static android.speech.SpeechRecognizer.ERROR_CLIENT;
import static android.speech.SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS;
import static android.speech.SpeechRecognizer.ERROR_NETWORK;
import static android.speech.SpeechRecognizer.ERROR_NETWORK_TIMEOUT;
import static android.speech.SpeechRecognizer.ERROR_NO_MATCH;
import static android.speech.SpeechRecognizer.ERROR_RECOGNIZER_BUSY;
import static android.speech.SpeechRecognizer.ERROR_SERVER;
import static android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT;

public class Voice {
    private static final int PERMISSION_RECORD_AUDIO = 0;
    private static Resources resources = null;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private Activity activity;

    public Voice(Activity activity) {
        this.activity = activity;
        resources = activity.getResources();

        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                1000
        );
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
        );
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
        );

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
    }

    public void setListener(Listener listener) {
        speechRecognizer.setRecognitionListener(listener);
    }

    public void checkPermission() {
        int selfPermission = ContextCompat.checkSelfPermission(
                activity, Manifest.permission.RECORD_AUDIO
        );
        if (selfPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_RECORD_AUDIO
            );
        }
    }

    public void startListening() {
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public static class Listener implements RecognitionListener {
        private static final String TAG = "Voice Listener";
        private static final Map<Integer, Integer> ERROR = ImmutableMap.<Integer, Integer>builder()
                .put(ERROR_AUDIO, R.string.error_audio)
                .put(ERROR_CLIENT, R.string.error_client)
                .put(ERROR_INSUFFICIENT_PERMISSIONS, R.string.error_insufficient_permissions)
                .put(ERROR_NETWORK, R.string.error_network)
                .put(ERROR_NETWORK_TIMEOUT, R.string.error_network_timeout)
                .put(ERROR_NO_MATCH, R.string.error_no_match)
                .put(ERROR_RECOGNIZER_BUSY, R.string.error_recognizer_busy)
                .put(ERROR_SERVER, R.string.error_server)
                .put(ERROR_SPEECH_TIMEOUT, R.string.error_speech_timeout)
                .build();

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            if (resources == null) return;

            Integer resourceId = ERROR.get(error);
            if (resourceId == null) resourceId = R.string.error_unknown;

            onError(resources.getString(resourceId));
        }

        public void onError(String error) {

        }

        @Override
        public void onResults(Bundle results) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @NonNull
        protected ArrayList<String> getMatches(Bundle results) {
            final ArrayList<String> matches = results.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
            );
            return matches != null ? matches : new ArrayList<>();
        }
    }
}
