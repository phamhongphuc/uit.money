package uit.money.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.facebook.CallbackManager;

import java.util.ArrayList;

import uit.money.R;
import uit.money.databinding.ActivityLoginBinding;
import uit.money.facebook.Credential;
import uit.money.fragment.FragmentAdapter;

import static uit.money.fragment.LoginFragment.getFragment;

public class LoginActivity extends RealmActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeListener();
        initializeBinding();
    }

    private void initializeListener() {
        Credential.initializeLogin(() -> {
            startActivity(new Intent(getBaseContext(), WalletActivity.class));
            finish();
        });
    }

    private void initializeBinding() {
        ActivityLoginBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setAdapter(new FragmentAdapter(
                getSupportFragmentManager(),
                new ArrayList<Fragment>() {{
                    add(getFragment(R.drawable.login_1_icon, R.string.login_1_title, R.string.login_1_content));
                    add(getFragment(R.drawable.login_2_icon, R.string.login_2_title, R.string.login_2_content));
                    add(getFragment(R.drawable.login_3_icon, R.string.login_3_title, R.string.login_3_content));
                }}
        ));
    }

    public void login(View view) {
        Credential.login(this);
    }

    // Don't care about this method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
