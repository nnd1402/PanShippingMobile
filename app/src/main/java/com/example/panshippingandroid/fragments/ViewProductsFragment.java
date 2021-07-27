package com.example.panshippingandroid.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.adapters.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ViewProductsFragment extends Fragment {

    public static final String TAG = "View products fragment";
    private ViewPager2 viewPager;
    TabLayout tabLayout;
    private FloatingActionButton addItemButton;
    private ViewPagerAdapter viewPagerAdapter;

    public static ViewProductsFragment newInstance() {
        ViewProductsFragment fragment = new ViewProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        tabLayout = requireView().findViewById(R.id.tabLayout);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideFAB(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayout.setSmoothScrollingEnabled(true);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    List<String> tabNames = new ArrayList<>();
                    tabNames.add("My products");
                    tabNames.add("All products");
                    tab.setText(tabNames.get(position));
                }
        ).attach();
        addItemButton.setOnClickListener(v -> {
            FragmentTransaction fr = getParentFragmentManager().beginTransaction();
            fr.addToBackStack(null);
            fr.replace(R.id.container, AddProductFragment.newInstance());
            fr.commit();
        });

    }

    private void hideFAB(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            addItemButton.setVisibility(View.VISIBLE);
        } else {
            addItemButton.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        viewPagerAdapter = new ViewPagerAdapter(requireActivity());
        viewPager = requireView().findViewById(R.id.viewPager);
        addItemButton = requireView().findViewById(R.id.btn_addNewProd);
    }
}