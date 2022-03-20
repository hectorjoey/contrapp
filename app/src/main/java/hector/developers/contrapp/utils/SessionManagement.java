package hector.developers.contrapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManagement {

    public static final String LOGIN_EMAIL = "mobile.fhi360.covid_19selfscreeningtool.LOGIN_EMAIL";
    public static final String LOGIN_PASSWORD = "mobile.fhi360.covid_19selfscreeningtool.LOGIN_PASSWORD";

    private final SharedPreferences prefs;

    public SessionManagement(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLoginEmail(String loginEmail) {
        prefs.edit().putString(LOGIN_EMAIL, loginEmail).apply();
    }

    public String getLoginEmail() {
        return prefs.getString(LOGIN_EMAIL, "");
    }

    public void setLoginPassword(String loginPassword) {
        prefs.edit().putString(LOGIN_PASSWORD, loginPassword).apply();
    }

    public String getLoginPassword() {
        return prefs.getString(LOGIN_PASSWORD, "");
    }
}
