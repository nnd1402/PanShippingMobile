package com.example.panshippingandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.add(R.id.fragment_container, new LoginFragment());
        fr.commit();
    }
}