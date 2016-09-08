package ivanrudyk.com.open_weather_api.presenter.activity;

import android.graphics.Bitmap;

import ivanrudyk.com.open_weather_api.model.ModelUser;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface RegisterPresenter {

    void addUser(ModelUser userAdd, String[] passwordsAndCity, Bitmap photoLoad, String userUid);

    void createAcount(ModelUser userAdd, String[] passwordsAndCity);
}
