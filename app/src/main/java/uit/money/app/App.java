package uit.money.app;

import android.app.Application;

import com.facebook.FacebookSdk;

import io.realm.Realm;
import model.Const;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Const.setResource(getResources());
        FacebookSdk.sdkInitialize(getApplicationContext());
        Realm.init(this);
    }
}
