package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.facebook.CallbackManager;

import org.parceler.Parcels;

import java.util.ArrayList;

import model.model.Wallet;
import uit.money.R;
import uit.money.adapter.FragmentAdapter;
import uit.money.databinding.ActivityLoginBinding;
import uit.money.facebook.Credential;
import uit.money.fragment.LoginFragment;

public class LoginActivity extends RealmActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeListener();
        initializeBinding();
    }

    private void initializeListener() {
        Credential.initializeLogin(() -> {
            // When already logged in facebook and connected or not connected to Realm Object Server
            // Call after login() is called
            Intent intent = new Intent(getBaseContext(), WalletActivity.class);
            intent.putExtra("wallet", Parcels.wrap(Wallet.getCurrentWallet()));
            startActivity(intent);
            finish();
        });
    }

    private void initializeBinding() {
        ActivityLoginBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setAdapter(new FragmentAdapter(
                getSupportFragmentManager(),
                new ArrayList<Fragment>() {{
                    add(new LoginFragment(R.drawable.login_1_icon, R.string.login_1_title, R.string.login_1_content));
                    add(new LoginFragment(R.drawable.login_2_icon, R.string.login_2_title, R.string.login_2_content));
                    add(new LoginFragment(R.drawable.login_3_icon, R.string.login_3_title, R.string.login_3_content));
                }}
        ));
    }

    public void login(View view) {
        Credential.login(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
