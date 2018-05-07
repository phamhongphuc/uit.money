package uit.money.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import uit.money.R;
import uit.money.databinding.FragmentLoginBinding;

@SuppressLint("ValidFragment")
public class LoginFragment extends Fragment {
    private final int icon;
    private final int title;
    private final int content;

    public LoginFragment(int icon, int title, int content) {
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLoginBinding binding;
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_login,
                container, false
        );
        binding.setBackground(Objects.requireNonNull(getContext()).getDrawable(icon));
        binding.setContent(getString(content));
        binding.setTitle(getString(title));
        return binding.getRoot();
    }
}
