package uit.money.facebook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;

import java.util.Collections;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static uit.money.app.Constants.AUTH_URL;
import static uit.money.app.Constants.BASE_URL;

public class Credential {
    private static AccessTokenTracker accessTokenTracker = null;

    public static void InitializeLogin(final Credential.Callback callback) {
        if (accessTokenTracker == null) {
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    SyncCredentials credentials = SyncCredentials.nickname(currentAccessToken.getUserId(), false);
                    SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
                        @Override
                        public void onSuccess(@NonNull SyncUser user) {
                            SyncConfiguration configuration = new SyncConfiguration
                                    .Builder(user, BASE_URL + "/~/" + currentAccessToken.getUserId())
                                    .partialRealm().build();
                            Realm.setDefaultConfiguration(configuration);
                            callback.Response();
                        }

                        @Override
                        public void onError(@NonNull ObjectServerError error) {
                            callback.Response();
                        }
                    });
                }
            };
        }
    }

    public static void Login(AppCompatActivity activity) {
        LoginManager
                .getInstance()
                .logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
    }

    public static void Logout() {
        LoginManager.getInstance().logOut();
    }

    public interface Callback {
        void Response();
    }
}
