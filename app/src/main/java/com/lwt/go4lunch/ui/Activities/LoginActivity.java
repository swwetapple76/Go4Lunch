package com.lwt.go4lunch.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lwt.go4lunch.R;
import com.lwt.go4lunch.databinding.ActivityLoginBinding;
import com.lwt.go4lunch.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private static final String TAG = "LoginActivity";
    int AUTHUI_REQUEST_CODE = 10001;

       @Override
    ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
        startLoginActivity();
    }
}

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    private void startLoginActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void handleLoginRegister(View view ){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers;
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        // Launch the activity
        Intent intent =   AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true)
                .setAlwaysShowSignInMethodScreen(true)
                .setLogo(R.drawable.logo)
                .build();
        startActivityForResult(intent, AUTHUI_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
                //Checking for User (New/Old)
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    Toast.makeText(this,"Welcome new user",Toast.LENGTH_SHORT).show();
                    //This is a New User
                } else {
                    //This is a returning user
                    Toast.makeText(this,"Welcome back again",Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();

            } else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }
            }
        }
    }

}




