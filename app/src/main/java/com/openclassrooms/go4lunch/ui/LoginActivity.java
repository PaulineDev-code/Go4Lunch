package com.openclassrooms.go4lunch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.go4lunch.databinding.ActivityLoginBinding;

import com.openclassrooms.go4lunch.viewmodels.ViewModelSignIn;
import com.openclassrooms.go4lunch.viewmodelfactory.ViewModelFactoryGo4Lunch;

import java.util.Collections;


public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private static final int RC_SIGN_IN = 123 ;
    ViewModelSignIn viewModelSignIn;

    @Override
    ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
        viewModelSignIn = new ViewModelProvider(this, ViewModelFactoryGo4Lunch.getInstance()).get(ViewModelSignIn.class);


        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.openclassrooms.go4lunch",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
    }

    private void setupListeners(){
        // Login Button
        binding.facebookLoginButton.setOnClickListener(view -> {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Collections.singletonList(
                                    new AuthUI.IdpConfig.FacebookBuilder().build()))
                            .setIsSmartLockEnabled(false, true)
                            .build(),RC_SIGN_IN);

        });

        binding.googleLoginButton.setOnClickListener(view -> {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Collections.singletonList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .setIsSmartLockEnabled(false, true)
                            .build(),RC_SIGN_IN);

        });
    }

   /* private void startSignInActivity(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.FirebaseUI)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.fui_ic_googleg_color_24dp)
                        .build(),
                RC_SIGN_IN);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(binding.loginActivityLyt, message, Snackbar.LENGTH_SHORT).show();
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                viewModelSignIn.createUser();
                Intent intentMainActivity = new Intent(this, MainActivity.class);
                startActivity(intentMainActivity);
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar("error_authentication_canceled");
                } else if (response.getError()!= null) {
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                        showSnackBar("error_no_internet");
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar("error_unknown_error");
                    }
                }
            }
        }
    }

}
