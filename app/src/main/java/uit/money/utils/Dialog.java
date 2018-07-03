package uit.money.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import uit.money.R;

public class Dialog {
    public static void OpenConfirm(Context context, String message, Callback yes) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_yes, (dialog, which) -> yes.call())
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> dialog.cancel())
                .show();
    }

    public interface Callback {
        void call();
    }
}
