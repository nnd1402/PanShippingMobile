package com.example.panshippingandroid.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.api.APIService;
import com.example.panshippingandroid.api.Service;
import com.example.panshippingandroid.fragments.AddProductFragment;
import com.example.panshippingandroid.fragments.ShippedProductFragment;
import com.example.panshippingandroid.fragments.ViewProductsFragment;
import com.example.panshippingandroid.utils.Const;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    public static APIService apiService;
    public Dialog dialog;

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    {
        mOnNavigationItemSelectedListener = item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.products:
                    fragment = new ViewProductsFragment();
                    replaceFragment(fragment, Const.TAG_VIEW);
                    return true;
                case R.id.buy_products:
                    fragment = new ShippedProductFragment();
                    replaceFragment(fragment, Const.TAG_SHIPPED);
                    return true;
                case R.id.add_products:
                    fragment = new AddProductFragment();
                    replaceFragment(fragment, Const.TAG_ADD);
                    return true;
            }
            return false;
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading_dialog();
        apiService = Service.getInstance(this).create(APIService.class);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(0);
        replaceFragment(ViewProductsFragment.newInstance(), Const.TAG_VIEW);
    }

    public void loading_dialog() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        final LayoutInflater inflaterLoading = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflaterLoading != null;
        final View viewLayoutLoading = inflaterLoading.inflate(R.layout.dialog_wait,
                findViewById(R.id.dialog_new_absence_wait_id), false);
        alertDialogBuilder.setView(viewLayoutLoading);
        dialog = alertDialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
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