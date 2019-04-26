package helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import model.PreferenceModel;
import model.Profile;
import model.Scooter;

/**
 * Created by User on 4/18/2018.
 */

public class PreferenceManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;

    // shared sharedPreferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "intro_slider-welcome";

//    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
//    private static final String IS_LOGGED_IN = "IsLoggedIn";
//    private static final String PHONE = "Phone";
//    private static final String TOKEN = "Token";
//    private static final String RIDE_ID = "RideId";

    private static final String PREFERENCE_MODEL = "PreferenceModel";
    private static final String PROFILE = "Profile";
    private static final String SCOOTER = "Scooter";

    public PreferenceManager(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setProfile(Profile profile){
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        editor.putString(PROFILE, json);
        editor.commit();
    }

    public Profile getProfile(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PROFILE, "");
        return gson.fromJson(json, Profile.class);
    }

    public void setPreferenceModel(PreferenceModel preferenceModel){
        Gson gson = new Gson();
        String json = gson.toJson(preferenceModel);
        editor.putString(PREFERENCE_MODEL, json);
        editor.commit();
    }

    public PreferenceModel getPreferenceModel(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREFERENCE_MODEL, "");
        return gson.fromJson(json, PreferenceModel.class);
    }

    public void setScooter(Scooter scooter){
        Gson gson = new Gson();
        String json = gson.toJson(scooter);
        editor.putString(SCOOTER, json);
        editor.commit();
    }

    public Scooter getScooter(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SCOOTER, "");
        return gson.fromJson(json, Scooter.class);
    }
}
