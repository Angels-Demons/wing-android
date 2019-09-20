package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import helper.SyncAsyncRequest;
import helper.PreferenceManager;
import masterpiece.wing.R;
import me.anwarshahriar.calligrapher.Calligrapher;
import model.PreferenceModel;

public class Login extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    PreferenceManager preferenceManager;

    private EditText phone, password;

    PreferenceModel preferenceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(this);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"font.ttf",true);

        preferenceModel = preferenceManager.getPreferenceModel();
    }

//    public void registerClicked(View view){
//        startActivity(new Intent(getApplicationContext(), Register.class));
//    }

    public void loginClicked (View view){
        RequestParams params = new RequestParams();
        params.put("phone", phone.getText().toString());
        params.put("password", password.getText().toString());
        final String relativeUrl = "accounts/login/";

        SyncAsyncRequest.post(this,false, relativeUrl, params, "", "", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
//                modify : if token was taken
                try {
                    preferenceModel.setToken(response.getString("token"), preferenceManager);
                    preferenceModel.setLoggedIn(true, preferenceManager);
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "کد نامعتبر", Toast.LENGTH_SHORT).show();
                    password.setText("");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
            }
        });
    }
}
