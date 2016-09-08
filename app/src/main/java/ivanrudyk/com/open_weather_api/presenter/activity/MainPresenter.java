package ivanrudyk.com.open_weather_api.presenter.activity;

import android.content.Context;

import com.facebook.Profile;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface MainPresenter {
    void retriveUserFirebase(String login, String password, Context context);

    void setProgressLogin(String s);

    void loginFacebook(Profile profile, String uid, Context applicationContext);
}
