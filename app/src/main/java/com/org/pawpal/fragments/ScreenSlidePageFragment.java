package com.org.pawpal.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.org.pawpal.R;
import com.org.pawpal.custom.CustomTextView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 27-11-2016.
 */
public class ScreenSlidePageFragment extends Fragment {
    private  int resource;
    private View view;
    private CustomTextView tvText;
    private String message;
    private CircleImageView ivSlider;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.slider_fragment, container, false);
        init();
        tvText.setText(getArguments().getString("msg"));
        if (resource != 0)
            Picasso.with(getContext()).load(resource).fit().centerCrop().into(ivSlider);
        return view;
    }

    private void init() {
        tvText = (CustomTextView)view.findViewById(R.id.tv_text);
        ivSlider = (CircleImageView) view.findViewById(R.id.iv_slider);
    }
    public static ScreenSlidePageFragment newInstance(String text, int icon) {

        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.resource = icon;
        f.setArguments(b);

        return f;
    }
}
