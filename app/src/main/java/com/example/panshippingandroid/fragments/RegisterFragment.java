package com.example.panshippingandroid.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

    private EditText et_firstName, et_lastName, et_userName, et_email, et_password, et_repassword, et_postal_address, et_country, et_phone;
    private Button back_btn, btn_register;

    boolean isAllFieldsChecked = false;
    Service service = new Service();
    APIService apiService;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        et_firstName = rootView.findViewById(R.id.et_firstName);
        et_lastName = rootView.findViewById(R.id.et_lastName);
        et_userName = rootView.findViewById(R.id.et_userName);
        et_email = rootView.findViewById(R.id.et_email);
        et_password = rootView.findViewById(R.id.et_password);
        et_repassword = rootView.findViewById(R.id.et_repassword);
        et_postal_address = rootView.findViewById(R.id.et_postal_address);
        et_phone = rootView.findViewById(R.id.et_phone);

        back_btn = rootView.findViewById(R.id.btn_back);
        back_btn.setOnClickListener(v -> {
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
                    UserModel userModel = new UserModel(
                            et_firstName.getText().toString(),
                            et_lastName.getText().toString(),
                            et_userName.getText().toString(),
                            et_email.getText().toString(),
                            et_password.getText().toString(),
                            et_repassword.getText().toString(),
                            et_postal_address.getText().toString(),
                            et_phone.getText().toString() 
                    );

                    registerCall(userModel);

                    FragmentTransaction fr = getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container, new LoginFragment());
                    fr.commit();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = Service.getInstance(getContext()).create(APIService.class);
    }


    private boolean CheckAllFields() {
        if (et_firstName.length() == 0) {
            et_firstName.setError("This field is required!");
            return false;
        }

        if (et_lastName.length() == 0) {
            et_lastName.setError("This field is required!");
            return false;
        }

        if (et_userName.length() == 0) {
            et_userName.setError("This field is required!");
            return false;
        }

        if (et_email.length() == 0) {
            et_email.setError("This field is required!");
            return false;
        }

        if (et_password.length() == 0) {
            et_password.setError("Password is required!");
            return false;
        }

        if (et_password.length() < 1) {
            et_password.setError("Password must be minimum 8 characters!");
            return false;
        }

        if (!et_repassword.getText().toString().equals(et_password.getText().toString())) {
            et_repassword.setError("Password do not match!");
            return false;
        }

        if (et_postal_address.length() == 0) {
            et_password.setError("Password is required!");
            return false;

        } else if (et_phone.length() == 0) {
            et_phone.setError("Password is required!");
            return false;
        }

        return true;
    }

    public void registerCall(UserModel userModel) {
        Call<Void> call = apiService.addUser(userModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {

                    System.out.println("Success");
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
