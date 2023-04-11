package com.dvvee.dnevnjakapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dvvee.dnevnjakapp.R;

public class LoginFragment extends Fragment {

    public LoginFragment(){
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        initListeners(view);
    }

    private void initListeners(View view){
        view.findViewById(R.id.loginButton).setOnClickListener((v) -> {
            String hello = ((EditText)view.findViewById(R.id.usernameInputField)).getText().toString().trim();
            Toast toast = Toast.makeText(this.getContext(), "Hello " + hello, Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}
