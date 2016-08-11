package ivanrudyk.com.open_weather_api.presenter.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.iterator.activity.RegisterIterator;
import ivanrudyk.com.open_weather_api.iterator.activity.RegisterIteratorInplement;
import ivanrudyk.com.open_weather_api.helper.FirebaseHelper;
import ivanrudyk.com.open_weather_api.model_user.Users;
import ivanrudyk.com.open_weather_api.ui.activity.RegisterView;

/**
 * Created by Ivan on 03.08.2016.
 */
public class RegisterPresenterImplement implements RegisterPresenter, RegisterIterator.OnRegisterFinishedListener {

    private RegisterView registerView;
    private RegisterIterator registerInteractor;
    Users user = new Users();
    ArrayList<String> locationStart = new ArrayList();
    FirebaseHelper helper = new FirebaseHelper();


    public RegisterPresenterImplement(RegisterView registerView) {
        this.registerView = registerView;
        this.registerInteractor = new RegisterIteratorInplement();
    }


    @Override
    public void addUser(Users userAdd, String confPass, String city, Bitmap photoLoad) {
        registerView.showProgress();
        this.user = userAdd;
        registerInteractor.register(user, this, confPass, city,  photoLoad);
    }

    @Override
    public void onUsernameError() {
        if (registerView != null) {
            registerView.setUsernameError();
            registerView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (registerView != null) {
            registerView.setPasswordError();
            registerView.hideProgress();
        }
    }

    @Override
    public void onSuccess(Users user, Bitmap photoLoad) {
        if (registerView != null) {
            helper.addUser(user);
            user.setPhoto(photoLoad);
            if (photoLoad != null) {
                helper.loadPhotoStorage(user.getUserName(), photoLoad);
            }
            RegisterProgress registerProgress = new RegisterProgress();
            registerProgress.execute();
        }

    }

    @Override
    public void onLoginError() {
        if (registerView != null) {
            registerView.setLoginError();
            registerView.hideProgress();
        }
    }

    @Override
    public void onCityError() {
        if (registerView != null) {
            registerView.setCityError();
            registerView.hideProgress();
        }
    }

    @Override
    public void onConfirmPasswordError(String s) {
        if (registerView != null) {
            registerView.setConfirmPasswordError(s);
            registerView.hideProgress();
        }
    }




    class RegisterProgress extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            registerView.showToast("User "+user.getUserName()+" save...");
            registerView.showProgress();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            registerView.hideProgress();
        }
    }


}
