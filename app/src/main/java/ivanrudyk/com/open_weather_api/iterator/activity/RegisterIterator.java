package ivanrudyk.com.open_weather_api.iterator.activity;

import android.graphics.Bitmap;

import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface RegisterIterator {

    void register(Users user, OnRegisterFinishedListener onRegisterFinishedListener,  String confPass, String city, Bitmap photoLoad);

    interface OnRegisterFinishedListener {

        void onUsernameError();

        void onPasswordError();

        void onSuccess(Users user, Bitmap photoLoad);

        void onLoginError();

        void onCityError();

        void onConfirmPasswordError(String s);
    }

}
