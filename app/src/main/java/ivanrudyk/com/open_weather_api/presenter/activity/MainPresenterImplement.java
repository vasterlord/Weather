package ivanrudyk.com.open_weather_api.presenter.activity;

import android.content.Context;
import android.os.AsyncTask;

import com.facebook.Profile;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.helpers.RealmDbHelper;
import ivanrudyk.com.open_weather_api.iterator.activity.MainIterator;
import ivanrudyk.com.open_weather_api.iterator.activity.MainIteratorImlement;
import ivanrudyk.com.open_weather_api.model.Users;
import ivanrudyk.com.open_weather_api.ui.activity.MainView;

/**
 * Created by Ivan on 03.08.2016.
 */
public class MainPresenterImplement implements MainPresenter, MainIterator.OnMainFinishedListener {
    private MainView mainView;
    private MainIterator iterator;
    private RealmDbHelper dbHelper = new RealmDbHelper();
    private Context context;
    private static final String BASE_CURRENT_WEATHER_URL_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPId=%s";
    private static final String BASE_CURRENT_WEATHER_URL_COORD = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&APPId=%s";
    private static final String BASE_DAILY_FORECAST_URL_COORD = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&lat=%s&lon=%s&units=metric";
    private static final String BASE_HOURLY_FORECAST_URL_COORD = "http://api.openweathermap.org/data/2.5/forecast/hourly?mode=json&lat=%s&lon=%s&units=metric";
    private static final String BASE_DAILY_FORECAST_URL_CITY = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&q=%s&units=metric";
    private static final String BASE_HOURLY_FORECAST_URL_CITY = "http://api.openweathermap.org/data/2.5/forecast/hourly?mode=json&q=%s&units=metric";
    String[] baseUrlCity = new String[]{BASE_CURRENT_WEATHER_URL_CITY, BASE_DAILY_FORECAST_URL_CITY, BASE_HOURLY_FORECAST_URL_CITY};
    String[] baseUrlCoord = new String[]{BASE_CURRENT_WEATHER_URL_COORD, BASE_DAILY_FORECAST_URL_COORD, BASE_HOURLY_FORECAST_URL_COORD};
    public String nowURL = BASE_CURRENT_WEATHER_URL_COORD;

    Users activeUser = new Users();
    public static Users userActive = new Users("vy");


    FirebaseHelper firebaseHelper = new FirebaseHelper();

    ArrayList<Users> retrivUserArray = new ArrayList<>();

    public MainPresenterImplement(MainView mainView) {
        this.mainView = mainView;
        this.iterator = new MainIteratorImlement();
    }


    @Override
    public void retriveUserFirebase(String userLogin, String userPassword, Context context) {
        this.context = context;
        mainView.showProgress();
        iterator.login(userLogin, userPassword, this);
    }

    @Override
    public void loginFacebook(Profile profile, Context context) {
           iterator.loginFasebook(profile, this, context);
    }

    @Override
    public void getForecast(String city, double v, double v1, String nowURL) {

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
        RetriveProgress progress = new RetriveProgress();
        progress.execute(userLogin, userPassword);
    }

    @Override
    public void setUserFasebook(Users users) {
        mainView.setUser(users);
    }



    private void retrivActiveUser(String login, String password) {
        for (int userNumber = 0; userNumber < retrivUserArray.size(); userNumber++) {
            if (login.equals(retrivUserArray.get(userNumber).getLogin()) &&
                    password.equals(retrivUserArray.get(userNumber).getPassword())) {
                activeUser.setUserName(retrivUserArray.get(userNumber).getUserName());
                activeUser.setLogin(retrivUserArray.get(userNumber).getLogin());
            }
        }
    }

    class RetriveProgress extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            FirebaseHelper.arrayListUser.clear();
            FirebaseHelper.arrayListUserData.clear();

            firebaseHelper.retrivDataUser();
            FirebaseHelper.arrayListLocation.clear();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firebaseHelper.sortDataUser();
            retrivUserArray = FirebaseHelper.arrayListUser;
            retrivActiveUser(strings[0], strings[1]);
            if (activeUser.getUserName() != null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                firebaseHelper.downloadPhotoStorage(activeUser.getUserName());
                firebaseHelper.retriveDataLocation(activeUser.getUserName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                mainView.toastShow("Incorect login or password");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activeUser.setPhoto(FirebaseHelper.photoDownload);
            activeUser.setLocation(FirebaseHelper.arrayListLocation);
            mainView.hideProgress();
            mainView.setUser(activeUser);
            mainView.setViseibleLogin();
            if(activeUser.getUserName()!=null) {
                mainView.setDialogClosed();
            }
            else {
                mainView.setLoginError("Incorect login or password");
            }

        }
    }

}
