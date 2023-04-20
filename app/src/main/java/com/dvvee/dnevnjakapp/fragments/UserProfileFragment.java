package com.dvvee.dnevnjakapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.activities.SplashActivity;
import com.dvvee.dnevnjakapp.db.SQLiteManager;
import com.dvvee.dnevnjakapp.model.User;
import com.dvvee.dnevnjakapp.viewmodels.SharedViewModel;

public class UserProfileFragment extends Fragment {

    private SharedViewModel sharedViewModel;

    private TextView emailTextView;
    private ImageView userProfilePic;
    private Button changePasswordButton;
    private Button logoutButton;

    public UserProfileFragment(){
        super(R.layout.user_profile_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        init(view);
    }

    private void init(View view){
        initView(view);
        initListeners();
        initObservers();
    }

    private void initView(View view){
        emailTextView = view.findViewById(R.id.email);
        userProfilePic = view.findViewById(R.id.profile_picture);
        changePasswordButton = view.findViewById(R.id.change_password_button);
        logoutButton = view.findViewById(R.id.logout_button);
    }

    private void initListeners(){
        changePasswordButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Change password", Toast.LENGTH_SHORT).show();
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.switchPage(6);
        });

        logoutButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logout", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.clear();
            editor.apply();

            Intent intent = new Intent(this.getActivity(), SplashActivity.class);
            startActivity(intent);
        });

        userProfilePic.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Change your profile picture");

            final EditText input = new EditText(v.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userInput = input.getText().toString();

                    if(userInput.startsWith("http://") || userInput.startsWith("https://")){
                        sharedViewModel.getUser().getValue().setImageLink(userInput);
                        updateUser();
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void initObservers(){
        this.sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            System.out.println(user.getEmail() + " " + user.getUsername() + " " + user.getPassword());
            Glide.with(this.getContext())
                    .load(user.getImageLink())
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_person_24)
                    .into((ImageView)getView()
                            .findViewById(R.id.profile_picture));
            System.out.println(user.getEmail());
            emailTextView.setText(user.getEmail());
        });
    }

    private void updateUser(){
        SQLiteManager manager = SQLiteManager.instanceOfDatabase(getContext());
        manager.updateUser(this.sharedViewModel.getUser().getValue());
    }
}
