package com.example.panshippingandroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.fragments.FirstFragment;
import com.example.panshippingandroid.fragments.LoginFragment;
import com.example.panshippingandroid.fragments.RegisterFragment;
import com.example.panshippingandroid.fragments.SecondFragment;
import com.example.panshippingandroid.fragments.ThirdFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private final long BACK_PRESSED_INTERVAL = 2000;
    private long backPressedTime;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.first_fragment:
                    replaceFragment( FirstFragment.newInstance(), FirstFragment.TAG );
                    return true;
                case R.id.second_fragment:
                    replaceFragment( SecondFragment.newInstance(), SecondFragment.TAG );
                    return true;
                case R.id.third_fragment:
                    replaceFragment( ThirdFragment.newInstance(), ThirdFragment.TAG );
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById( R.id.bottom_navigation );
        navigation.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );
        navigation.setSelectedItemId( 0 );
        replaceFragment( FirstFragment.newInstance(), FirstFragment.TAG );

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + BACK_PRESSED_INTERVAL > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText( getBaseContext(), R.string.press_back_again, Toast.LENGTH_SHORT ).show();
        backPressedTime = System.currentTimeMillis();
    }

    private void replaceFragment(Fragment fragment, String TAG) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.fragment_container, fragment, TAG )
                .commit();
    }
}