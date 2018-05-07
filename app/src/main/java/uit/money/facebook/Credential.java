package uit.money.facebook;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import java.util.Collections;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import model.model.User;

import static uit.money.app.Constants.AUTH_URL;
import static uit.money.app.Constants.BASE_URL;

public class Credential {
    private static final String TAG = "Credential";
    private static AccessTokenTracker accessTokenTracker = null;
    private static boolean isOnline = false;

    public static void initializeLogin(final Credential.Callback callback) {
        if (accessTokenTracker != null) return;

        checkAndLogin(AccessToken.getCurrentAccessToken(), callback);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken accessToken) {
                checkAndLogin(accessToken, callback);
            }
        };
    }

    private static void checkAndLogin(@Nullable AccessToken accessToken, final Credential.Callback callback) {
        if (accessToken == null || accessToken.isExpired()) return;

        loginToRealm(accessToken, () -> {
            setCurrentUser(accessToken);
            callback.call();
        });
    }

    private static void loginToRealm(AccessToken accessToken, @NonNull Callback callback) {
        SyncCredentials credentials = SyncCredentials.nickname(accessToken.getUserId(), false);
        SyncUser.logInAsync(credentials, AUTH_URL, new SyncUser.Callback<SyncUser>() {
            @Override
            public void onSuccess(@NonNull SyncUser user) {
                String uri = BASE_URL + "/~/" + accessToken.getUserId();
                SyncConfiguration configuration = new SyncConfiguration
                        .Builder(user, uri)
                        .partialRealm()
                        .build();
                Realm.setDefaultConfiguration(configuration);
                isOnline = true;
                callback.call();
            }

            @Override
            public void onError(@NonNull ObjectServerError error) {
                isOnline = false;
                callback.call();
            }
        });
    }

    private static void setCurrentUser(AccessToken accessToken) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        User user = new User();
        user.setFbid(accessToken.getUserId());
        user.setName(Profile.getCurrentProfile().getName());
        User.setCurrentUser(realm.copyToRealmOrUpdate(user));

        realm.commitTransaction();
    }

    public static void login(AppCompatActivity activity) {
        LoginManager
                .getInstance()
                .logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
    }

    public static void logout() {
        LoginManager.getInstance().logOut();
    }

    public static boolean isOnline() {
        return isOnline;
    }

    public interface Callback {
        void call();
    }
}
