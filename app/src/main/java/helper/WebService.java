package helper;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import activities.MainActivity;
import activities.Register;
import cz.msebera.android.httpclient.Header;
import model.PreferenceModel;
import model.Profile;
import model.Scooter;

import static activities.SplashScreen.DEBUG;
/**
 * Created by User on 12/8/2018.
 */

public class WebService {
    public static void startRide(final MainActivity activity){
        RequestParams params = new RequestParams();
        params.put("phone", activity.preferenceModel.getPhone());
        params.put("qr_info", activity.qrInfo);
        final String relativeUrl = "scooter/start_ride/";

        AsyncRequest.post(relativeUrl, params, "Authorization", "token " + activity.preferenceModel.getToken(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
//                modify : if token was taken
                try {
                    if (response.getString("message").contains("success")) {
//                        activity.preferenceModel.setRideId(response.getString("ride_id"), activity.preferenceManager);
                        activity.profile.setIs_riding(true, activity.preferenceManager);
                        activity.profile.setRide_id(Integer.parseInt(response.getString("ride_id")), activity.preferenceManager);
                        System.out.println(response.getString("ride_id"));

//                        start trip
//            if successful
                        activity.setState(2);
//            else
                    }
                    Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "کد تصویری نامعتبر", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + relativeUrl);
            }
        });
    }

    public static void endRide(final MainActivity activity){
        RequestParams params = new RequestParams();
        params.put("phone", activity.preferenceModel.getPhone());
//        modify: got ride id from profile instead of preference model
//        I can delete ride id from preference model
        params.put("ride_id", activity.profile.getRide_id());
//        if (DEBUG) System.out.println("debug: ride id from profile: " + activity.profile.getRide_id());
//        if (DEBUG) System.out.println("debug: ride id: " + activity.preferenceModel.getRideId());
        final String relativeUrl = "scooter/end_ride/";

        AsyncRequest.post(relativeUrl, params, "Authorization", "token " + activity.preferenceModel.getToken(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
                try {
                    if (response.getString("message").contains("success")) {
//                        activity.preferenceModel.setRideId("", activity.preferenceManager);
                        activity.profile.setRide_id(0, activity.preferenceManager);
                        activity.profile.setIs_riding(false, activity.preferenceManager);

//                        end trip
//            if successful
                        activity.setState(0);
//            else
                    }
                    Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "کد سفر نامعتبر", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
            }
        });
    }

    public static void nearbyDevices(final MainActivity activity){
        RequestParams params = new RequestParams();
        params.put("phone", activity.preferenceModel.getPhone());
//        modify : find by location
        params.put("latitude", 35);
        params.put("longitude", 51);
        params.put("radius", 1);
        final String relativeUrl = "scooter/nearby_devices/";

        AsyncRequest.post(relativeUrl, params, "Authorization", "token " + activity.preferenceModel.getToken(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
                activity.scooters = new Gson().fromJson(response.toString(), new TypeToken<List<Scooter>>() {
                }.getType());
//                modify
                try {
                    activity.loadMap();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                System.out.println(activity.scooters.size() + " scooters loaded to map");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + relativeUrl);
            }
        });
    }

    public static void register(final Register activity){
        RequestParams params = new RequestParams();
        params.put("phone", activity.phone.getText().toString());
        params.put("password", activity.password.getText().toString());
        final String relativeUrl = "accounts/register/";
        AsyncRequest.post(relativeUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
//                modify : check if response is 1(success)
//                if (response.toString().contentEquals("1"));
                activity.preferenceModel.setPhone(activity.phone.getText().toString(), activity.preferenceManager);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
                System.out.println(statusCode);
                System.out.println(responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + relativeUrl);
            }
        });

    }

    public static void login(final Register activity){
            RequestParams params = new RequestParams();
//        System.out.println("debug: " + preferenceModel.getPhone());
        params.put("phone", activity.preferenceModel.getPhone());
        params.put("password", activity.verificationCode.getText().toString());
        final String relativeUrl = "accounts/login/";

        AsyncRequest.post(relativeUrl, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
//                modify : if token was taken
                try {
                    activity.preferenceModel.setToken(response.getString("token"), activity.preferenceManager);
                    activity.preferenceModel.setLoggedIn(true, activity.preferenceManager);
                    activity.animator.cancel();
                    loadProfileFirstTime(activity);
//                    activity.finish();
//                    activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity.getApplicationContext(), "کد نامعتبر", Toast.LENGTH_SHORT).show();
                    activity.verificationCode.setText("");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + relativeUrl);
            }
        });
    }

    private static void loadProfileFirstTime(final Register activity){
        RequestParams params = new RequestParams();
        params.put("phone", activity.preferenceModel.getPhone());
        final String relativeUrl = "scooter/my_profile/";
        if (DEBUG) System.out.println("posting: scooter/my_profile");
        AsyncRequest.post(relativeUrl, params, "Authorization", "token " + activity.preferenceModel.getToken(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
                Profile profile = new Gson().fromJson(response.toString(), Profile.class);
                if (profile != null){
                    System.out.println("profile loaded");
                    if (DEBUG) System.out.println("debug: is riding boolean: " + profile.isIs_riding());
                }
                else {
                    System.out.println("profile not loaded. making a fake one");
                    profile = new Profile(false, 1000, 0, "0");
                }
                activity.preferenceManager.setProfile(profile);
//                modify
//                activity.profile = profile;
                if (DEBUG) System.out.println("debug: is riding boolean: " + profile.isIs_riding());
                if (DEBUG) System.out.println("debug:" + profile.toString());
                activity.finish();
                activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));

                if (profile.isIs_riding()){
                    activity.preferenceModel.setState(2, activity.preferenceManager);
                    if (DEBUG) System.out.println("user is riding : state is set to 2");
//                            setState(preferenceModel.getState());
                }
                else {
                    activity.preferenceModel.setState(0, activity.preferenceManager);
                    if (DEBUG) System.out.println("user is not riding : state is set to 0");
//                            setState(preferenceModel.getState());
                }
//                activity.setState(activity.preferenceModel.getState());
//                if (DEBUG) System.out.println("starting map activity");
//                activity.startActivity(intent);
//                activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
//                activity.finish();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
                if (DEBUG) System.out.println("starting register activity");
                activity.startActivity(new Intent(activity, Register.class));
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + relativeUrl);
                if (DEBUG) System.out.println("starting register activity");
                activity.startActivity(new Intent(activity, Register.class));
            }
        });
    }

    public static void myProfile(final MainActivity activity){
        RequestParams params = new RequestParams();
        params.put("phone", activity.preferenceModel.getPhone());
        final String relativeUrl = "scooter/my_profile/";
        if (DEBUG) System.out.println("posting: scooter/my_profile");
        AsyncRequest.post(relativeUrl, params, "Authorization", "token " + activity.preferenceModel.getToken(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("success: " + relativeUrl + "\n" + response.toString());
                Profile profile = new Gson().fromJson(response.toString(), Profile.class);
                if (profile != null){
                    System.out.println("profile loaded");
                    if (DEBUG) System.out.println("debug: is riding boolean: " + profile.isIs_riding());
                }
                else {
                    System.out.println("profile not loaded. making a fake one");
                    profile = new Profile(false, 1000, 0, "0");
                }
                activity.preferenceManager.setProfile(profile);
                activity.profile = profile;
                if (DEBUG) System.out.println("debug: is riding boolean: " + profile.isIs_riding());
                if (DEBUG) System.out.println("debug:" + profile.toString());
                if (profile.isIs_riding()){
                    activity.preferenceModel.setState(2, activity.preferenceManager);
                    if (DEBUG) System.out.println("user is riding : state is set to 2");
//                            setState(preferenceModel.getState());
                }
                else {
                    activity.preferenceModel.setState(0, activity.preferenceManager);
                    if (DEBUG) System.out.println("user is not riding : state is set to 0");
//                            setState(preferenceModel.getState());
                }
                activity.setState(activity.preferenceModel.getState());
//                if (DEBUG) System.out.println("starting map activity");
//                activity.startActivity(intent);
//                activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
//                activity.finish();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("failure: " + relativeUrl);
                if (DEBUG) System.out.println("starting register activity");
                activity.startActivity(new Intent(activity, Register.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("failure: " + relativeUrl);
                if (DEBUG) System.out.println("starting register activity");
                activity.startActivity(new Intent(activity, Register.class));
            }
        });
    }
}
