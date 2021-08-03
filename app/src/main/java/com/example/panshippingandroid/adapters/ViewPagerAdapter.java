package com.example.panshippingandroid.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.panshippingandroid.fragments.AllProductsFragment;
import com.example.panshippingandroid.fragments.MyProductsFragment;


import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        AllProductsFragment allProductsFragment = new AllProductsFragment();
        fragments.add(allProductsFragment);
        MyProductsFragment myProductsFragment = new MyProductsFragment();
        fragments.add(myProductsFragment);
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
