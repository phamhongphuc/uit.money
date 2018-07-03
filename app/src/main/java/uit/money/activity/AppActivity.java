package uit.money.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;

import static uit.money.utils.Timer.setTimeout;

@SuppressLint("Registered")
public class AppActivity extends AppCompatActivity {
    protected final Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void delayStartActivity(Intent intent) {
        setTimeout(() -> startActivity(intent), 300);
    }
}
