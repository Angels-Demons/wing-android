package application;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.maps.model.LatLng;

import java.net.InetAddress;

import activities.MainActivity;
import dialogs.NetworkDialog;
import notifications.MyAppsNotificationManager;

public class MyApplication extends Application {
    public boolean isNetworkDialogActive = false;
    public static final String BASE_URL = "http://5.253.27.84/";
    public static boolean DEBUG = true;

    public static final String NEWS_CHANNEL_ID = "abc";
    public static final String CHANNEL_NEWS = "def";
    public static final String CHANNEL_DESCRIPTION = "ghi";
    public static final int notificationId = 100;

    //        10 for tehran
//        15 for sharif
    public static final int DEFAULT_ZOOM = 15;
    public static final LatLng sharifUni = new LatLng(35.7002762, 51.354876);

    public static final int ICON_SCALE = 16;
    public static final int ICON_HEIGHT = 9;
    public static final int ICON_WIDTH = 7;


    MyAppsNotificationManager  myAppsNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        TypeFaceUtil.overrideFont(getApplicationContext(), "SERIF", "font.ttf");

        myAppsNotificationManager = MyAppsNotificationManager.getInstance(this);
        myAppsNotificationManager.registerNotificationChannelChannel(
                MyApplication.NEWS_CHANNEL_ID,
                MyApplication.CHANNEL_NEWS,
                MyApplication.CHANNEL_DESCRIPTION);
    }

    public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){
        myAppsNotificationManager.triggerNotification(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId, pendingIntentFlag);
    }

    public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId){
        myAppsNotificationManager.triggerNotification(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId);
    }

    public void triggerNotificationWithBackStack(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){
        myAppsNotificationManager.triggerNotificationWithBackStack(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId, pendingIntentFlag);
    }

    public void updateNotification(Class targetNotificationActivity,String title,String text, String channelId, int notificationId, String bigpictureString, int pendingIntentFlag, boolean onGoing){
        myAppsNotificationManager.updateWithoutPicture(targetNotificationActivity, title, text, channelId, notificationId, bigpictureString, pendingIntentFlag, onGoing);
    }

    public void cancelNotification(int notificaitonId){
        myAppsNotificationManager.cancelNotification(notificaitonId);
    }

    public boolean isNetworkConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        System.out.println("====");
        System.out.println(cm == null);
        System.out.println(cm.getActiveNetworkInfo() == null);
        if (cm != null && cm.getActiveNetworkInfo() != null) {
            return true;
        }
        if (isNetworkDialogActive) return false;
        Toast.makeText(context,"دستگاه شما به شبکه ای متصل نیست", Toast.LENGTH_SHORT).show();
        NetworkDialog networkDialog = new NetworkDialog(context);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder
                .setMessage("اینترنت خود را فعال نمایید")
                .setPositiveButton("wifi", networkDialog)
                .setNegativeButton("data", networkDialog)
                .setCancelable(false)
                .show();
        isNetworkDialogActive = true;
//            connectionDialog(context);
        return false;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.toString().equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private void connectionDialog(final Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Wifi Settings");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to enable WIFI ?")
                .setCancelable(false)
                .setPositiveButton("wifi",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //enable wifi
                        context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
//                        wifiMan.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("data",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //disable wifi
//                        wifiMan.setWifiEnabled(false);

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
