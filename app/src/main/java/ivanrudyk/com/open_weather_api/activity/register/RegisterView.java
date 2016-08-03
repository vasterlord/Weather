package ivanrudyk.com.open_weather_api.activity.register;

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
}
