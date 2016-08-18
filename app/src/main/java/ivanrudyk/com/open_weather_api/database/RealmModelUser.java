package ivanrudyk.com.open_weather_api.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ivan on 11.08.2016.
 */
public class RealmModelUser extends RealmObject {
    @PrimaryKey
    private String userName;
    private String login;
    private byte[] byteArray;
    private RealmList<RealmLocation> realmLocationList = new RealmList<>();

    public RealmList<RealmLocation> getRealmLocationList() {
        return realmLocationList;
    }

    public void setRealmLocationList(RealmList<RealmLocation> realmLocationList) {
        this.realmLocationList = realmLocationList;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
