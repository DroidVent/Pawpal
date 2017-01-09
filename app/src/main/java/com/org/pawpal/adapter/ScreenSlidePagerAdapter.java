package com.org.pawpal.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.org.pawpal.R;
import com.org.pawpal.fragments.ScreenSlidePageFragment;

/**
 * Created by hp-pc on 27-11-2016.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private final TypedArray iconArray;
    private String[] text = new String[4];

    public ScreenSlidePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        text[0] = "Matching dog owners with local hosts for walk, sitting and holiday care";
        text[1] = "Create a pawpal profile for yourself or your dog, upload some photos and add your availability";
        text[2] = "Find connect and meet with lovely doggies or borrowers in your local area";
        text[3] = "Subscribe to access security benefits like profile verification, third party insurance and a 24/7 Vet Line";
        iconArray = context.getResources().obtainTypedArray(R.array.icon_viewpager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return ScreenSlidePageFragment.newInstance(text[position],iconArray.getResourceId(position, 0));
            case 1: return ScreenSlidePageFragment.newInstance(text[position], iconArray.getResourceId(position, 0));
            case 2: return ScreenSlidePageFragment.newInstance(text[position], iconArray.getResourceId(position, 0));
            case 3: return ScreenSlidePageFragment.newInstance(text[position],iconArray.getResourceId(position, 0));
        }
      //  return  ScreenSlidePageFragment.newInstance(text[position]);
        return null;
    }

    @Override
    public int getCount() {
        return text.length;
    }

}
