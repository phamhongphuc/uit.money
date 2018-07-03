package uit.money.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmRecyclerViewAdapter;
import model.model.User;
import model.model.Wallet;
import uit.money.R;
import uit.money.databinding.ItemWalletBinding;

public class WalletRecyclerViewAdapter extends RealmRecyclerViewAdapter<Wallet, WalletRecyclerViewAdapter.ViewHolder> {
    public WalletRecyclerViewAdapter(User user) {
        super(user.getWallets(), true);
    }

    public static WalletRecyclerViewAdapter getInstance(User user) {
        return new WalletRecyclerViewAdapter(user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWalletBinding viewDataBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_wallet,
                parent, false
        );
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemWalletBinding binding;

        ViewHolder(ItemWalletBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            this.binding = binding;
        }

        void bind(Wallet wallet) {
            wallet.initialize();
            binding.setWallet(wallet);
            binding.setAction(new Action(wallet));
            binding.executePendingBindings();
        }
    }

    public static class Action {
        private Wallet wallet;

        Action(Wallet wallet) {
            this.wallet = wallet;
        }

        public void select(View view) {
            Wallet.setCurrentWallet(wallet);
            Activity activity = (Activity) view.getContext();
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }
}