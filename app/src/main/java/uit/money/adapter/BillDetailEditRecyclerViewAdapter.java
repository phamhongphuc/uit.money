package uit.money.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import model.model.transaction.Bill;
import model.model.transaction.BillDetail;
import uit.money.R;
import uit.money.databinding.ItemBillDetailEditBinding;

public class BillDetailEditRecyclerViewAdapter extends RealmRecyclerViewAdapter<BillDetail, BillDetailEditRecyclerViewAdapter.ViewHolder> {
    public BillDetailEditRecyclerViewAdapter(Bill bill) {
        super(bill.getBillDetails(), true);
    }

    public static BillDetailEditRecyclerViewAdapter getInstance(Bill bill) {
        return new BillDetailEditRecyclerViewAdapter(bill);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBillDetailEditBinding viewDataBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_bill_detail_edit,
                parent, false
        );
        return new ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBillDetailEditBinding binding;

        ViewHolder(ItemBillDetailEditBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            this.binding = binding;
        }

        void bind(BillDetail billDetail) {
            binding.setBillDetail(billDetail);
            binding.setAction(new Action(billDetail));
            binding.executePendingBindings();
        }
    }

    public static class Action {
        private BillDetail billDetail;

        Action(BillDetail billDetail) {
            this.billDetail = billDetail;
        }

        public void delete(View view) {
            Realm.getDefaultInstance().executeTransaction(r -> {
//                billDetail.setBill(null);
                if (billDetail.isValid()) billDetail.deleteFromRealm();
            });
        }
    }
}