package helper;

import com.loopj.android.http.*;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
/**
 * Created by User on 11/12/2018.
 */

public class AsyncRequest {
//    public static String BASE_URL = "https://ajax.googleapis.com/";
//    public static String BASE_URL = "http://172.16.3.157:8000/";
//    public static String BASE_URL = "http://178.32.170.99/";
//    public static String BASE_URL = "http://api2.wingco.ir/";
    public static String BASE_URL = "http://5.253.27.158/";
    public static void get(final String relativeUrl, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        String url = BASE_URL + relativeUrl;
        AsyncHttpClient client = new AsyncHttpClient();
        if (jsonHttpResponseHandler == null){
            jsonHttpResponseHandler = new JsonHttpResponseHandler(){
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
        client.get(url, params, jsonHttpResponseHandler);
    }

    public static void post(final String relativeUrl, RequestParams params, String headerKey, String headerValue, JsonHttpResponseHandler jsonHttpResponseHandler){
        String url = BASE_URL + relativeUrl;
        AsyncHttpClient client = new AsyncHttpClient();
        if (headerKey != null && headerValue != null){
            client.addHeader(headerKey, headerValue);
        }
        if (jsonHttpResponseHandler == null){
            jsonHttpResponseHandler = new JsonHttpResponseHandler(){
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

    public static void post(final String relativeUrl, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler){
        String url = BASE_URL + relativeUrl;
        AsyncHttpClient client = new AsyncHttpClient();
        if (jsonHttpResponseHandler == null){
            jsonHttpResponseHandler = new JsonHttpResponseHandler(){
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
