package ivanrudyk.com.open_weather_api.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.helpers.AlertDialogFragment;
import ivanrudyk.com.open_weather_api.helpers.Helper;
import ivanrudyk.com.open_weather_api.helpers.RealmDbHelper;
import ivanrudyk.com.open_weather_api.model.CurrentlyWeather;
import ivanrudyk.com.open_weather_api.model.Forecast;
import ivanrudyk.com.open_weather_api.model.Users;
import ivanrudyk.com.open_weather_api.presenter.activity.MainPresenter;
import ivanrudyk.com.open_weather_api.presenter.activity.MainPresenterImplement;
import ivanrudyk.com.open_weather_api.ui.fragment.NavigationDraverFragment;

class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private static final String KEY = "keyprf";

    Toolbar toolbar;
    ImageView imOk;
    TextView etRegister, tv;
    EditText etLogin, etPassword;
    ProgressBar progressBar;
    LoginButton loginButtonFacebook;
    ImageButton ibLogin;
    public static final String TAG = MainActivity.class.getSimpleName();
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView descriptonField;
    ImageView iconView;
    Button btnCurrentPlace;
    Button btnCityChanged;
    CheckBox checkBoxShowPassword;
    ImageView mRefreshImageView;
    ProgressBar mProgressBar;
    TextView mEmptyTextView;
    Handler handler;
    public Forecast mForecast = new Forecast();
    public Helper mHelper = new Helper();
    Drawable drawable;
    private static final String BASE_CURRENT_WEATHER_URL_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPId=%s";
    private static final String BASE_CURRENT_WEATHER_URL_COORD = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&APPId=%s";
    String nowURL = BASE_CURRENT_WEATHER_URL_COORD;
    String city = "";
    String jsonData = "";
    double[] coord = new double[2];

    MainPresenter presenter;
    Users users = new Users();

    public MainActivity() {
        handler = new Handler();
    }

    RealmDbHelper dbHelper = new RealmDbHelper();
    Profile profile;
    Bitmap bitmap;
    int inputType;

    private Dialog d;

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            profile = Profile.getCurrentProfile();
            presenter.loginFacebook(profile, getApplicationContext());

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        setVisibleLoginItem();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        RealmDbHelper dbHelper = new RealmDbHelper();
//        Users u = new Users();
//        u = dbHelper.retriveUserFromRealm(this);
//        if (u.getUserName() != null && u.getUserName().length() > 0) {
//            if (users.getUserName() != null && users.getUserName().length() > 0) {
//                dbHelper.deleteUserFromRealm(this);
//
//            }
//
//        }
//        dbHelper.saveUserToRealm(users, this);
//        ArrayHelper arrayHelper = new ArrayHelper(this);
//        arrayHelper.saveArray(KEY, (ArrayList<String>) users.getLocation());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        mRefreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        descriptonField = (TextView) findViewById(R.id.decription_field);
        iconView = (ImageView) findViewById(R.id.icon_Image);
        btnCurrentPlace = (Button) findViewById(R.id.currentLoc);
        btnCityChanged = (Button) findViewById(R.id.cityLoc);
        coord = mHelper.CoordTracker(getApplicationContext());
        Log.e("TESTING", "Lat = : " + coord[0]);
        Log.e("TESTING", "Lon = " + coord[1]);

        // city = "";

        btnCurrentPlace.setOnClickListener(this);
        btnCityChanged.setOnClickListener(this);
        mRefreshImageView.setOnClickListener(this);

        users = dbHelper.retriveUserFromRealm(this);

        presenter = new MainPresenterImplement(this);

        ibLogin = (ImageButton) findViewById(R.id.ibLogin);
        onCreareToolBar();
        ibLogin.setOnClickListener(this);
        mCallbackManager = new CallbackManager.Factory().create();
        profile = Profile.getCurrentProfile();

        InitializeDialog();

    }

    private void InitializeDialog() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.login_layout);
        loginButtonFacebook = (LoginButton) d.findViewById(R.id.login_button);
        etLogin = (EditText) d.findViewById(R.id.etLogin);
        etPassword = (EditText) d.findViewById(R.id.etPassword);
        checkBoxShowPassword = (CheckBox) d.findViewById(R.id.checkBoxShowPassword);
        inputType = etPassword.getInputType();
