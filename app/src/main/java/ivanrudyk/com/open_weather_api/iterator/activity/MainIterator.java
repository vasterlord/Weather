package ivanrudyk.com.open_weather_api.iterator.activity;

import android.content.Context;

import com.facebook.Profile;

import ivanrudyk.com.open_weather_api.model.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface MainIterator {

    void login(String userLogin, String userPassword, OnMainFinishedListener onMainFinishedListener);

    void loginFasebook(Profile profile, OnMainFinishedListener onMainFinishedListener, Context context);


    interface OnMainFinishedListener {

        void onLoginError();

        void onPasswordError();

        void onSuccess(String userLogin, String userPassword);

        void setUserFasebook(Users users);

    }

}
