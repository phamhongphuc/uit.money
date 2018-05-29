package uit.money.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uit.money.R;
import uit.money.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private int icon;
    private int title;
    private int content;

    public LoginFragment() {

    }

    public static LoginFragment getFragment(int icon, int title, int content) {
        final LoginFragment loginFragment = new LoginFragment();
        loginFragment.icon = icon;
        loginFragment.title = title;
        loginFragment.content = content;
        return loginFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLoginBinding binding;
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login,
                container, false
        );
        try {
            binding.setBackground(getResources().getDrawable(icon, null));
            binding.setContent(getString(content));
            binding.setTitle(getString(title));
        } catch (Exception ignored) {
        }
        return binding.getRoot();
    }
}
