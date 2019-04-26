package activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import masterpiece.wing.R;
import me.anwarshahriar.calligrapher.Calligrapher;
import ui.MyColor;

public class Tutorial extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private ViewPager viewPager;
    private Tutorial.MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_tutorial);



        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        next = (Button) findViewById(R.id.next);

//        btnSkip = (Button) findViewById(R.id.btn_skip);
//        btnNext = (Button) findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more layouts if you want

//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout page = (LinearLayout) layoutInflater.inflate(R.layout.tutorial_layout, null, false);
//        LottieAnimationView lottieAnimationView = (LottieAnimationView) page.findViewById(R.id.animationView);
//        lottieAnimationView.setAnimation("motorcycle.json");
        layouts = new int[]{
                R.layout.tutorial_layout,
                R.layout.tutorial_layout,
                R.layout.tutorial_layout,
                R.layout.tutorial_layout,
                R.layout.tutorial_layout,
                R.layout.tutorial_layout};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new Tutorial.MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

//        btnSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(getApplicationContext(), Register.class));
//                finish();
//            }
//        });

//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // checking for last page
//                // if last page home screen will be launched
//                int current = getItem(+1);
//                if (current < layouts.length) {
//                    // move to next screen
//                    viewPager.setCurrentItem(current);
////                    Calligrapher calligrapher = new Calligrapher(getApplicationContext());
////                    calligrapher.setFont(getParent(),"font.ttf",true);
//                } else {
////                    startActivity(new Intent(getApplicationContext(), Register.class));
//                    finish();
//                }
//            }
//        });

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"font.ttf",true);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int colorActive = getResources().getColor(R.color.md_white_1000);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
//        int colorInactive = getResources().getColor(R.color.colorPrimaryDarkTransparent);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);
            dots[i].setPadding(0,0,0,0);
            dots[i].setTextColor(MyColor.WHITE_TRANSPARENT);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorActive);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                next.setText("بستن");
//                btnNext.setText("شروع");
//                btnSkip.setVisibility(View.INVISIBLE);
            } else {
                next.setText("بعدی");
                // still pages are left
//                btnNext.setText("بعدی");
//                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    /**
     * View pager adapter
     */
    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            View view = layoutInflater.inflate(layouts[position], container, false);

            RelativeLayout view = (RelativeLayout) layoutInflater.inflate(layouts[position], container, false);
            LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R.id.animationView);
            TextView title= (TextView) view.findViewById(R.id.title_tut);
            TextView info= (TextView) view.findViewById(R.id.info_tut);
            switch (position){
                case 0:
                    lottieAnimationView.setAnimation("motorcycle.json");
                    title.setText("موتور سیکلت");
                    info.setText("سواری کنید و لذت ببرید");
                    break;
                case 1:
                    lottieAnimationView.setAnimation("no_internet_connection.json");
                    title.setText("اتصال اینترنت");
                    info.setText("ارتباط شما با سرور قطع است");
                    break;
                case 2:
                    lottieAnimationView.setAnimation("rt_ending_your_ride.json");
                    title.setText("پایان سفر");
                    info.setText("اینطوری سفر خود را تمام کنید");
                    break;
                case 3:
//                    lottieAnimationView.setAnimation("rt_find_and_unlock.json");
                    lottieAnimationView.setAnimation("small_car.json");
                    title.setText("دست انداز");
                    info.setText("مواظب باش مهندس");
                    break;
                case 4:
                    lottieAnimationView.setAnimation("rt_find_and_unlock.json");
                    title.setText("قفل دستگاه را باز کنید");
                    info.setText("هزار بده فشار بده");
//                    lottieAnimationView.setAnimation("small_car.json");
                    break;
                default:
                    lottieAnimationView.setAnimation("fluid_loading_animation.json");
                    title.setText("سیالات کلک باز");
                    info.setText("ممنون از توجهی که به کلک دارید");
                    break;
            }
            lottieAnimationView.playAnimation();
            lottieAnimationView.loop(true);


            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void backToMain(View view){
        finish();
    }

    public void next(View view){
        int current = getItem(1);
        if (current < layouts.length){
            viewPager.setCurrentItem(current);
//            Calligrapher calligrapher = new Calligrapher(getApplicationContext());
//            calligrapher.setFont(getParent(),"font.ttf",true);
        } else {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}
