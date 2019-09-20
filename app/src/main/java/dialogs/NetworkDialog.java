package dialogs;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import application.MyApplication;

public class NetworkDialog implements DialogInterface.OnClickListener{

    private Context context;

    public NetworkDialog(Context context) {
        System.out.println(" ===== network dialog created");
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                //wifi button clicked
//                dialogInterface.cancel();
                context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //data button clicked
                Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
//                intent.setClassName("activities", "MainActivity");
//                ComponentName cName = new ComponentName("com.android.phone","com.android.phone.settings");
//                intent.setComponent(cName);
                context.startActivity(intent);
//                enableMobileData(context, true);
                break;
        }
        dialogInterface.cancel();
        ((MyApplication)((Activity)context).getApplication()).isNetworkDialogActive = false;
    }

    private void enableMobileData(Context context, boolean enabled) {
        try {
            final ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(cm.getClass().getName());
            final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
            connectivityManagerField.setAccessible(true);
            final Object connectivityManager = connectivityManagerField.get(cm);
            final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
