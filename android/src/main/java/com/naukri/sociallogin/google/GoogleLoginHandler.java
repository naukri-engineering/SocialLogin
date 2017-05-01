package com.naukri.sociallogin.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;


/**
 * Created by sudeep on 17/1/17.
 */

public class GoogleLoginHandler implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private FragmentActivity fragmentActivity;
    private GoogleInteractor googleInteractor;
    public static final int RC_SIGN_IN = 6006;         //Google Login activity request code
    public final int NULL_ERROR_STATUS_CODE = -33;
    public AuthFactory authFactory;

    /**
     * Inits the  Google class
     *
     * @param activity         - The fragment activity initiating the login process
     * @param googleInteractor - Instance of  class implementing GoogleInteractor interface
     */
    public GoogleLoginHandler(FragmentActivity activity, GoogleInteractor googleInteractor) {
        this(activity, googleInteractor, null, new AuthFactory());
    }

    public GoogleLoginHandler(AuthFactory authFactory) {
        this.authFactory = authFactory;
    }

    /**
     * Inits the  Google class
     *
     * @param activity         - The fragment activity initiating the login process
     * @param googleInteractor - Instance of  class implementing GoogleInteractor interface
     * @param authCode         - Client OAuth2.0 ID
     */
    public GoogleLoginHandler(FragmentActivity activity, GoogleInteractor googleInteractor, String authCode,AuthFactory authFactory) {
        this.fragmentActivity = activity;
        this.googleInteractor = googleInteractor;
        GoogleSignInOptions.Builder builder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail();
        if (authCode != null) {
            builder.requestServerAuthCode(authCode, false);
            builder.requestIdToken(authCode);
        }
        googleSignInOptions = builder.build();
        this.authFactory = authFactory;
    }

    /**
     * Initiates the login process
     */

    public void initiateLogin() {
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(fragmentActivity.getApplicationContext())
                    .enableAutoManage(fragmentActivity, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        }
        Intent signInIntent = this.authFactory.getGoogleSignInApi().getSignInIntent(mGoogleApiClient);
        fragmentActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    /**
     * Call this function from the {@link Activity#onActivityResult(int, int, Intent)} method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                if (data != null) {
                    GoogleSignInResult result = this.authFactory.getGoogleSignInApi().getSignInResultFromIntent(data);
                    handleSignInResult(result);
                } else {
                    loginFailed(NULL_ERROR_STATUS_CODE);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            loginFailed(NULL_ERROR_STATUS_CODE);
        }
    }

    private void loginFailed(int statusCode) {
        if (googleInteractor != null) {
            googleInteractor.onError(statusCode);
        }
    }

    private void handleSignInResult(final GoogleSignInResult result) {
        if (result == null) {
            loginFailed(NULL_ERROR_STATUS_CODE);
        } else if (result.isSuccess()) {
            if (googleInteractor != null) {
                googleInteractor.loginSuccess(result);
            }
        } else {
            Status status = result.getStatus();
            int statusCode = status.getStatusCode();
            loginFailed(statusCode);
        }
    }

    public void signOut(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        GoogleLoginHandler.this.authFactory.getGoogleSignInApi().signOut(mGoogleApiClient);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
        mGoogleApiClient.connect();
    }

    public void destroyGoogleClient(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(fragmentActivity);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (googleInteractor != null) {
            googleInteractor.connectionFailed();
        }
    }

    public void initializeGoogleApiClient(GoogleApiClient googleApiClient){
        this.mGoogleApiClient =  googleApiClient;
    }
}
