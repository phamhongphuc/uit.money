package uit.money.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.model.transaction.Payment.PaymentType;
import uit.money.BR;
import uit.money.R;

import static model.Const.IN;
import static model.Const.OUT;
import static model.Const.getResource;
import static model.Utils.getMoneyColor;
import static model.model.transaction.Payment.INITIALIZE;
import static model.model.transaction.Payment.PAYMENT_TYPE;
import static model.model.transaction.Payment.UNKNOWN;

public class PaymentTypeRecyclerViewAdapter extends RecyclerView.Adapter<PaymentTypeRecyclerViewAdapter.ViewHolder> {
    public static final List<State> states;

    static {
        states = new ArrayList<>();
        for (Map.Entry<Integer, PaymentType> each : PAYMENT_TYPE.entrySet()) {
            if (each.getKey() != INITIALIZE && each.getKey() != UNKNOWN) {
                states.add(new State(each.getKey(), each.getValue()));
            }
        }
    }

    public static void setKind(int kind) {
        for (State state : states) {
            state.setTextColor(state.kind == kind);
        }
    }

    public static PaymentTypeRecyclerViewAdapter getInstance() {
        return new PaymentTypeRecyclerViewAdapter();
    }

    private PaymentTypeRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(states.get(position));
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        ViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());

            final View root = binding.getRoot();

            FlexboxLayoutManager.LayoutParams layoutParams =
                    (FlexboxLayoutManager.LayoutParams) root.getLayoutParams();
            layoutParams.setFlexBasisPercent(1f / 3);
            root.setLayoutParams(layoutParams);

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

        void bind(State state) {
            binding.setVariable(BR.state, state);
            binding.executePendingBindings();
        }
    }

    public static class State {
        public final int kind;
        public final PaymentType paymentType;
        public ObservableInt textColor = new ObservableInt(getResource().getColor(R.color._text_color, null));

        State(Integer kind, PaymentType paymentType) {
            this.kind = kind;
            this.paymentType = paymentType;
        }

        public void select(View view) {
            for (State state : states) state.setTextColor(false);
            setTextColor(true);
        }

        private void setTextColor(Boolean value) {
            textColor.set(
                    getResource().getColor(
                            value ? (kind > 0 ? R.color.in_color : R.color.out_color) : R.color._text_color,
                            null
                    )
            );
        }

        public int getLineColor() {
            return getMoneyColor(kind > 0 ? IN : OUT);
        }
    }
}