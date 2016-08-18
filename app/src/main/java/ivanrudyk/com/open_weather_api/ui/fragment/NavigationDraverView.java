package ivanrudyk.com.open_weather_api.ui.fragment;

import ivanrudyk.com.open_weather_api.model.Users;

/**
 * Created by Ivan on 10.08.2016.
 */
public interface NavigationDraverView {
    void setUpFragment();

    void setLocationAddError(String s);

    void setDialogClosed();

    void showProgress();

    void hideProgress();

    void setUser(Users user);
}
