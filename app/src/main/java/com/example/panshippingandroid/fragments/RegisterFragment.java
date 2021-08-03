package com.example.panshippingandroid.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.model.UserModel;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;


public class RegisterFragment extends Fragment {

    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_userName;
    private EditText et_email;
    private EditText et_password;
    private EditText et_confirmPassword;
    private EditText et_address;
    private EditText et_country;
    private EditText et_phone;
    private TextView tv_login;
    private Button btn_register;
    private View rootView;
    private FrameLayout container;
    private boolean isAllFieldsChecked = false;

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        tv_login.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, LoginFragment.newInstance());
            fragmentTransaction.commit();
        });
        btn_register.setOnClickListener(v -> {
            isAllFieldsChecked = CheckAllFields();
            if (isAllFieldsChecked) {
                UserModel userModel = new UserModel();
                userModel.setFirstName(et_firstName.getText().toString());
                userModel.setLastName(et_lastName.getText().toString());
                userModel.setUsername(et_userName.getText().toString());
                userModel.setEmail(et_email.getText().toString());
                userModel.setPassword(et_password.getText().toString());
                userModel.setConfirmPassword(et_confirmPassword.getText().toString());
                userModel.setAddress(et_address.getText().toString());
                userModel.setCountry(et_country.getText().toString());
                userModel.setPhone(et_phone.getText().toString());
                registerCall(userModel);
            }
        });
    }

    private void initUI() {
        et_firstName = requireView().findViewById(R.id.et_firstName);
        et_lastName = requireView().findViewById(R.id.et_lastName);
        et_userName = requireView().findViewById(R.id.et_userName);
        et_email = requireView().findViewById(R.id.et_email);
        et_password = requireView().findViewById(R.id.et_password);
        et_confirmPassword = requireView().findViewById(R.id.et_confirmPassword);
        et_address = requireView().findViewById(R.id.et_address);
        et_country = requireView().findViewById(R.id.et_country);
        et_phone = requireView().findViewById(R.id.et_phone);
        tv_login = requireView().findViewById(R.id.tv_login);
        btn_register = rootView.findViewById(R.id.btn_register);
        container = requireView().findViewById(R.id.fragment_container);
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean CheckAllFields() {
        if (et_firstName.getText().toString().length() == 0) {
            et_firstName.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_lastName.getText().toString().length() == 0) {
            et_lastName.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_userName.getText().toString().length() == 0) {
            et_userName.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_email.getText().toString().length() < 12 && !isValidEmail(et_email.getText().toString())) {
            et_email.setError(getString(R.string.field_is_required));
            Toast.makeText(getActivity(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_password.getText().toString().length() < 6) {
            Toast.makeText(getActivity(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_password.getText().toString().length() != et_confirmPassword.getText().toString().length()) {
            Toast.makeText(getActivity(), R.string.matching_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_address.getText().toString().length() == 0) {
            et_address.setError(getString(R.string.field_is_required));
            return false;
        }
        if (et_country.getText().toString().length() == 0) {
            et_country.setError(getString(R.string.field_is_required));
            return false;
        } else if (et_phone.getText().toString().length() == 0) {
            et_phone.setError(getString(R.string.field_is_required));
            return false;
        }
        Toast.makeText(getActivity(), R.string.successfully_registration, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void registerCall(UserModel userModel) {
        Call<Void> call = apiService.addUser(userModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    replaceFragment(new LoginFragment());
                } else {
                    Toast.makeText(getActivity(), "Don't have response!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fr = getParentFragmentManager().beginTransaction();
        fr.replace(R.id.fragment_container, fragment);
        fr.commit();
    }
}
