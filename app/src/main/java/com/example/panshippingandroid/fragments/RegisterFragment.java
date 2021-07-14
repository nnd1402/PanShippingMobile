package com.example.panshippingandroid.fragments;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.api.APIService;
import com.example.panshippingandroid.api.Service;
import com.example.panshippingandroid.model.UserModel;

import java.net.HttpURLConnection;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    private EditText et_firstName;
    private EditText et_lastName;
    private EditText et_userName;
    private EditText et_email;
    private EditText et_password;
    private EditText et_address;
    private EditText et_country;
    private EditText et_phone;

    private TextView tv_login;
    private Button btn_register;
    private View rootView;

    private boolean isAllFieldsChecked = false;
    private APIService apiService;

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

        apiService = Service.getInstance(getContext()).create(APIService.class);
    }


    private void initUI() {

        et_firstName = rootView.findViewById(R.id.et_firstName);
        et_lastName = rootView.findViewById(R.id.et_lastName);
        et_userName = rootView.findViewById(R.id.et_userName);
        et_email = rootView.findViewById(R.id.et_email);
        et_password = rootView.findViewById(R.id.et_password);
        et_address = rootView.findViewById(R.id.et_address);
        et_country = rootView.findViewById(R.id.et_country);
        et_phone = rootView.findViewById(R.id.et_phone);

        tv_login = rootView.findViewById(R.id.btn_back);
        tv_login.setOnClickListener(v -> {
            FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.replace(R.id.fragment_container, new LoginFragment());
            fr.commit();
        });

        btn_register = rootView.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {

                    UserModel userModel = new UserModel();

                    userModel.setFirstName(et_firstName.getText().toString());
                    userModel.setLastName(et_lastName.getText().toString());
                    userModel.setUsername(et_userName.getText().toString());
                    userModel.setEmail(et_email.getText().toString());
                    userModel.setPassword(et_password.getText().toString());
                    userModel.setAddress(et_address.getText().toString());
                    userModel.setCountry(et_country.getText().toString());
                    userModel.setPhone(et_phone.getText().toString());

                    registerCall(userModel);

                }
            }
        });

    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean CheckAllFields() {
        if (et_firstName.getText().toString().length() == 0) {
            et_firstName.setError("This field is required!");
            return false;
        }

        if (et_lastName.getText().toString().length() == 0) {
            et_lastName.setError("This field is required!");
            return false;
        }

        if (et_userName.getText().toString().length() == 0) {
            et_userName.setError("This field is required!");
            return false;
        }

        if (et_email.getText().toString().length() < 12 && !isValidEmail(et_email.getText().toString())) {
            et_email.setError("This field is required!");

            Toast.makeText(getActivity(), "Invalid email!", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (et_password.getText().toString().length() < 6) {
            Toast.makeText(getActivity(), "Invalid password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_address.getText().toString().length() == 0) {
            et_address.setError("This field is required!");
            return false;
        }
        if (et_country.getText().toString().length() == 0) {
            et_country.setError("This field is required!");
            return false;

        } else if (et_phone.getText().toString().length() == 0) {
            et_phone.setError("This field is required!");
            return false;
        }

        return true;
    }

    public void registerCall(UserModel userModel) {
        Call<Void> call = apiService.addUser(userModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new LoginFragment());
                    fr.commit();

                } else {

                    Toast.makeText(getActivity(), "Don't have response!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
                System.out.println(t.getLocalizedMessage());
                call.cancel();
            }
        });
    }
}
