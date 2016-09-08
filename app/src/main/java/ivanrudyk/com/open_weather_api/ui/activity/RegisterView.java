package ivanrudyk.com.open_weather_api.ui.activity;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface RegisterView {

    void showToast(String s);

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void setLoginError();

    void setCityError();

    void setConfirmPasswordError(String s);

    void navigateToMain();

    void closeWiew();

    void createUserAcount();

    void finishActivity();
}
