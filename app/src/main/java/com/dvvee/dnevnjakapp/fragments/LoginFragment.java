package com.dvvee.dnevnjakapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.db.SQLiteManager;

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
            SQLiteManager manager = SQLiteManager.instanceOfDatabase(getContext());

            System.out.println(manager.getUserList().get(0).getPassword());

            String email = ((EditText)view.findViewById(R.id.usernameInputField)).getText().toString().trim();
            String password = ((EditText)view.findViewById(R.id.passwordInputField)).getText().toString().trim();

            if(email.isEmpty()) {
                Toast.makeText(this.getContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if(password.isEmpty()){
                Toast.makeText(this.getContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int res = manager.checkUser(email, password);

            if (res != -1){
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(this.getActivity().getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("userId", res);
                editor.apply();

                Intent intent = new Intent(this.getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
