package ivanrudyk.com.open_weather_api.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;
import ivanrudyk.com.open_weather_api.database.RealmModelUser;
import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 11.08.2016.
 */
public class RealmDbHelper {

    private static final String KEY = "keyprf";
    public static final String PREF_FILE_NAME = "prefname";

    public void saveUserToRealm(Users user, Context context) {
        Realm realm = null;
        Realm realm1 = Realm.getDefaultInstance();
        try {
            realm = Realm.getInstance(context);

        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealmFile(context);
            realm = Realm.getInstance(context);
        }
        if(user.getUserName()!=null) {
            realm.beginTransaction();
            RealmModelUser person = realm.createObject(RealmModelUser.class);
            person.setByteArray(encodeTobase64(user.getPhoto()));
            person.setUserName(user.getUserName());
            person.setLogin(user.getLogin());
            realm.commitTransaction();
            ArrayHelper arrayHelper = new ArrayHelper(context);
            arrayHelper.removeDataSharedPrefs(PREF_FILE_NAME);
            arrayHelper.saveArray(KEY, (ArrayList<String>) user.getLocation());
        }
    }

    public Users retriveUserFromRealm(Context context) {
        ArrayList<String> arrLoc = new ArrayList();
        ArrayHelper arrayHelper = new ArrayHelper(context);
        Users users = new Users();
        Realm realm = Realm.getInstance(context);
        RealmModelUser realmModelUser = realm.where(RealmModelUser.class).findFirst();
        if (realmModelUser != null) {
            users.setPhoto(decodeBase64(realmModelUser.getByteArray()));
            users.setLogin(realmModelUser.getLogin());
            users.setUserName(realmModelUser.getUserName());
            users.setLocation(arrayHelper.getArray(KEY));
        }
        return users;
    }

    public void deleteUserFromRealm(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        RealmQuery query = realm.where(RealmModelUser.class);
        RealmResults results = query.findAll();
        results.remove(0);
        realm.commitTransaction();
    }

    public static byte[] encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static Bitmap decodeBase64(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
