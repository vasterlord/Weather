package ivanrudyk.com.open_weather_api.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.helper.RealmDbHelper;

public class SettingsActivity extends AppCompatActivity {

    RealmDbHelper dbHelper = new RealmDbHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button bQuit = (Button) findViewById(R.id.bQuit);
        bQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteUserFromRealm(SettingsActivity.this);
            }
        });
    }
}
