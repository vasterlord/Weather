package ivanrudyk.com.open_weather_api.iterator.activity;

import android.graphics.Bitmap;

import ivanrudyk.com.open_weather_api.model.ModelUser;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface RegisterIterator {

    void register(ModelUser user, OnRegisterFinishedListener onRegisterFinishedListener, final String[] paswordsAndCity);

    interface OnRegisterFinishedListener {

        void onUsernameError();

        void onPasswordError();

        void onSuccess();

        void onLoginError();

        void onCityError();

        void onConfirmPasswordError(String s);
    }

}
