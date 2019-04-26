package activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import helper.AsyncRequest;
import helper.PreferenceManager;
import helper.WebService;
import masterpiece.wing.R;
import model.PreferenceModel;
import ui.PieView;

public class Register extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private PieView pieView;
    private TextView timer;
    private static final long TIMER = 60 * 1000;
    public ObjectAnimator animator;

    public EditText phone, password, repeatPassword;

    public EditText verificationCode;

    public PreferenceManager preferenceManager;
    public PreferenceModel preferenceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_register);

        preferenceManager = new PreferenceManager(this);
        preferenceModel = preferenceManager.getPreferenceModel();
        init1();
    }

    public void closeClicked (View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
        this.finish();
    }

    public void registerClicked (View view){
        if (!verifyCredentials(phone, password, repeatPassword)){
            Toast.makeText(getApplicationContext(), "wrong credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        WebService.register(this);
//        register
//        RequestParams params = new RequestParams();
//        params.put("phone", phone.getText().toString());
//        params.put("password", password.getText().toString());
//        final String relativeUrl = "accounts/register/";
//        AsyncRequest.post(relativeUrl, params, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                System.out.println("success: " + relativeUrl + "\n" + response.toString());
////                modify : check if response is 1(success)
////                if (response.toString().contentEquals("1"));
//                preferenceModel.setPhone(phone.getText().toString(), preferenceManager);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                System.out.println("failure: " + relativeUrl);
//                System.out.println(statusCode);
//                System.out.println(responseString);
//                throwable.printStackTrace();
//            }
//        });

        setContentView(R.layout.activity_register_verify);
        init2();
        animator = ObjectAnimator.ofInt(pieView, "degree", 360,0);
        animator.setDuration(TIMER);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                Toast.makeText(getApplicationContext(), "Time Expired", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_register);
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {

            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String timerText = String.valueOf(pieView.getDegree()*TIMER/360/1000);
                timerText = timerText + " seconds";
                timer.setText(timerText);
            }
        });
    }

    public void verifyClicked (View view){
        WebService.login(this);
    }

    private void init1(){
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.repeat_password);

        pieView = (PieView) findViewById(R.id.pieView);
        timer = (TextView) findViewById(R.id.timer);
    }

    private void init2(){
        pieView = (PieView) findViewById(R.id.pieView);
        timer = (TextView) findViewById(R.id.timer);
        verificationCode = (EditText) findViewById(R.id.verification_code);
//        Calligrapher calligrapher = new Calligrapher(this);
//        calligrapher.setFont(this,"font.ttf",true);
    }

    private boolean verifyCredentials(EditText phone, EditText password, EditText repeatPassword){
        if (phone.getText().toString().length() != 11){
            System.out.println("not 11 digits");
            phone.setText("");
            return false;
        }
        if (!password.getText().toString().contentEquals(repeatPassword.getText().toString())){
            password.setText("");
            repeatPassword.setText("");
            return false;
        }
        if (!phone.getText().toString().startsWith("0")){
            System.out.println("does not start with 0");
            phone.setText("");
            return false;
        }
        return true;
    }

}
