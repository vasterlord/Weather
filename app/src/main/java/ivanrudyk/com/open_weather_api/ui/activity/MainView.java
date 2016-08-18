package ivanrudyk.com.open_weather_api.ui.activity;

import ivanrudyk.com.open_weather_api.model.Users;


public interface MainView {
    void setLoginError(String userName);

    void hideProgress();

    void setPasswordError();

    void showProgress();

    void toastShow(String userName);

    void setUser(Users activeUser);

    void setViseibleLogin();

    void setDialogClosed();

}
