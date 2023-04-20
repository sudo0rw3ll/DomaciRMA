package com.dvvee.dnevnjakapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.db.SQLiteManager;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;
import com.dvvee.dnevnjakapp.viewpager.PagerAdapter;

public class ChangePassword extends Fragment {

    private SharedViewModel sharedViewModel;

    private EditText newPasswordTextField;
    private EditText checkPasswordTextField;

    private Button changePasswordButton;

    public ChangePassword(){
        super(R.layout.change_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        init(view);
        System.out.println("PASSWORD " + this.sharedViewModel.getUser().getValue().getPassword());
    }

    private void init(View view){
        initView(view);
        initListeners();
    }

    private void initView(View view){
        newPasswordTextField = view.findViewById(R.id.new_password_field);
        checkPasswordTextField = view.findViewById(R.id.new_password_confirm_field);
        changePasswordButton = view.findViewById(R.id.change_password_button);
    }

    private void initListeners(){
        changePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordTextField.getText().toString();
            String checkPassword = checkPasswordTextField.getText().toString();

            if(newPassword.equals(this.sharedViewModel.getUser().getValue().getPassword())){
                Toast.makeText(getContext(), "New password cannot be same as your old password", Toast.LENGTH_SHORT).show();
                return;
            }

            if(checkPassword(newPassword)){
                if(checkPassword.equals(newPassword)){
                    this.sharedViewModel.getUser().getValue().setPassword(newPassword);
                    Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                    update();
                    clean();
                }
            }else{
                Toast.makeText(getContext(), "Password must be at least 5 chars long, containing 1 or more upper case letters and one digit", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkPassword(String password){
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?!.*[~#^|$%&*!])[A-Za-z\\d]{5,}$";

        if(password.matches(regex))
            return true;

        return false;
    }

    private void clean(){
        newPasswordTextField.setText("");
        checkPasswordTextField.setText("");
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.switchPage(PagerAdapter.USER_PROFILE_FRAGMENT);
    }

    private void update(){
        SQLiteManager manager = SQLiteManager.instanceOfDatabase(getContext());
        manager.updateUser(this.sharedViewModel.getUser().getValue());
    }
}
