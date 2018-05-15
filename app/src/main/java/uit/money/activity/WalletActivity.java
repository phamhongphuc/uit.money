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

import java.util.ArrayList;
import java.util.Observable;

import model.model.User;
import model.model.Wallet;
import uit.money.R;
import uit.money.adapter.TransactionRecyclerViewAdapter;
import uit.money.databinding.ActivityWalletBinding;
import uit.money.facebook.Credential;
import voice.Voice;
import voice.recognizer.RecognizerBill;

import static uit.money.utils.Timer.setTimeout;

public class WalletActivity extends RealmActivity {
    private static final String TAG = "WalletActivity";
    private Wallet wallet;
    private Voice voice;
    private State state = new State();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        initializeWallet();
        initializeUser();
        initializeVoice();
        initializeDataBinding();
    }

    private void initializeUser() {
        user = User.getCurrentUser();
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
            public void onBeginningOfSpeech() {
                super.onBeginningOfSpeech();
                state.speechRecognizerString.set(getString(R.string.voice_start));
                state.setShowSpeechRecognizerBar(true);
            }

            @Override
            public void onError(String error) {
                state.speechRecognizerString.set(error);
                state.ratio.set(0);
                setTimeout(() -> state.setShowSpeechRecognizerBar(false), 1200);
            }

            @Override
            public void onResults(Bundle results) {
                super.onResults(results);
                final ArrayList<String> log = getMatches(results);
                state.speechRecognizerString.set(log.get(0));
                state.ratio.set(0);
                setTimeout(() -> state.setShowSpeechRecognizerBar(false), 1200);

                RecognizerBill recognizerBill = new RecognizerBill(wallet, log.get(0));

                if (recognizerBill.isValid()) {
                    recognizerBill.copyToRealmOrUpdate();
                }
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
        final ActivityWalletBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        binding.setWallet(wallet);
        binding.setUser(user);
        binding.setState(state);
    }

    public void voice(View view) {
        voice.startListening();
    }

    public void openDrawer(View view) {
        state.isOpenDrawer.set(true);
    }

    public void openWallets(View view) {
    }

    public void logout(View view) {
        Credential.logout();
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
    }

    public static class State extends Observable {
        public final ObservableBoolean isOpenDrawer = new ObservableBoolean(false);
        public final ObservableInt showSpeechRecognizerBar = new ObservableInt(View.GONE);
        public final ObservableField<String> speechRecognizerString = new ObservableField<>("");
        public final ObservableFloat ratio = new ObservableFloat(0);

        public void setShowSpeechRecognizerBar(boolean value) {
            showSpeechRecognizerBar.set(value ? View.VISIBLE : View.GONE);
        }

        @BindingAdapter("android:visibility")
        public static void setVisibility(View view, int visibility) {
            TransitionManager.beginDelayedTransition((ViewGroup) view.getParent());
            view.setVisibility(visibility);
        }

        public TransactionRecyclerViewAdapter getTransactionAdapter(Wallet wallet) {
            return new TransactionRecyclerViewAdapter(wallet);
        }
    }
}