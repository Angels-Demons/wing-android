package helper;

import java.util.TreeMap;

import activities.MainActivity;
import enums.StateMainActivity;

public class RideVerifier implements Runnable {
    private static int counter = 0;
    private static final int MAX_RETRIES = 20;
    private MainActivity mainActivity;
    private int retryTime;
    public RideVerifier(MainActivity mainActivity, int retryTime) {
        this.mainActivity = mainActivity;
        this.retryTime = retryTime;
        counter++;
        System.out.println("DEBUG: creating a new ride verifier " + counter);
    }

    @Override
    public void run() {
        while (!mainActivity.profile.isRide_is_verified()){
            WebService.myProfile(mainActivity, true);
            try {
                Thread.sleep(retryTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        for (int i=0; i < MAX_RETRIES && !mainActivity.profile.isRide_is_verified(); i++){
//            System.out.println("DEBUG: retry: " + i);
//            WebService.myProfile(mainActivity, true);
//            try {
//                Thread.sleep(retryTime);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
