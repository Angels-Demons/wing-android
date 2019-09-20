package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.TutorialFrag;
import masterpiece.wing.R;

public class TutorialAdapter extends FragmentPagerAdapter {

    public TutorialAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TutorialFrag frag = new TutorialFrag();
        Bundle bundle = new Bundle();
        switch (position)
        {
            case 0:
                bundle.putInt("layout", R.layout.fragment_tutorial_0);
                break;
            case 1:
                bundle.putInt("layout", R.layout.fragment_tutorial_1);
                break;
            case 2:
                bundle.putInt("layout", R.layout.fragment_tutorial_2);
                break;
            case 3:
                bundle.putInt("layout", R.layout.fragment_tutorial_3);
                break;
            case 4:
                bundle.putInt("layout", R.layout.fragment_tutorial_4);
                break;
            case 5:
                bundle.putInt("layout", R.layout.fragment_tutorial_5);
                break;
            default:
                break;
        }
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return 6; //six fragments
    }
}