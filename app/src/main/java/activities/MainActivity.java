package activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import application.MyApplication;
import dialogs.PurchaseDialog;
import enums.StateMainActivity;
import helper.PreferenceManager;
import helper.RideVerifier;
import helper.WebService;
import masterpiece.wing.R;
import me.anwarshahriar.calligrapher.Calligrapher;
import model.PreferenceModel;
import model.Profile;
import model.Scooter;
import model.User;

import static application.MyApplication.DEFAULT_ZOOM;
import static application.MyApplication.ICON_HEIGHT;
import static application.MyApplication.ICON_SCALE;
import static application.MyApplication.ICON_WIDTH;

//public class MainActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
public class MainActivity extends FragmentActivity
        implements
        OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private FirebaseAnalytics mFirebaseAnalytics;

    public static boolean DEBUG = true;

    private static final int LOCATION_PADDING = 0;
    private static final int CAMPUS_PADDING = 300;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 20;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 80;

    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    private GoogleMap mMap;
    public List<Scooter> scooters;
    public Profile profile;

    TextView header, credit, timer;
    LinearLayout statBar, tutButton;
    TextView action;
    ImageButton backButton;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Scooter selectedScooter;
    Marker selectedScooterMarker;

    private IntentIntegrator qrScan;

    public String qrInfo;

    public PreferenceManager preferenceManager;
    public PreferenceModel preferenceModel;

    LinearLayout headerBar;

    SupportMapFragment mapFragment;

    GoogleApiClient googleApiClient;

    private Location myLocation;

    LinearLayout start_ride_loader;

    Thread rideVerifierThread;

    public static int RETRY_TIME = 2500;

    ProgressDialog progressDialog;

    LinearLayout progressLayout;

    public static final float selectZoom = 20;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    qrScan.initiateScan();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "برای روشن کردن دستگاه باید توسط دوربین کد را اسکن کنید", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToMyLocation();
                    getMyLocation();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(getApplicationContext(), "برای مشاهده موقعیت خود باید موقعیت یاب را فعال کنید", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "بارکدی پیدا نشد", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                qrScanned(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebService.myProfile(this, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        init();
        setUpGClient();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (preferenceModel.getState() == StateMainActivity.A_SCOOTER_IS_SELECTED) {
                setState(StateMainActivity.NOTHING_IS_SELECTED);
                selectedScooterMarker.hideInfoWindow();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_purchase:
                PurchaseDialog purchaseDialog = new PurchaseDialog();
                Bundle bundle = new Bundle();
                bundle.putString("phone", preferenceModel.getPhone());
                purchaseDialog.setArguments(bundle);
                purchaseDialog.show(getFragmentManager(), "افزایش اعتبار");
                break;

            case R.id.nav_logout:
                YesNoDialog yesNoDialog = new YesNoDialog(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setMessage("آیا از خروچ خود مطمئن هستید؟")
                        .setPositiveButton("بله", yesNoDialog)
                        .setNegativeButton("خیر", yesNoDialog)
                        .show();
                break;
//            case R.id.nav_ride_history:
////                startActivity(new Intent(getApplicationContext(), Shop.class));
//
//                break;
            case R.id.nav_tutorial:
                startActivity(new Intent(getApplicationContext(), Tutorial.class));
                break;
//            case R.id.nav_become_charger:
//
//                break;
//            case R.id.nav_help:
//
//                break;
//            case R.id.nav_safety:
//
//                break;
//            case R.id.nav_settings:
//
//                break;
//            case R.id.nav_share:
//
//                break;
//            case R.id.enter_code:
//                WebService.startRide(this, 132453);
//                break;
//            default:
//                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.clear();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
                selectedScooterMarker = marker;
//                don't zoom back if user has zoomed in
                if (mMap.getCameraPosition().zoom < selectZoom)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), selectZoom));
                else
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), mMap.getCameraPosition().zoom));
                for (Scooter scooter : scooters) {
                    if (Integer.parseInt(marker.getSnippet()) == scooter.getDevice_code()) {
                        selectScooter(scooter);
//                        System.out.println("DEBUG found");
                        return true;
                    }
                }
