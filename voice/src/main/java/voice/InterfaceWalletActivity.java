package voice;

import android.view.View;

import model.model.Wallet;

public interface InterfaceWalletActivity {
    void openCreateBill(View view);

    void openListOfWallets(View view);

    Wallet getWallet();
}
