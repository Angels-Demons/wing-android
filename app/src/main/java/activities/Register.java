package activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;

import com.google.firebase.analytics.FirebaseAnalytics;

import helper.PreferenceManager;
import helper.WebService;
import masterpiece.wing.R;
import model.PreferenceModel;
import ui.PieView;

public class Register extends BaseActivity {
//public class Register extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    public PieView pieView;
    public TextView timer;
    public static final long TIMER = 2 * 60 * 1000;
    public ObjectAnimator animator;

//    public EditText phone, password, repeatPassword;
    public EditText phone;
    public TextView phoneVerify;

    public EditText verificationCode;

    public PreferenceManager preferenceManager;
    public PreferenceModel preferenceModel;

    LinearLayout logoHeader;
    TextView rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_register);

        attachKeyboardListeners();

        preferenceManager = new PreferenceManager(this);
        preferenceModel = preferenceManager.getPreferenceModel();
        init1();
    }

    public void closeClicked (View view){
        startActivity(new Intent(getApplicationContext(), Login.class));
        this.finish();
    }

    public void registerClicked (View view){
        if (!verifyCredentials(phone)){
            Toast.makeText(getApplicationContext(), "wrong credentials", Toast.LENGTH_SHORT).show();
            return;
        }
        WebService.register(this);
//        register
//        RequestParams params = new RequestParams();
//        params.put("phone", phone.getText().toString());
//        params.put("password", password.getText().toString());
//        final String relativeUrl = "accounts/register/";
//        SyncAsyncRequest.post(relativeUrl, params, new JsonHttpResponseHandler(){
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

    }

    public void verifyClicked (View view){
        WebService.login(this);
    }

    private void init1(){
        phone = (EditText) findViewById(R.id.phone);
//        password = (EditText) findViewById(R.id.password);

        pieView = (PieView) findViewById(R.id.pieView);
        timer = (TextView) findViewById(R.id.timer);

        rules = findViewById(R.id.rules);
        logoHeader = findViewById(R.id.logo_header);

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://wingco.ir/terms/")));
            }
        });
    }

    public void init2(){
        pieView = (PieView) findViewById(R.id.pieView);
        timer = (TextView) findViewById(R.id.timer);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        phoneVerify = findViewById(R.id.phone_verify);
//        Calligrapher calligrapher = new Calligrapher(this);
//        calligrapher.setFont(this,"font.ttf",true);
    }

    private boolean verifyCredentials(EditText phone){
        if (phone.getText().toString().length() != 11){
            System.out.println("not 11 digits");
            phone.setText("");
            return false;
        }
//        if (!password.getText().toString().contentEquals(repeatPassword.getText().toString())){
//            password.setText("");
//            repeatPassword.setText("");
//            return false;
//        }
        if (!phone.getText().toString().startsWith("0")){
            System.out.println("does not start with 0");
            phone.setText("");
            return false;
        }
        return true;
    }

//    @Override
//    protected void onShowKeyboard(int keyboardHeight) {
//        System.out.println("===== on show");
//        // do things when keyboard is shown
//        rules.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected void onHideKeyboard() {
//        System.out.println("===== on hide");
//        // do things when keyboard is hidden
//        logoHeader.setVisibility(View.INVISIBLE);
//    }
}
