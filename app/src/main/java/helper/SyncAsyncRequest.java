package helper;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONObject;

import java.net.InetAddress;

import application.MyApplication;
import cz.msebera.android.httpclient.Header;
/**
 * Created by User on 11/12/2018.
 */

public class SyncAsyncRequest {
    public static String BASE_URL = MyApplication.BASE_URL;

    public static void post(final Activity activity, boolean synchronous, final String relativeUrl, RequestParams params, String headerKey, String headerValue, JsonHttpResponseHandler jsonHttpResponseHandler){
//        modify
        if (!((MyApplication)activity.getApplication()).isNetworkConnected(activity)){
            System.out.println("===== network is not connected: " + relativeUrl);
            return;
        }
        if (!isInternetAvailable(activity)){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(activity,"دستگاه شما به اینترنت متصل نیست", Toast.LENGTH_SHORT).show();
                }
            });

//            return;
        }
        String url = BASE_URL + relativeUrl;
        if (synchronous){
            SyncHttpClient client = new SyncHttpClient();
            if (headerKey != null && headerValue != null) {
                client.addHeader(headerKey, headerValue);
            }
            if (jsonHttpResponseHandler == null) {
                jsonHttpResponseHandler = new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("success: " + relativeUrl + "\n" + response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("failure: " + relativeUrl);
                        System.out.println(statusCode);
                        System.out.println(responseString);
                        throwable.printStackTrace();
                    }
                };
            }
            client.post(url, params, jsonHttpResponseHandler);
        }
        else {
            AsyncHttpClient client = new AsyncHttpClient();
            if (headerKey != null && headerValue != null) {
                client.addHeader(headerKey, headerValue);
            }
            if (jsonHttpResponseHandler == null) {
                jsonHttpResponseHandler = new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        System.out.println("success: " + relativeUrl + "\n" + response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        System.out.println("failure: " + relativeUrl);
                        System.out.println(statusCode);
                        System.out.println(responseString);
                        throwable.printStackTrace();
                    }
                };
            }
            client.post(url, params, jsonHttpResponseHandler);
        }
    }

    private static boolean isInternetAvailable(Activity activity) {
        try {
//            InetAddress ipAddr = InetAddress.getByName(MyApplication.BASE_URL);
            InetAddress ipAddr = InetAddress.getByName("http://google.com");
            //You can replace it with your name
            if (MyApplication.DEBUG) System.out.println("====");
            if (MyApplication.DEBUG) System.out.println(ipAddr);
            boolean available = ipAddr.equals("");
            if (available) return true;
//            modify
//            check network status and open wifi or data service dialog
//            Toast.makeText(activity,"ارتباط با اینترنت یا سرور برقرار نیست", Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if (MyApplication.DEBUG) System.out.println("===========");
//            Toast.makeText(activity,"ارتباط با اینترنت یا سرور برقرار نیست", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
