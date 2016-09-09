package ivanrudyk.com.open_weather_api.presenter.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.helpers.RealmDbHelper;
import ivanrudyk.com.open_weather_api.iterator.activity.MainIterator;
import ivanrudyk.com.open_weather_api.iterator.activity.MainIteratorImlement;
import ivanrudyk.com.open_weather_api.iterator.activity.WeatherIterator;
import ivanrudyk.com.open_weather_api.iterator.activity.WeatherIteratorImplement;
import ivanrudyk.com.open_weather_api.model.ModelLocation;
import ivanrudyk.com.open_weather_api.model.ModelUser;
import ivanrudyk.com.open_weather_api.ui.activity.MainView;

/**
 * Created by Ivan on 03.08.2016.
 */
public class MainPresenterImplement implements MainPresenter, MainIterator.OnMainFinishedListener, WeatherIterator.OnWeatherFinishedListener {
    private MainView mainView;
    private MainIterator iterator;
    private WeatherIterator weatherIterator;
    private RealmDbHelper dbHelper = new RealmDbHelper();
    private Context context;
    private String uid;
    private Profile profile;
    private Bitmap photoUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean t;

    ModelUser activeUser = new ModelUser();
    ModelLocation modelLocation = new ModelLocation();
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    public MainPresenterImplement(MainView mainView) {
        this.mainView = mainView;
        this.iterator = new MainIteratorImlement();
        this.weatherIterator = new WeatherIteratorImplement();
    }

    @Override
    public void retriveUserFirebase(String userLogin, String userPassword, Context context) {
        this.context = context;
        mainView.showProgress();
        iterator.login(userLogin, userPassword, this);
    }


    @Override
    public void setProgressLogin(String uid) {
        this.uid = uid;
        firebaseHelper.retrivDataUser(uid);
    }

    @Override
    public void loginFacebook(Profile profile, final String uid, Context context) {
        this.profile = profile;
        this.uid = uid;
        this.context = context;
        firebaseHelper.retrivDataUser(uid);
        LoginFacebookProgress loginFacebookProgress = new LoginFacebookProgress();
        loginFacebookProgress.execute();
    }

    private boolean listenerFacebook() {

        if (FirebaseHelper.modelUser.getUserName() != null) {
            t = true;
        }
        else if (FirebaseHelper.modelUser.getUserName() != null) {
            if (FirebaseHelper.modelUser.getUserName().length() > 0) {
                t = true;
            }
        }
        else if (FirebaseHelper.modelUser != null)
        {
            t = true;
        }
        else if(FirebaseHelper.photoDownload !=null){
            t = true;
        }
        else{
        t = false;
        }

        return t;
    }

    @Override
    public void onLoginError() {
        if (mainView != null) {
            mainView.setLoginError("login is empty");
            mainView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (mainView != null) {
            mainView.setPasswordError();
            mainView.hideProgress();
        }
    }

    @Override
    public void onSuccess(String userLogin, String userPassword) {
        mainView.loginUserFirebase(userLogin, userPassword);
    }

    private class LoginFacebookProgress extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (profile == null);
            Log.e("TESTING", "88888888888");
            do {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (profile.getName().length() < 1);

            Log.e("TESTING", "UID = " + uid);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listenerFacebook()) {
                loginUserFacebook();
                Picasso.with(context)
                        .load(profile.getProfilePictureUri(150, 150))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                photoUser = bitmap;
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
            Log.e("TESTING", "LOG");
            LoginFacebookProgress2 loginFacebookProgress2 = new LoginFacebookProgress2();
            loginFacebookProgress2.execute();
        }
    }

    private class LoginFacebookProgress2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (listenerFacebook()) {
                do {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (photoUser == null);
                firebaseHelper.loadPhotoStorage(uid, photoUser);
                Log.e("TESTING", "000000000000000");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                firebaseHelper.retrivDataUser(uid);
                firebaseHelper.downloadPhotoStorage(uid);
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (FirebaseHelper.photoDownload == null);
                Log.e("TESTING", "1111111111");
            } else {
                firebaseHelper.retrivDataUser(uid);
                firebaseHelper.downloadPhotoStorage(uid);
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (FirebaseHelper.photoDownload == null && FirebaseHelper.modelUser == null);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("TESTING", "LOG");
            activeUser = FirebaseHelper.modelUser;
            activeUser.setPhoto(FirebaseHelper.photoDownload);
            if (dbHelper.retriveUserFromRealm(context) != null)
                dbHelper.deleteUserFromRealm(context);
            dbHelper.saveUserToRealm(activeUser, context);
            mainView.setDialogClosed();
            mainView.setUser(activeUser);
        }
    }

    public void loginUserFacebook() {
        Log.e("TESTING", "LOGIIINNNN");
        ModelUser actUser = new ModelUser();
        actUser.setUserName(profile.getName());
        actUser.setEmailAdress("");
        ArrayList<String> mLoc = new ArrayList<>();
        mLoc.add("");
        modelLocation.setLocation(mLoc);
        actUser.setLocation(modelLocation);
        firebaseHelper.addUser(actUser, uid);
    }

}
