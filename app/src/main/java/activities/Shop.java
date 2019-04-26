package activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//import com.gigamole.infinitecycleviewpager.VerticalViewPager;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
//import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
//import com.zarinpal.ewallets.purchase.PaymentRequest;
//import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import helper.AsyncRequest;
import helper.PreferenceManager;
import masterpiece.wing.R;
import model.PreferenceModel;
import model.Profile;
import ui.MyColor;

//public class Shop extends AppCompatActivity {

public class Shop extends FragmentActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    WebView mWebview;
    private PreferenceManager preferenceManager;
    private PreferenceModel preferenceModel;
    private Profile profile;
    private EditText amount;
    ScrollView scrollView;
    RadioGroup radioGroup;
//    WheelPicker wheelPicker;

    String priceString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_shop);

//        init();

//        openInWebView();
//        openInBrowser();

        if (getIntent().getData() != null) {

            System.out.println("debug found intent");

//            ZarinPal.getPurchase(this).verificationPayment(getIntent().getData(), new OnCallbackVerificationPaymentListener() {
//                @Override
//                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
//                    Log.i("TAG", "onCallbackResultVerificationPayment: " + refID);
//                    System.out.println("success: " + isPaymentSuccess);
//                }
//            });
        }

    }

//    private void init() {
//        amount = (EditText) findViewById(R.id.amount);
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
////        scrollView.onscrol
//        preferenceManager = new PreferenceManager(this);
//        preferenceModel = preferenceManager.getPreferenceModel();
//        profile = preferenceManager.getProfile();
//
//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
//        radioGroup.check(radioGroup.getChildAt(1).getId());
//
////        FrameLayout flContainer = (FrameLayout) findViewById(R.id.container);
////        WheelPicker wheelPicker = new WheelPicker(this);
////        FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams
////                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        flParams.gravity = Gravity.CENTER;
////        flContainer.addView(wheelPicker, flParams);
//        wheelPicker = (WheelPicker) findViewById(R.id.wheelPicker);
//        List<String> prices = new ArrayList<>();
//        prices.add("5,000");
//        prices.add("10,000");
//        prices.add("20,000");
//        prices.add("50,000");
//        prices.add("100,000");
//        wheelPicker.setData(prices);
////        wheelPicker.setSelectedItemPosition(1);
//        wheelPicker.setSelectedItemTextColor(MyColor.SECONDARY);
//        wheelPicker.setCurtainColor(MyColor.DKGRAY);
//        wheelPicker.setIndicatorColor(MyColor.GRAY);
//        wheelPicker.setVerticalFadingEdgeEnabled(true);
//        wheelPicker.setFadingEdgeLength(30);
//        wheelPicker.setVisibleItemCount(3);
////        wheelPicker.setBackgroundColor(MyColor.GREEN);
//        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(WheelPicker picker, Object data, int position) {
//                priceString = (String) data;
//            }
//        });
//
//        final VerticalViewPager infiniteCycleViewPager = (VerticalViewPager) findViewById(R.id.vicvp);
////        final VerticalInfiniteCycleViewPager infiniteCycleViewPager = (VerticalInfiniteCycleViewPager) findViewById(R.id.vicvp);
////        final HorizontalInfiniteCycleViewPager infiniteCycleViewPager =
////                (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.hicvp);
//        class MyPagerAdapter extends PagerAdapter {
//
//            @Override
//            public int getCount() {
//                return 4;
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object o) {
//                return o==view;
//            }
//
//            @Override
//            public Object instantiateItem(final ViewGroup container, int position) {
//                Button button = new Button(container.getContext());
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                button.setLayoutParams(params);
//
//                LayoutInflater li = LayoutInflater.from(getApplicationContext());
//                LinearLayout linearLayout = (LinearLayout) li.inflate(R.layout.price,null,false);
//
//                String text = "انتخاب کنید";
//                switch (position){
//                    case 0:
//                        text = "5,000";
//                        break;
//                    case 1:
//                        text = "10,000";
//                        break;
//                    case 2:
//                        text = "20,000";
//                        break;
//                    case 3:
//                        text = "50,000";
//                        break;
//                    default:
//                        text = "50,000";
//                        break;
//                }
//                button.setText(text);
//                ((TextView)linearLayout.getChildAt(0)).setText(text);
//
//                LinearLayout layout = new LinearLayout(container.getContext());
//                layout.setOrientation(LinearLayout.VERTICAL);
//                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//                //add buton to layout
//                layout.addView(button);
//
//                final int page = position;
//                button.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(container.getContext(), "You clicked: " + page + ". page.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                //to container add layout instead of button
//                container.addView(layout);
//                //return layout instead of button
//
////                return button;
//                return layout;
////                return linearLayout;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                //cast to LinearLayout
//                container.removeView((LinearLayout)object);
//            }
//        }
//        infiniteCycleViewPager.setAdapter(new MyPagerAdapter());
////        infiniteCycleViewPager.setScrollDuration(500);
////        infiniteCycleViewPager.setInterpolator(...);
////        infiniteCycleViewPager.setMediumScaled(true);
////        infiniteCycleViewPager.setMaxPageScale(0.8F);
////        infiniteCycleViewPager.setMinPageScale(0.5F);
////        infiniteCycleViewPager.setCenterPageScaleOffset(30.0F);
////        infiniteCycleViewPager.setMinPageScaleOffset(5.0F);
////        infiniteCycleViewPager.setOnInfiniteCyclePageTransformListener(...);
//
////        If you want to get item position just call this method:
////        infiniteCycleViewPager.getRealItem();
////        To update your ViewPager after some adapter update or else, you can call this method:
////        infiniteCycleViewPager.notifyDataSetChanged();
////        If you want to start auto scroll or stop call this methods:
////        true - positive
////        false - negative
////        infiniteCycleViewPager.startAutoScroll(...);
////        infiniteCycleViewPager.stopAutoScroll();
//    }

    private void openInBrowser(int amount, String phone) {
        String uriString = String.format(AsyncRequest.BASE_URL, "zarinpal/request/?amount=",amount,"&phone=",phone);
//        Uri uri = Uri.parse(uriString);
        Uri uri = Uri.parse(AsyncRequest.BASE_URL + "zarinpal/request/?amount=" + amount + "&phone=" + phone);
//        Uri uri = Uri.parse("return://zarinpalpayment/");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void zarinpalAndroid(){
//        PaymentRequest payment = ZarinPal.getPaymentRequest();
//
//        payment.setMerchantID("8f1d98e6-f9e5-11e8-868c-005056a205be");
////        payment.setMerchantID("9cb9c630-8cbe-11e8-9869-005056a205be"); hardtech
//        payment.setAmount(100);
//        payment.setDescription("بال هایت را شارژ کن");
////        payment.setCallbackURL("app://app");
//        payment.setCallbackURL("wing://payment/");
//        payment.setMobile("09367498998");
//        payment.setEmail("hfarahani1374@gmail.com");
//
//
//        ZarinPal.getPurchase(getApplicationContext()).startPayment(payment, new OnCallbackRequestPaymentListener() {
//            @Override
//            public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {
//
//                startActivity(intent);
//            }
//        });
    }

    private void openInWebView(){
        mWebview  = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebview.loadUrl(AsyncRequest.BASE_URL + "zarinpal/request/");
        setContentView(mWebview );
    }

    public void goForPurchase(View view){
//        if (!amount.getText().toString().contentEquals(""))
//            openInBrowser(Integer.parseInt(amount.getText().toString()), preferenceModel.getPhone());
//        int price = Integer.parseInt(((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
        System.out.println("DEBUG");
//        System.out.println(wheelPicker.getData().get(wheelPicker.getSelectedItemPosition()));
//        String priceString = (String) wheelPicker.getData().get(wheelPicker.getSelectedItemPosition());
        priceString = priceString.replace(",", "");
        System.out.println(priceString);
        int price = Integer.parseInt(priceString);
        System.out.println(price);
        openInBrowser(price, preferenceModel.getPhone());
        finish();
    }
}