//        etPassword.setInputType(InputType.TEXT);
    }


    private void onCreareToolBar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        onCreateNavigationDraver();
    }

    private void onCreateNavigationDraver() {
        NavigationDraverFragment draverFragment = (NavigationDraverFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_draver);
        draverFragment.setUp(R.id.fragment_navigation_draver, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, users);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogLogin() {
        loginButtonFacebook.setReadPermissions("user_friends");
        loginButtonFacebook.registerCallback(mCallbackManager, mCallback);
        progressBar = (ProgressBar) d.findViewById(R.id.progressBarLogin);
        imOk = (ImageView) d.findViewById(R.id.iv_ok_login);
        etRegister = (TextView) d.findViewById(R.id.etRegister);
        etRegister.setSelectAllOnFocus(true);

        checkBoxShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxShowPassword.isChecked()){
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else {
                    etPassword.setInputType(inputType);
                }
            }
        });
        etRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        imOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Helper.isNetworkAvailable(getApplicationContext())) {
                    d.cancel();
                    Toast.makeText(getApplicationContext(),
                            "No internet access", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    presenter.retriveUserFirebase(etLogin.getText().toString(), etPassword.getText().toString(), MainActivity.this);
                }
            }
        });
        d.show();
    }

    protected void updateDisplay() {
        CurrentlyWeather currentlyWeather = mForecast.getCurrent();
        cityField.setText(currentlyWeather.mLocationCurrentWeather.getCity().toUpperCase(Locale.US) +
                ", " +
                currentlyWeather.mLocationCurrentWeather.getCountry().toUpperCase(Locale.US));
        descriptonField.setText(currentlyWeather.mCurrentCondition.getCondition() + "(" + currentlyWeather.mCurrentCondition.getDescription() + ")");
        detailsField.setText("Humidity: " + String.format("%.0f", currentlyWeather.mCurrentCondition.getHumidity()) + "%" +
                "\n" + "Pressure: " + String.format("%.0f", currentlyWeather.mCurrentCondition.getPressure()) + " hPa" +
                "\n" + "Wind speed: " + String.format("%.0f", currentlyWeather.mWind.getSpeed()) + " mps" +
                "\n" + "Wind direction: " + String.format("%.0f", currentlyWeather.mWind.getDegree()) + "º" +
                "\n" + "Cloudness: " + currentlyWeather.mClouds.getPrecipitation() + " %"
        );
        currentTemperatureField.setText(String.format("%.0f", currentlyWeather.mTemperature.getTemperature()) + "℃");
        updatedField.setText("Last update: " + currentlyWeather.mLastUpdate.gettimeUpdate().toUpperCase(Locale.US));
        drawable = getResources().getDrawable(currentlyWeather.getIconId());
        iconView.setImageDrawable(drawable);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibLogin:
                if (this.users.getUserName() != null) {
                    showDialogQuit();
                } else {
                    showDialogLogin();
                }

                break;
            case R.id.currentLoc:
//                nowURL = BASE_CURRENT_WEATHER_URL_COORD;
//                coord = mHelper.CoordTracker(getApplicationContext());
//                try {
//                    getForecast(city,  coord[0],  coord[1], nowURL);
//                    city = "";
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
                break;
            case R.id.cityLoc:
                nowURL = BASE_CURRENT_WEATHER_URL_CITY;
                showInputDialog();
                coord[0] = 0.0;
                coord[1] = 0.0;
                break;
            default:
                break;
        }
    }

    private void showDialogQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        final TextView input = new TextView(this);
        input.setText("You really want to continue?");
        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String temp = users.getLogin();
                if(users.getLogin()== null || temp.length()==0 || temp.equals("")){
                    loginButtonFacebook.performClick();
                }
                Users userQuit = new Users();
                ArrayList<String>  ar = new ArrayList<String>();
                users = userQuit;
                users.setLocation(ar);
                dbHelper.deleteUserFromRealm(getApplication());
                onCreareToolBar();
                setVisibleLoginItem();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Change city");
        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                city = input.getText().toString();
                presenter.getForecast(city, coord[0], coord[1], nowURL);
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input.setText("");
            }
        });
        builder.show();
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
        Context context = getApplicationContext();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_message))
                .setPositiveButton(context.getString(R.string.error_ok_button_text), null);

        builder.show();
    }

    public void dialogClosed() {
        d.cancel();
    }

    @Override
    public void setLoginError(String value) {
        etLogin.setError(value);
    }

    @Override
    public void setPasswordError() {
        etPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void toastShow(String value) {

    }

    @Override
    public void setUser(Users activeUser) {
        dbHelper.saveUserToRealm(activeUser, this);
        this.users = activeUser;
        etLogin.setText("");
        etPassword.setText("");
        ibLogin.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp));
        d.cancel();
        onCreareToolBar();
    }

    @Override
    public void setViseibleLogin() {
        setVisibleLoginItem();
    }

    @Override
    public void setDialogClosed() {
        dialogClosed();
    }

    private void setVisibleLoginItem() {
        if (users.getUserName() == null) {
            ibLogin.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open_white_24dp));
        } else {
            ibLogin.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp));
        }
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}