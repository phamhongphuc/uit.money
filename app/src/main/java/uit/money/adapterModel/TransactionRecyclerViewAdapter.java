package uit.money.adapterModel;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import io.realm.RealmResults;
import model.model.Wallet;
import model.model.transaction.Bill;
import model.model.transaction.Loan;
import model.model.transaction.TransactionModel;
import uit.money.BR;
import uit.money.R;
import uit.money.activity.LoginActivity;
import uit.money.adapterModel.separator.DateSeparator;
import uit.money.adapterModel.separator.EndSeparator;

import static model.Const.BILL;
import static model.Const.DATE_SEPARATOR;
import static model.Const.END_SEPARATOR;
import static model.Const.LOAN;
import static model.Const.START_SEPARATOR;

public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder> {
    private RealmResults<Loan> loans;
    private RealmResults<Bill> bills;
    private List<TransactionModel> transactions;

    public TransactionRecyclerViewAdapter(Wallet wallet) {
        loans = wallet.getLoans();
        bills = wallet.getBills();

        loans.addChangeListener(l -> onChange());
        bills.addChangeListener(l -> onChange());

        indexing();
    }

    private void onChange() {
        notifyDataSetChanged();
        indexing();
    }

    private void indexing() {
        transactions = new ArrayList<>();
        transactions.addAll(loans);
        transactions.addAll(bills);

        Collections.sort(transactions, (a, b) -> a.getTime().compareTo(b.getTime()));

        ListIterator<TransactionModel> i = transactions.listIterator();
        Date time = new Date(0);
        while (i.hasNext()) {
            TransactionModel each = i.next();

            final boolean equals = DateUtils.isSameDay(time, each.getTime());
            time = each.getTime();

            if (!equals) {
                i.previous();
                i.add(new DateSeparator(time));
                i.next();
            }
        }

        transactions.add(new EndSeparator());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case START_SEPARATOR:
                return ViewHolder.getViewHolder(parent, R.layout.item_start_separate, BR.startSeparator);
            case END_SEPARATOR:
                return ViewHolder.getViewHolder(parent, R.layout.item_end_separate, BR.endSeparate);
            case DATE_SEPARATOR:
                return ViewHolder.getViewHolder(parent, R.layout.item_date_separate, BR.dateSeparator);
            case LOAN:
                return ViewHolder.getViewHolder(parent, R.layout.item_loan, BR.loan);
            case BILL:
                return ViewHolder.getViewHolder(parent, R.layout.item_bill, BR.bill);
            default:
                throw new ExceptionInInitializerError("viewType not match");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(transactions.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return transactions.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;
        private int variable;

        ViewHolder(ViewDataBinding binding, int variable) {
            super(binding.getRoot());
            binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            this.variable = variable;
            this.binding = binding;
        }

        static ViewHolder getViewHolder(ViewGroup parent, int layout, int variable) {
            return new ViewHolder(DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    layout,
                    parent, false
            ), variable);
        }

        void bind(TransactionModel model) {
            model.initialize();
            binding.setVariable(variable, model);
            binding.setVariable(BR.action, new Action());
            binding.executePendingBindings();
        }
    }

    public static class Action {
        Action() {
        }

        public void click(View view) {
            view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
        }
    }
}