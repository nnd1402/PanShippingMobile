package com.example.panshippingandroid.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.panshippingandroid.R;
import com.example.panshippingandroid.activities.MainActivity;
import com.example.panshippingandroid.model.LoginModel;
import com.example.panshippingandroid.model.UserModel;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.panshippingandroid.activities.LoginActivity.apiService;
import static com.example.panshippingandroid.utils.Const.AUTHENTICATION_FILE_NAME;
import static com.example.panshippingandroid.utils.Const.USER_ID;


public class LoginFragment extends Fragment {

    private EditText et_username;
    private EditText et_password;
    private Button login_btn;
    private TextView register_btn;
    boolean isAllFieldsChecked = false;
    private SharedPreferences sharedPreferences;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireContext().getSharedPreferences(AUTHENTICATION_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        register_btn.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container, RegisterFragment.newInstance());
            fragmentTransaction.commit();
        });
        login_btn.setOnClickListener(v -> {
            isAllFieldsChecked = fieldUserAndPassword();
            if (isAllFieldsChecked) {
                LoginModel loginModel = new LoginModel();
                loginModel.setUsername(et_username.getText().toString().trim());
                loginModel.setPassword(et_password.getText().toString().trim());
                loginCall(loginModel);
            }
        });
    }

    private void initUI() {
        et_username = requireView().findViewById(R.id.et_username);
        et_password = requireView().findViewById(R.id.et_password);
        register_btn = requireView().findViewById(R.id.btn_register);
        login_btn = requireView().findViewById(R.id.btn_login);
    }

    private boolean fieldUserAndPassword() {
        if (et_username.getText().toString().length() == 0) {
            et_username.setError(getString(R.string.username_is_required));
            return false;
        } else if (et_password.getText().toString().length() == 0) {
            et_password.setError(getString(R.string.field_to_password));
            return false;
        }
        return true;
    }

    private void loginCall(LoginModel loginModel) {
        Call<UserModel> call = apiService.login(loginModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    UserModel user = response.body();
                    if (user != null) {
                        sharedPreferences.edit().putLong(USER_ID, user.getId()).apply();
                    }
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Toast.makeText(getActivity(), R.string.problem_with_login, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }
}