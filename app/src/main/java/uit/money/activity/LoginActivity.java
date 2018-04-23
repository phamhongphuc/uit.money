package uit.money.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.Profile;

import io.realm.Realm;
import model.User;
import uit.money.R;
import uit.money.facebook.Credential;

public class LoginActivity extends RealmActivity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializeListener();
    }

    private void InitializeListener() {
        Log.d(TAG, "Đã login");
        Credential.InitializeLogin(this::SaveUser);
    }

    private void SaveUser() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        User user = new User();
        user.setFbid(AccessToken.getCurrentAccessToken().getUserId());
        user.setName(Profile.getCurrentProfile().getName());

        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void Login(View view) {
        Credential.Login(this);
    }

    //region Facebook Login [onActivityResult]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Logout(View view) {
        Credential.Logout();
    }
    //endregion
}
