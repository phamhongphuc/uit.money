package uit.money.app;

import android.app.Application;

import com.facebook.FacebookSdk;

import io.realm.Realm;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
//        Realm.deleteRealm(Realm.getDefaultConfiguration());
        Realm.init(this);
    }
}
