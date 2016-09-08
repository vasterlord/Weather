package ivanrudyk.com.open_weather_api.presenter.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.iterator.activity.RegisterIterator;
import ivanrudyk.com.open_weather_api.iterator.activity.RegisterIteratorInplement;
import ivanrudyk.com.open_weather_api.model.ModelUser;
import ivanrudyk.com.open_weather_api.ui.activity.RegisterView;

/**
 * Created by Ivan on 03.08.2016.
 */
public class RegisterPresenterImplement implements RegisterPresenter, RegisterIterator.OnRegisterFinishedListener {


    private RegisterView registerView;
    private RegisterIterator registerInteractor;
    ModelUser user = new ModelUser();
    Bitmap photoUser;
    private String password;
    private String userUid;

    FirebaseHelper firebaseHelper = new FirebaseHelper();

    public RegisterPresenterImplement(RegisterView registerView) {
        this.registerView = registerView;
        this.registerInteractor = new RegisterIteratorInplement();
    }


    @Override
    public void addUser(ModelUser userAdd, String[] passwordsAndCity, Bitmap photoLoad, String userUid) {
        this.user = userAdd;
        this.photoUser = photoLoad;
        this.password = passwordsAndCity[0];
        this.userUid = userUid;
        firebaseHelper.addUser(user, userUid);
        firebaseHelper.loadPhotoStorage(userUid, photoUser);
        RegisterProgress registerProgress = new RegisterProgress();
        registerProgress.execute();
    }

    @Override
    public void createAcount(ModelUser userAdd, String[] passwordsAndCity) {
        registerInteractor.register(userAdd, this, passwordsAndCity);
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
    public void onSuccess() {
        if (registerView != null) {
            registerView.createUserAcount();
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

   private class RegisterProgress extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            registerView.finishActivity();
            registerView.hideProgress();
        }
    }

}
