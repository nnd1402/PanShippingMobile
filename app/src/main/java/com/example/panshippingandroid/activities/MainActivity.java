package com.example.panshippingandroid.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.api.APIService;
import com.example.panshippingandroid.api.Service;
import com.example.panshippingandroid.fragments.AddProductFragment;
import com.example.panshippingandroid.fragments.FirstFragment;
import com.example.panshippingandroid.fragments.SecondFragment;
import com.example.panshippingandroid.fragments.ThirdFragment;
import com.example.panshippingandroid.model.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    public static APIService apiService;

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.first_fragment:
                fragment = new FirstFragment();
                replaceFragment(fragment, FirstFragment.TAG);
                return true;
            case R.id.second_fragment:
                fragment = new SecondFragment();
                replaceFragment(fragment, SecondFragment.TAG);
                return true;
            case R.id.add_fragment:
                fragment = new AddProductFragment();
                replaceFragment(fragment, AddProductFragment.TAG);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = Service.getInstance(this).create(APIService.class);
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

    @Override
    public void onBackPressed() {

        int fragmentBackStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentBackStackCount > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.press_back_again, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        }
    }
}