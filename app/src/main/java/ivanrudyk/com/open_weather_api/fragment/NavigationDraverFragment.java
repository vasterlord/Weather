package ivanrudyk.com.open_weather_api.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.activity.main.MainPresenter;
import ivanrudyk.com.open_weather_api.helper.PhotoHelper;
import ivanrudyk.com.open_weather_api.model_user.Users;


/**
 * A simple {@link Fragment} subclass.
 */

public class NavigationDraverFragment extends Fragment {

    public Users users = new Users();

    TextView tvNavUserName, tvNavLogin;
    ImageView ivPhotoUser;


    public static final String PREF_FILE_NAME = "preffilename";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    private boolean mUserLearndDrawer;
    private boolean mFromSavedInstanseState;
    private View containerView;
    Bitmap bmEnd;

    MainPresenter presenter;
    PhotoHelper photoHelper = new PhotoHelper();

    public NavigationDraverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (bmEnd == null) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.qwe);
            bmEnd = photoHelper.getCircleMaskedBitmapUsingClip(users.getPhoto(), 60);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mUserLearndDrawer = Boolean.valueOf((readFromPreferenses(getActivity(), KEY_USER_LEARNED_DRAWER, "false")));
        if (savedInstanceState != null) {
            mFromSavedInstanseState = true;
        }
//        users.setUserName("");
//        users.setPhoto(BitmapFactory.decodeResource(getResources(), R.drawable.qwe));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_navigation_draver, container, false);
        ivPhotoUser = (ImageView) v.findViewById(R.id.ivPhotoUser);
        tvNavUserName = (TextView) v.findViewById(R.id.tvDrUserName);
        tvNavLogin = (TextView) v.findViewById(R.id.tvDrLogin);
        ivPhotoUser.setImageBitmap(photoHelper.getCircleMaskedBitmapUsingClip(BitmapFactory.decodeResource(getResources(), R.drawable.qwe), 60));
        return v;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolBar, Users users) {
        this.users = users;

        tvNavUserName.setText(this.users.getUserName());
        tvNavLogin.setText(this.users.getLogin());
        ivPhotoUser.setImageBitmap(PhotoHelper.getCircleMaskedBitmapUsingClip(this.users.getPhoto(), 60));

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearndDrawer) {
                    mUserLearndDrawer = true;
                    saveToPreferenses(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearndDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                if (slideOffset < 0.6) {
//                    toolBar.setAlpha(1 - slideOffset);
//                }
//            }
        };

        if (!mUserLearndDrawer && !mFromSavedInstanseState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferenses(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferenses(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

}