//                System.out.println("DEBUG not");
                return false;
            }
        });

        goToMyLocation();

//        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//                loadScooters();
//            }
//        });


    }

    private void goToMyLocation(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_CAMERA is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        } else {
            // Permission has already been granted
            // TODO: Before enabling the My Location layer, you must request
            // location permission from the user. This sample does not include
            // a request for location permission.

//            check if GPS is on
            if (true){
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
//                    Toast.makeText(getApplicationContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
                        // Return false so that we don't consume the event and the default behavior still occurs
                        // (the camera animates to the user's current position).
                        return false;
                    }
                });
//            mMap.setOnMyLocationClickListener(new OnMy);
            }
            else {
//                request to turn on the GPS

            }

//
        }
    }

    public void loadMap() throws NullPointerException {
        //        showUserOnMap();
        mMap.clear();
        if (scooters.size() > 0) {
            for (Scooter scooter : scooters) {
                System.out.println("=== adding marker " + scooter.getDevice_code());
                mMap.addMarker(new MarkerOptions()
                        .position(scooter.getLatLng())
                        .title("دستگاه انتخاب شده")
                        .icon(scooter.getIcon(getApplicationContext(),
                                ICON_WIDTH*ICON_SCALE,ICON_HEIGHT*ICON_SCALE))
                        .snippet(String.valueOf(scooter.getDevice_code()))
                );
            }
            if (preferenceModel.getState() == StateMainActivity.RIDE_STARTED_AND_VERIFIED){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(scooters.get(0).getLatLng(), DEFAULT_ZOOM));
            }
            else {
                //                animate camera to current position
                goToMyLocation();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
                        DEFAULT_ZOOM));
            }
        } else {
            //                animate camera to current position
            goToMyLocation();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLocation.getLatitude(), myLocation.getLongitude()),
                    DEFAULT_ZOOM));
            Toast.makeText(getApplicationContext(), "هیچ وسیله ای در نزدیکی شما یافت نشد", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadScooters() {
        scooters = new ArrayList<>();
        WebService.nearbyDevices(this);
    }

    private void showUserOnMap() {
        User user = new User(preferenceModel.getPhone(), 1000, 35.710276, 51.345876);
        mMap.addMarker(new MarkerOptions()
                .position(user.getLatLng())
                .icon(user.getIcon())
        );
    }

    private void init() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "font.ttf", true);

//        modify ? maybe move to on_create
        preferenceManager = new PreferenceManager(this);
        preferenceModel = preferenceManager.getPreferenceModel();

//        modify
        WebService.myProfile(this, false);
        profile = preferenceManager.getProfile();

//        preferenceModel = preferenceModel.getPreferenceModel();

        qrScan = new IntentIntegrator(this);

        header = (TextView) findViewById(R.id.header);
        credit = (TextView) findViewById(R.id.credit);
        timer = (TextView) findViewById(R.id.timer);

        statBar = (LinearLayout) findViewById(R.id.stat_bar);
        tutButton = findViewById(R.id.tut_button);

        action = (TextView) findViewById(R.id.action);

        backButton = (ImageButton) findViewById(R.id.backButton);
//        loadScooters();
//        setPreferenceModel(0);
//        if (preferenceModel.getRide_id() != null && !preferenceModel.getRide_id().equals("")) {
//            setPreferenceModel(2);
//        }
//        try {
//        }catch (Resources.NotFoundException e){
//            if (DEBUG) System.out.println("onCreate->init() is called before onMapReady is finished");
//            e.printStackTrace();
//        }
        //        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        modify : not compatible with fragment activity : appcompat
//        setSupportActionBar(toolbar);


        //        modify
        //        my location button

        headerBar = (LinearLayout) findViewById(R.id.header_bar);
        ViewTreeObserver observer = headerBar.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                init2();
                headerBar.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });


