package com.example.robien.beachbuddy;

/**
 * Created by Robien on 5/4/2016.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FifthFragment extends Fragment {

    /**
     * fields
     */
    private static FifthFragment instance = null;

    /**
     * Create fragment view when paginated.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fifth_frag_layout, container, false);

        TextView textView = (TextView) v.findViewById(R.id.tvFragFifth);
        textView.setText(getArguments().getString("msg"));

        return v;
    }

    /**
     * Returns new instance.
     *
     * @param text
     * @return
     */
    public static FifthFragment newInstance(String text) {

        if (instance == null) {
            // new instance
            instance = new FifthFragment();

            // sets data to bundle
            Bundle bundle = new Bundle();
            bundle.putString("msg", text);

            // set data to fragment
            instance.setArguments(bundle);

            return instance;
        } else {
            return instance;
        }
    }
}

