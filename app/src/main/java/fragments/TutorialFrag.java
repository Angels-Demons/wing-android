package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import masterpiece.wing.R;

public class TutorialFrag extends Fragment {
    int resource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle bundle = getArguments();
        try {
            int layout = bundle.getInt("layout");
            return inflater.inflate(layout, container, false);
        }catch (NullPointerException e){
//            e.printStackTrace();
            return null;
        }

    }
}