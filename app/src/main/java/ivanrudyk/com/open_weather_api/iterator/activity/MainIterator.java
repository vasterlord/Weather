package ivanrudyk.com.open_weather_api.iterator.activity;

/**
 * Created by Ivan on 03.08.2016.
 */
public interface MainIterator {

    void login(String userLogin, String userPassword, OnMainFinishedListener onMainFinishedListener);


    interface OnMainFinishedListener {

        void onLoginError();

        void onPasswordError();

        void onSuccess(String userLogin, String userPassword);

    }

}
