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
import voice.Recognizer;
import voice.Voice;
import voice.utils.InterfaceWalletActivity;

import static uit.money.utils.Timer.setTimeout;

public class WalletActivity extends RealmActivity implements InterfaceWalletActivity {
    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final int NONE = 0;
    public static final int CREATE = 1;
    public static final int EDIT = 2;
    public static final int LAYOUT = R.layout.activity_wallet;

    private Wallet wallet;
    private Voice voice;
    private State state = new State();
    private ActivityWalletBinding binding;
    private Recognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initializeRecognizer();
        initializeVoice();
        initializeWallet();
        initializeDataBinding();
    }

    private void initializeRecognizer() {
        recognizer = new Recognizer(this);
    }

    private void initializeVoice() {
        voice = new Voice(this);
        voice.checkPermission();
        voice.setListener(new Voice.Listener() {
            @Override
            public void onBeginningOfSpeech() {
                super.onBeginningOfSpeech();
                state.speechRecognizerString.set(getString(R.string.voice_start));
                state.setIsShowSpeechRecognizerBar(true);
            }

            @Override
            public void onRmsChanged(float db) {
                final float min = 2;
                final float max = 10;
                state.ratio.set(db < min ? 0 : db > max ? 1 : (db - min) / (max - min));
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

                recognizer.recognize(log);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                super.onPartialResults(partialResults);
                final ArrayList<String> matches = getMatches(partialResults);
                state.speechRecognizerString.set(matches.get(0));
            }
        });
    }

    private void initializeWallet() {
        wallet = Wallet.getCurrentWallet();
        if (wallet != null) {
            wallet.initialize();
        }
    }

    private void initializeDataBinding() {
        binding = DataBindingUtil.setContentView(this, LAYOUT);
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

    @Override
    public void openCreateBill(View view) {
        final Intent intent = new Intent(getBaseContext(), EditBillActivity.class);
        intent.putExtra(TYPE, CREATE);
        startActivity(intent);
    }

    @Override
    public void openListOfWallets(View view) {
        startActivity(new Intent(getBaseContext(), ListOfWalletsActivity.class));
    }

    @Override
    public Wallet getWallet() {
        return wallet;
    }

    public void openCreatePayment(View view) {
    }

    public void openCreateLoan(View view) {
    }

    public void openCreateTransfer(View view) {
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