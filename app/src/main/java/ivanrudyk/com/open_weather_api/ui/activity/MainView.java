package ivanrudyk.com.open_weather_api.ui.activity;


import ivanrudyk.com.open_weather_api.model.ModelUser;

public interface MainView {
    void setLoginError(String userName);

    void hideProgress();

    void setPasswordError();

    void showProgress();

    void toastShow(String userName);

    void setUser(ModelUser activeUser);

    void setViseibleLogin();

    void setDialogClosed();

    void loginUserFirebase(String userLogin, String userPassword);

}
