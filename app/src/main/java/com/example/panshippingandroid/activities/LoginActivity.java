package com.example.panshippingandroid.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.api.APIService;
import com.example.panshippingandroid.api.Service;
import com.example.panshippingandroid.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    public static APIService apiService;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiService = Service.getInstance(this).create(APIService.class);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, LoginFragment.newInstance());
        fragmentTransaction.commit();
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