package uit.money.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

@SuppressLint("Registered")
public class RealmActivity extends AppCompatActivity {
    protected final Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
