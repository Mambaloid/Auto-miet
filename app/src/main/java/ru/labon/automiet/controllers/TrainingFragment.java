package ru.labon.automiet.controllers;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.labon.automiet.R;
import ru.labon.automiet.adapters.ViewPagerAdapter;

/**
 * Created by HP on 02.10.2017.
 */

public class TrainingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.training_fragment, container, false);
        ViewPager viewPager = view.findViewById(R.id.viewpager_training_fragment);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout_training_fragment);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MaterialFragment(), getString(R.string.materials));
        adapter.addFragment(new TestingFragment(), getString(R.string.testing));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TRAIN","RESUME");

    }
}
