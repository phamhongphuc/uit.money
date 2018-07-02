package uit.money.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uit.money.BR;
import uit.money.R;

public class PaymentTypeRecyclerViewAdapter extends RecyclerView.Adapter<PaymentTypeRecyclerViewAdapter.ViewHolder> {
    private List<PaymentType> paymentTypes;

    public static PaymentTypeRecyclerViewAdapter getInstance() {
        return new PaymentTypeRecyclerViewAdapter();
    }

    private PaymentTypeRecyclerViewAdapter() {
        indexing();
    }

//    private void onChange() {
//        notifyDataSetChanged();
//        indexing();
//    }

    private void indexing() {
        paymentTypes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(paymentTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return paymentTypes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            this.binding = binding;
        }

        @NonNull
        static ViewHolder getViewHolder(ViewGroup parent) {
            return new ViewHolder(DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_payment_type,
                    parent, false
            ));
        }

        void bind(PaymentType model) {
            binding.setVariable(BR.state, new State(model));
            binding.executePendingBindings();
        }
    }

    public static class State {
        private PaymentType paymentType;

        State(PaymentType paymentType) {
            this.paymentType = paymentType;
        }

        public void select(View view) {
//            final Context context = view.getContext();
        }
    }

    interface PaymentType {

    }
}