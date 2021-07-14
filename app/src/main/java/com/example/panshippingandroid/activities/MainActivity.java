package com.example.panshippingandroid.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.fragments.FirstFragment;
import com.example.panshippingandroid.fragments.SecondFragment;
import com.example.panshippingandroid.fragments.ThirdFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.first_fragment:
                replaceFragment(FirstFragment.newInstance(), FirstFragment.TAG);
                return true;
            case R.id.second_fragment:
                replaceFragment(SecondFragment.newInstance(), SecondFragment.TAG);
                return true;
            case R.id.third_fragment:
                replaceFragment(ThirdFragment.newInstance(), ThirdFragment.TAG);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(0);
        replaceFragment(FirstFragment.newInstance(), FirstFragment.TAG);
    }

    private void replaceFragment(Fragment fragment, String TAG) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }
}