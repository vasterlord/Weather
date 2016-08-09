package ivanrudyk.com.open_weather_api.activity.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.model_user.Users;

public class RegisterActivity extends AppCompatActivity implements RegisterView, View.OnClickListener {


    EditText etLoginRegister, etPasswordRegister, etUserName, etCity, etConfirmPassword;
    ImageView ivCamera, ivGalary, ivOkRegister, ivCancelRegister;
    ProgressBar progressBarRegister;

    CameraPhoto cameraPhoto;
    private final int CAMERA_REQUEST = 13323;
    private Bitmap photoLoad;
    GalleryPhoto galleryPhoto;
    private final int GALLERY_REQEST = 34623;

    RegisterPresenter presenter;
    Users userAdd = new Users();
    ArrayList<String> locationStart = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        initializeComponentView();

        presenter = new RegisterPresenterImplement(this);


        ivOkRegister.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivCancelRegister.setOnClickListener(this);
        ivGalary.setOnClickListener(this);
    }

    private void initializeComponentView() {
        etLoginRegister = (EditText) findViewById(R.id.et_register_login);
        etPasswordRegister = (EditText) findViewById(R.id.et_register_password);
        etUserName = (EditText) findViewById(R.id.et_register_user_name);
        etCity = (EditText) findViewById(R.id.et_city_register);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivGalary = (ImageView) findViewById(R.id.iv_galery);
        ivOkRegister = (ImageView) findViewById(R.id.iv_ok_register);
        ivCancelRegister = (ImageView) findViewById(R.id.iv_cancel_register);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ok_register:
                locationStart.clear();
                locationStart.add(etCity.getText().toString());
                userAdd.setLogin(etLoginRegister.getText().toString());
                userAdd.setUserName(etUserName.getText().toString());
                userAdd.setPassword(etPasswordRegister.getText().toString());
                userAdd.setLocation(locationStart);
                String city = etCity.getText().toString();
                String confPass = etConfirmPassword.getText().toString();
                presenter.addUser(userAdd, confPass, city, photoLoad);
                break;
            case R.id.iv_camera:
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_galery:
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQEST);
                break;
            case R.id.iv_cancel_register:
                NavUtils.navigateUpFromSameTask(RegisterActivity.this);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(50, 80).getBitmap();
                    photoLoad = bitmap;
                } catch (FileNotFoundException e) {

                }

            } else if (requestCode == GALLERY_REQEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(50, 80).getBitmap();
                    photoLoad = bitmap;
                } catch (FileNotFoundException e) {

                }

            }
        }
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBarRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarRegister.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setUsernameError() {
        etUserName.setError(getString(R.string.username_error));
    }

    @Override
    public void setPasswordError() {
        etPasswordRegister.setError(getString(R.string.password_error));
    }

    @Override
    public void setLoginError() {
        etLoginRegister.setError(getString(R.string.login_error));
    }

    @Override
    public void setCityError() {
        etCity.setError(getString(R.string.city_error));
    }

    @Override
    public void setConfirmPasswordError(String s) {
        etConfirmPassword.setError(s);
    }

    @Override
    public void navigateToMain() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