//    callfragment();
//}

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        start_ride_loader = findViewById(R.id.start_ride_loader);
        progressLayout = findViewById(R.id.progress_layout);
        progressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"لطفا منتظر بمانید", Toast.LENGTH_SHORT).show();
            }
        });
        startTimer();
    }

    protected void init2() {
        int a = headerBar.getHeight();
//        int b = headerBar.getWidth();
//        Toast.makeText(getActivity,""+a+" "+b,3000).show();
        System.out.println("DEBUG:" + a);

//        modify
        try{
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        rlp.setMargins(0, a/4, 0, 0);
            rlp.setMargins(0, 0, 0, 0);
            mMap.setPadding(0, a, 0, 0);
        }catch (NullPointerException e){
            System.out.println("DEBUG: can't put margin for location button");
            e.printStackTrace();
        }


    }

    private void selectScooter(Scooter scooter) {
        selectedScooter = scooter;
        setState(StateMainActivity.A_SCOOTER_IS_SELECTED);
    }

    public void actionBarClicked(View view) {
        if (preferenceModel.getState() == StateMainActivity.NOTHING_IS_SELECTED || preferenceModel.getState() == StateMainActivity.A_SCOOTER_IS_SELECTED) {
            if (DEBUG) System.out.println("DEBUG: state 0 or 1");
//            modify
//            scan QR here
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (DEBUG) System.out.println("DEBUG: you dont have permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
                // Permission is not granted
                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA)) {
//                    // Show an explanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//                } else {
//                    // No explanation needed; request the permission
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//
//                    // MY_PERMISSIONS_REQUEST_CAMERA is an
//                    // app-defined int constant. The callback method gets the
//                    // result of the request.
//                }
            } else {
                // Permission has already been granted
                if (Integer.parseInt(profile.getCredit()) < 1000){
                    Toast.makeText(getApplicationContext(),"اعتبار زیر 1000 تومان است!", Toast.LENGTH_SHORT).show();
                }
                else {
                    qrScan.initiateScan();
                }
//
            }


        } else if (preferenceModel.getState() == StateMainActivity.RIDE_STARTED_AND_VERIFIED) {
            WebService.endRide(this);
        }
    }

    public void menuClicked(View view) {
        drawerLayout.openDrawer(Gravity.END);

    }

    public void backClicked(View view) {
        onBackPressed();
    }

    public void setState(StateMainActivity state) {
        System.out.println("request to set the state to " + state + " from " + preferenceModel.getState());
        switch (state) {
//            nothing selected
            case NOTHING_IS_SELECTED:
                loadScooters();
                System.out.println("size: " + scooters.size());
//                loadMap();
                header.setText("درخواست اسکوتر");
                credit.setText("اعتبار: " + profile.getCredit() + " تومان");
                timer.setText("");
                statBar.setVisibility(View.GONE);
                tutButton.setVisibility(View.VISIBLE);
                action.setText("شروع سفر");
                backButton.setVisibility(View.GONE);
                start_ride_loader.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
                break;

//            a scooter is selected
            case A_SCOOTER_IS_SELECTED:
                tutButton.setVisibility(View.GONE);
                if (selectedScooter != null) {
                    int battery = selectedScooter.getBattery();
                    System.out.println("DEBUG: " + selectedScooter.toString());
                    header.setText("درخواست اسکوتر");
                    credit.setText("اعتبار: " + profile.getCredit() + " تومان");
                    timer.setText("");
                    String deviceCode = selectedScooter.getDevice_code() + "\nپلاک";
                    String charge = battery + "%\nشارژ";
                    String status = selectedScooter.getStatusString() + "\nوضعیت";
                    ((TextView) statBar.getChildAt(0)).setText(deviceCode);
                    ((TextView) statBar.getChildAt(1)).setText(charge);
                    ((TextView) statBar.getChildAt(2)).setText(status);
                    statBar.setVisibility(View.VISIBLE);
                    action.setText("شروع سفر");
                    backButton.setVisibility(View.VISIBLE);
                    start_ride_loader.setVisibility(View.GONE);
                    progressLayout.setVisibility(View.GONE);
                } else {
                    System.out.println("state was changed to zero because selected scooter was null");
                    setState(StateMainActivity.NOTHING_IS_SELECTED);
                    return;
                }
                break;


//            scooter is scanned successfully (if not stay in 1)
//            timer is running
            case RIDE_STARTED_NOT_VERIFIED:
                tutButton.setVisibility(View.GONE);
                int battery = profile.getBattery();

                header.setText("در حال سفر");
                credit.setText("اعتبار: " + profile.getCredit() + " تومان");
//                timer.setText(profile.getTimer());

                String distance = battery * 30 / 100 + "\nکیلومتر";
                String charge = battery + "%\nشارژ";
                String endurance = battery * 90 / 100 + "\nدقیقه";

                ((TextView) statBar.getChildAt(0)).setText(distance);
                ((TextView) statBar.getChildAt(1)).setText(charge);
                ((TextView) statBar.getChildAt(2)).setText(endurance);

                statBar.setVisibility(View.VISIBLE);
                action.setText("پایان سفر");
                backButton.setVisibility(View.GONE);
//                block UI thread with progress bar
                progressLayout.setVisibility(View.VISIBLE);
//                progressDialog = new ProgressDialog(this);
//                progressDialog.setMessage("در حال روشن کردن دستگاه");
//                progressDialog.show();

//                start_ride_loader.setVisibility(View.VISIBLE);

//                here is the loop
                if (rideVerifierThread == null) {
                    rideVerifierThread = new Thread(new RideVerifier(this, RETRY_TIME));
                    rideVerifierThread.start();
                }
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions()
//                        .position(selectedScooter.getLatLng())
//                        .title("دستگاه انتخاب شده")
//                        .icon(selectedScooter.getIcon())
//                        .snippet(String.valueOf(selectedScooter.getDevice_code()))
//                );
                break;

            case RIDE_STARTED_AND_VERIFIED:
                tutButton.setVisibility(View.GONE);
                loadScooters();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(scooters.get(0).getLatLng(), DEFAULT_ZOOM));
                if (progressDialog != null) progressDialog.dismiss();
                progressLayout.setVisibility(View.GONE);
                battery = profile.getBattery();

//                modify : get real battery

                header.setText("در حال سفر");
                credit.setText("اعتبار: " + profile.getCredit() + " تومان");
//                timer.setText(profile.getTimer());

                distance = battery * 30 / 100 + "\nکیلومتر";
                charge = battery + "%\nشارژ";
                endurance = battery * 90 / 100 + "\nدقیقه";

                ((TextView) statBar.getChildAt(0)).setText(distance);
                ((TextView) statBar.getChildAt(1)).setText(charge);
                ((TextView) statBar.getChildAt(2)).setText(endurance);

                statBar.setVisibility(View.VISIBLE);
                action.setText("پایان سفر");
                backButton.setVisibility(View.GONE);
                start_ride_loader.setVisibility(View.GONE);
                break;
            default:
                break;
        }

