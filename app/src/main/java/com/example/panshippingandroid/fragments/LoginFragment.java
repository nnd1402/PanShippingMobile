package com.example.panshippingandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.panshippingandroid.activities.MainActivity;
import com.example.panshippingandroid.R;
import com.example.panshippingandroid.api.APIService;
import com.example.panshippingandroid.api.Service;
import com.example.panshippingandroid.model.LoginModel;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private EditText et_username;
    private EditText et_password;

    private Button login_btn;
    private TextView register_btn;
    private View rootView;

    boolean isAllFieldsChecked = false;
    private APIService apiService;


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

        apiService = Service.getInstance(getContext()).create(APIService.class);


    }

    private void initUI() {

        et_username = rootView.findViewById(R.id.et_username);
        et_password = rootView.findViewById(R.id.et_password);

        register_btn = rootView.findViewById(R.id.btn_register);
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
                isAllFieldsChecked = fieldUserAndPassword();
                if (isAllFieldsChecked) {

                    LoginModel loginModel = new LoginModel();
                    loginModel.setUsername(et_username.getText().toString());
                    loginModel.setPassword(et_password.getText().toString());

                    loginCall(loginModel);

                }

            }
        });

    }

    private boolean fieldUserAndPassword() {
        if (et_username.getText().toString().length() == 0) {
            et_username.setError("This field is required!");
            return false;

        } else if (et_password.getText().toString().length() == 0) {
            et_password.setError("This field is required!");
            return false;
        }

        return true;
    }

    public void loginCall(LoginModel loginModel) {
        Call<Void> call = apiService.login(loginModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {

                    startActivity(new Intent(getActivity(), MainActivity.class));

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