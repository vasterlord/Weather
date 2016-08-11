package ivanrudyk.com.open_weather_api.presenter.activity;

import android.graphics.Bitmap;

import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface RegisterPresenter {
    void addUser(Users userAdd, String confPass, String city, Bitmap photoLoad);
}