//        WebService.myProfile(this);
        if (state == preferenceModel.getState() && state == StateMainActivity.RIDE_STARTED_NOT_VERIFIED){

        }
        else {
            updateNotification(
                    state,
//                this flag determines that if the method is invoked from endRide webservice
//                (if former state is RIDE_STARTED_AND_VERIFIED
//                and current state is NOTHING_IS_SELECTED
                    this.preferenceModel.getState()==StateMainActivity.RIDE_STARTED_AND_VERIFIED
                            && state == StateMainActivity.NOTHING_IS_SELECTED);
            this.preferenceModel.setState(state, preferenceManager);
        }

        System.out.println("state was changed to " + preferenceModel.getState());
    }

    public void tutorialButtonClicked(View view){
        startActivity(new Intent(getApplicationContext(), Tutorial.class));
    }

    private void qrScanned(String qrInfo) {
        this.qrInfo = qrInfo;
//        modify get device code from get parameters
//        String deviceCode = qrInfo.split("scooterid")[1];
        WebService.startRide(this, 0);
    }

    private void startTimer() {
        final int[] nSeconds = {0};
        final Handler handler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (profile.isIs_riding() && profile.isRide_is_verified()){
                    nSeconds[0]++;
                    timer.setText(profile.getTimer(nSeconds[0]));

                }
                handler.postDelayed(this, 1000);

            }
        };
        handler.removeCallbacks(task);
        handler.post(task);

    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
        if (myLocation != null) {
            Double latitude= myLocation.getLatitude();
            Double longitude= myLocation.getLongitude();
            if (DEBUG){
                System.out.println("DEBUG");
                System.out.println(latitude);
                System.out.println(longitude);
            }

//            latitudeTextView.setText("Latitude : "+latitude);
//            longitudeTextView.setText("Longitude : "+longitude);
            //Or Do whatever you want with your location
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    myLocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    myLocation = location;
                                    if (myLocation != null) {
                                        Double latitude= myLocation.getLatitude();
                                        Double longitude= myLocation.getLongitude();
//            latitudeTextView.setText("Latitude : "+latitude);
//            longitudeTextView.setText("Longitude : "+longitude);
                                        //Or Do whatever you want with your location
                                    }
                                }
                            });
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(MainActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        myLocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(MainActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }


    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

    private class YesNoDialog implements DialogInterface.OnClickListener{

        Activity activity;

        YesNoDialog(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    preferenceModel.setLoggedIn(false, preferenceManager);
                    preferenceModel.setPhone(null, preferenceManager);
                    preferenceModel.setState(StateMainActivity.NOTHING_IS_SELECTED, preferenceManager);
                    preferenceModel.setToken(null, preferenceManager);

                    startActivity(new Intent(getApplicationContext(), Register.class));
                    activity.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    }

    public void trigger(View view){
        ((MyApplication)getApplication()).triggerNotificationWithBackStack(MainActivity.class,
                MyApplication.NEWS_CHANNEL_ID,
                "aaaaa",
                "bbb",
                "cccccccc",
                NotificationCompat.PRIORITY_HIGH,
                true,
                MyApplication.notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void trigger(String title, String text, String bigText){
        ((MyApplication)getApplication()).triggerNotificationWithBackStack(MainActivity.class,
                MyApplication.NEWS_CHANNEL_ID,
                title,
                text,
                bigText,
                NotificationCompat.PRIORITY_HIGH,
                true,
                MyApplication.notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void updateNotification(StateMainActivity state, boolean endRide){
        if (endRide){
            update("سفر به پایان رسید",
                    "امیدواریم سفر خوبی را با وینگ تجربه کرده باشید",
                    "برای آغاز سفر جدید این پیغام بزنید",
                    false);
        }
        else {
            switch (state){
                case RIDE_STARTED_NOT_VERIFIED:
                    trigger("بررسی درخواست سفر",
                            "وینگ در حال روشن کردن دستگاه مورد نظر شماست",
                            "برای آگاهی از وضعیت سفر یا پایان سفر روی این پیغام بزنید");
                    break;
                case RIDE_STARTED_AND_VERIFIED:
                    update("در حال سفر",
                            "از سفر خود با وینگ لذت ببرید",
                            "برای آگاهی از وضعیت سفر یا پایان سفر روی این پیغام بزنید",
                            true);
                    break;
                case NOTHING_IS_SELECTED:
                    cancel(null);
                    break;
                case A_SCOOTER_IS_SELECTED:
                    cancel(null);
                    break;
                default:
                    break;

            }
        }
    }

    public void cancel(View view){
        ((MyApplication)getApplication()).cancelNotification(MyApplication.notificationId);
    }

    public void update(View view){
        ((MyApplication)getApplication()).updateNotification(MainActivity.class,
                "در حال سفر",
                "This is updatedNotification",
                MyApplication.NEWS_CHANNEL_ID,
                MyApplication.notificationId,
                "This is a updated information for bigpicture String",
                PendingIntent.FLAG_UPDATE_CURRENT,
                true);
    }

    public void update(String title, String text, String bigText, boolean onGoing){
        ((MyApplication)getApplication()).updateNotification(MainActivity.class,
                title,
                text,
                MyApplication.NEWS_CHANNEL_ID,
                MyApplication.notificationId,
                bigText,
                PendingIntent.FLAG_UPDATE_CURRENT,
                onGoing);
    }

    public Location getMyLocation(String n){
        return myLocation;
    }
}
