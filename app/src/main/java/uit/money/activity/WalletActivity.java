package uit.money.activity;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;

import model.model.User;
import model.model.Wallet;
import uit.money.R;
import uit.money.databinding.ActivityWalletBinding;
import uit.money.facebook.Credential;
import voice.Voice;
import voice.recognizer.RecognizerBill;
import voice.recognizer.RecognizerWallet;

import static uit.money.utils.Timer.setTimeout;

public class WalletActivity extends RealmActivity {
    private static final String TAG = "WalletActivity";
    private Wallet wallet;
    private Voice voice;
    private State state = new State();
    private ActivityWalletBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initializeVoice();
        initializeWallet();
        initializeDataBinding();
    }

    private void initializeWallet() {
        wallet = Wallet.getCurrentWallet();
        if (wallet != null) {
            wallet.initialize();
        }
    }

    private void initializeVoice() {
        voice = new Voice(this);
        voice.setListener(new Voice.Listener() {
            @Override
            public void onBeginningOfSpeech() {
                super.onBeginningOfSpeech();
                state.speechRecognizerString.set(getString(R.string.voice_start));
                state.setIsShowSpeechRecognizerBar(true);
            }

            @Override
            public void onRmsChanged(float db) {
                float ratio;
                float min = 2;
                float max = 10;
                if (db < min) {
                    ratio = 0;
                } else if (db > max) {
                    ratio = 1;
                } else {
                    ratio = (db - min) / (max - min);
                }
                state.ratio.set(ratio);
            }

            @Override
            public void onError(String error) {
                state.speechRecognizerString.set(error);
                state.ratio.set(0);
                setTimeout(() -> state.setIsShowSpeechRecognizerBar(false), 1200);
            }

            @Override
            public void onResults(Bundle results) {
                super.onResults(results);
                final ArrayList<String> log = getMatches(results);
                state.speechRecognizerString.set(log.get(0));
                state.ratio.set(0);
                setTimeout(() -> state.setIsShowSpeechRecognizerBar(false), 1200);

                RecognizerBill recognizerBill = new RecognizerBill(wallet, log.get(0));
                if (recognizerBill.isValid()) recognizerBill.copyToRealmOrUpdate();

                RecognizerWallet recognizerWallet = new RecognizerWallet(wallet, log.get(0));
                if (recognizerWallet.isValid()) recognizerWallet.start();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                super.onPartialResults(partialResults);
                final ArrayList<String> matches = getMatches(partialResults);
                state.speechRecognizerString.set(matches.get(0));
            }
        });
    }

    private void initializeDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        binding.setUser(User.getCurrentUser());
        binding.setState(state);
        binding.setWallet(wallet);
    }

    public void voice(View view) {
        voice.startListening();
    }

    public void openDrawer(View view) {
        state.isOpenDrawer.set(true);
    }

    public void openListOfWallets(View view) {
        startActivity(new Intent(getBaseContext(), ListOfWalletsActivity.class));
    }

    public void editWallet(View view) {
        startActivity(new Intent(getBaseContext(), EditWalletActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeWallet();
        binding.setWallet(wallet);
    }

    public void logout(View view) {
        Credential.logout();
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
    }

    public void openStatistical(View view) {
        Toast.makeText(getApplicationContext(), R.string.error_features_not_developed_yet, Toast.LENGTH_SHORT).show();
    }

    public void openPayloan(View view) {
        Toast.makeText(getApplicationContext(), R.string.error_features_not_developed_yet, Toast.LENGTH_SHORT).show();
    }

    public void createTransaction(View view) {
        startActivity(new Intent(getBaseContext(), CreateBillActivity.class));
    }

    public static class State extends Observable {
        public final ObservableBoolean isOpenDrawer = new ObservableBoolean(false);
        public final ObservableInt isShowSpeechRecognizerBar = new ObservableInt(View.GONE);
        public final ObservableField<String> speechRecognizerString = new ObservableField<>("");
        public final ObservableFloat ratio = new ObservableFloat(0);

        @BindingAdapter("android:visibility")
        public static void setVisibility(View view, int visibility) {
            TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
            view.setVisibility(visibility);
        }

        public void setIsShowSpeechRecognizerBar(boolean value) {
            isShowSpeechRecognizerBar.set(value ? View.VISIBLE : View.GONE);
        }
    }
}