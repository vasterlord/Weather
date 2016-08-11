package ivanrudyk.com.open_weather_api.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.helper.ArrayHelper;
import ivanrudyk.com.open_weather_api.helper.RealmDbHelper;
import ivanrudyk.com.open_weather_api.model_user.Users;
import ivanrudyk.com.open_weather_api.presenter.activity.MainPresenter;
import ivanrudyk.com.open_weather_api.presenter.activity.MainPresenterImplement;
import ivanrudyk.com.open_weather_api.ui.fragment.NavigationDraverFragment;

class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private static final String KEY = "keyprf";

    Toolbar toolbar;
    ImageView imOk, imFasebookLogin, iv;
    TextView etRegister, tv;
    EditText etLogin, etPassword;
    ProgressBar progressBar;
    ImageButton ibLogin;

    MainPresenter presenter;
    Users users = new Users();

    RealmDbHelper dbHelper = new RealmDbHelper();

    private Dialog d;

    @Override
    protected void onStart() {
        super.onStart();
        setVisibleLoginItem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ArrayHelper arrayHelper = new ArrayHelper(this);
        arrayHelper.saveArray(KEY, (ArrayList<String>) users.getLocation());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        setContentView(R.layout.activity_main);

        users = dbHelper.retriveUserFromRealm(this);

        iv = (ImageView) findViewById(R.id.imageWeather);
        presenter = new MainPresenterImplement(this);
        tv = (TextView) findViewById(R.id.textView3);
        ibLogin = (ImageButton) findViewById(R.id.ibLogin);
        onCreareToolBar();
        ibLogin.setOnClickListener(this);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogLogin() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.login_layout);
        etLogin = (EditText) d.findViewById(R.id.etLogin);
        etPassword = (EditText) d.findViewById(R.id.etPassword);
        progressBar = (ProgressBar) d.findViewById(R.id.progressBarLogin);
        imOk = (ImageView) d.findViewById(R.id.iv_ok_login);
        etRegister = (TextView) d.findViewById(R.id.etRegister);
        etRegister.setSelectAllOnFocus(true);
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
                if (!isOnline()) {
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

    private boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibLogin:
                showDialogLogin();
                break;
            default:
                break;
        }
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
        this.users = activeUser;
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
            ibLogin.setVisibility(View.VISIBLE);
        } else {
            ibLogin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}