package activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import adapters.TutorialAdapter;
import masterpiece.wing.R;
import ui.MyColor;

public class Tutorial extends AppCompatActivity {

    LinearLayout dotsLayout;
    TutorialAdapter tutorialAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        dotsLayout = findViewById(R.id.layoutDots);

        ViewPager viewPager = findViewById(R.id.view_pager);
        tutorialAdapter = new TutorialAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tutorialAdapter);
        addBottomDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                addBottomDots(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void backToMain(View view){
        this.finish();
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[tutorialAdapter.getCount()];

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

}
