package ivanrudyk.com.open_weather_api.activity.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.activity.register.RegisterActivity;
import ivanrudyk.com.open_weather_api.fragment.NavigationDraverFragment;
import ivanrudyk.com.open_weather_api.model_user.Users;


public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    Toolbar toolbar;
    ImageView imOk, imFasebookLogin, iv;
    TextView etRegister, tv;
    EditText etLogin, etPassword;
    ProgressBar progressBar;


    MainPresenter presenter;
    Users users = new Users();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView)findViewById(R.id.imageWeather);
                presenter = new MainPresenterImplement(this);
        tv = (TextView) findViewById(R.id.textView3);
        onCreareToolBar(users);
    }


    private void onCreareToolBar(Users users) {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        if (id == R.id.navigate_login) {
            showDialogLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogLogin() {
        final Dialog d = new Dialog(this);
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
                presenter.retriveUserFirebase(etLogin.getText().toString(), etPassword.getText().toString());
            }
        });

        d.show();
    }

    @Override
    public void onClick(View view) {

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
        onCreareToolBar(activeUser);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
