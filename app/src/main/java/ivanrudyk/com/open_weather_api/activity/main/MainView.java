package ivanrudyk.com.open_weather_api.activity.main;

import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface MainView {
    void setLoginError(String userName);

    void hideProgress();

    void setPasswordError();

    void showProgress();

    void toastShow(String userName);

    void setUser(Users activeUser);
}
