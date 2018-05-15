package uit.money.adapterModel;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.realm.RealmRecyclerViewAdapter;
import uit.money.BR;
import uit.money.R;
import model.model.transaction.Bill;
import model.model.transaction.BillDetail;

public class BillDetailRecyclerViewAdapter extends RealmRecyclerViewAdapter<BillDetail, BillDetailRecyclerViewAdapter.ViewHolder> {
    public BillDetailRecyclerViewAdapter(Bill bill) {
        super(bill.getBillDetails(), true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_bill_detail,
                parent, false
        );
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillDetail detail = getItem(position);
        holder.bind(detail);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            binding.getRoot().setOnClickListener(v -> {
                ((FrameLayout) v.getParent().getParent().getParent()).getChildAt(0).callOnClick();
            });
            this.binding = binding;
        }

        void bind(BillDetail detail) {
            binding.setVariable(BR.billDetail, detail);
            binding.executePendingBindings();
        }
    }
}
