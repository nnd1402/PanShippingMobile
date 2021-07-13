package com.example.panshippingandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.panshippingandroid.activities.MainActivity;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.api.APIService;
import com.example.panshippingandroid.api.Service;


public class LoginFragment extends Fragment {

    private EditText et_username;
    private EditText et_password;

    private Button login_btn;
    private View rootView;

    boolean isAllFieldsChecked = false;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();

    }

    private void initUI() {

        et_username = rootView.findViewById(R.id.et_username);
        et_password = rootView.findViewById(R.id.et_password);

        TextView register_btn = rootView.findViewById(R.id.btn_register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new RegisterFragment());
                fr.commit();
            }
        });

        login_btn = rootView.findViewById(R.id.btn_login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

    }

    private boolean CheckAllFields() {
        if (et_username.length() == 0) {
            et_username.setError("This field is required!");
            return false;
        }

        if (et_password.length() == 0) {
            et_password.setError("Password is required!");
            return false;

        } else if (et_password.length() < 8) {
            et_password.setError("Password must be minimum 8 characters!");
            return false;
        }

        return true;
    }

}