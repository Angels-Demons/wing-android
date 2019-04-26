package activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import helper.AsyncRequest;
import helper.PreferenceManager;
import helper.WebService;
import masterpiece.wing.R;
import me.anwarshahriar.calligrapher.Calligrapher;
import model.PreferenceModel;
import model.Profile;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private PreferenceManager preferenceManager;
    private Class nextActivity;

    public static final boolean DEBUG = true;

    PreferenceModel preferenceModel;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_splash_screen);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"quicksandregular.otf",true);



//        initiating preferenceModel
        preferenceManager = new PreferenceManager(this);

        try {
            preferenceManager.getPreferenceModel().getPhone();
        }
        catch (NullPointerException e){
//            e.printStackTrace();
//            System.out.println("caught =================================================");
            preferenceManager.setPreferenceModel(new PreferenceModel());
        }

        preferenceModel = preferenceManager.getPreferenceModel();
        if (!preferenceModel.isFirstTimeLaunch()) {
            // is noy first time launch
            // check for is logged in
            if (preferenceModel.isLoggedIn()){
//                WebService.myProfile(preferenceModel, preferenceManager, this, new Intent(getApplicationContext(), MainActivity.class));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Checking for first time launch - before calling setContentView()
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }, 500);

            }
            else {
//                by passing login page
//                nextActivity = Login.class;
                nextActivity = Register.class;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Checking for first time launch - before calling setContentView()
                        startActivity(new Intent(getApplicationContext(), nextActivity));
                        finish();
                    }
                }, 500);
            }
        }
        else {
            // is first time launch
            // launches welcome activity then register
            preferenceModel.setFirstTimeLaunch(false, preferenceManager);
//            nextActivity = WelcomeActivity.class;
//            modify
            nextActivity = Register.class;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Checking for first time launch - before calling setContentView()
                    startActivity(new Intent(getApplicationContext(), nextActivity));
                    finish();
                }
            }, 500);
        }



    }
}
