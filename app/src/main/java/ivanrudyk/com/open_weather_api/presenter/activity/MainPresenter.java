package ivanrudyk.com.open_weather_api.presenter.activity;

import android.content.Context;

import com.facebook.Profile;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface MainPresenter {
    void retriveUserFirebase(String login, String password, Context context);

    void loginFacebook(Profile profile, Context context);

    void getForecast(String city, double v, double v1, String nowURL);
}
