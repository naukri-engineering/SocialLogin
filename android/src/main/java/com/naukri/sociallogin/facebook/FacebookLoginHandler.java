package com.naukri.sociallogin.facebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sudeep on 12/1/17.
 */

public class FacebookLoginHandler {

    private static CallbackManager callbackManager;
    private static Activity initiatingActivity;

    FacebookLoginHandler() {

    }

    /**
     * Function will start the Facebook Login process
     *
     * @param activity     - Instance of activity that is initiating the Login Process
     * @param fbInteractor - Instance of  class implementing this interface
     * @param permissions - The list of permissions that your account will fetch
     */
    public static void initiateLogin(Activity activity, FbInteractor fbInteractor, List permissions, InstanceFactory instanceFactory) {
        instanceFactory.initializeFacebookSdk(activity.getApplicationContext());
        initiatingActivity = activity;
        callbackManager = instanceFactory.getCallBackManager();
        requestForLogin(activity, fbInteractor, permissions,instanceFactory.getLoginManagerInstance());
    }

    private static void requestForLogin(Activity activity, final FbInteractor fbInteractor, List permissions,LoginManager loginManager) {
        //LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(activity, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbInteractor.onSuccess(loginResult);
            }

            @Override
            public void onCancel() {
                fbInteractor.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                if (error instanceof FacebookAuthorizationException && isLogedIn()) {
                    logout();
                }
                fbInteractor.onError(error);
            }
        });
    }

    /**
     * Call this function from the {@link Activity#onActivityResult(int, int, Intent)} method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (FacebookSdk.isFacebookRequestCode(requestCode)) {           //To check if the resultl is from Facebook,as it would be called in everycase otherwise
                if(initiatingActivity != null) {
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
        }
    }

    /**
     * Check weather the user is logged in or out
     *
     * @return
     */
    public static boolean isLogedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    /**
     * logout the current user
     */
    public static void logout() {
        LoginManager.getInstance().logOut();
    }

    /**
     * return the facebook user profile data
     *
     * @return
     */
    public static Profile getProfile() {
        return Profile.getCurrentProfile();
    }

    /**
     *  Function will fetch data from facebook
     * @param loginResult - Facebook LoginResult
     * @param graphJSONObjectCallback - Object of  GraphRequest.GraphJSONObjectCallback
     * @param fields - Fields to fetch from facebook
     */
    public static void fetchDataFromFacebook(final LoginResult loginResult,
                                             GraphRequest.GraphJSONObjectCallback graphJSONObjectCallback, String fields) {
        final GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                graphJSONObjectCallback);
        Bundle parameters = new Bundle();
        parameters.putString("fields", fields);
        request.setParameters(parameters);
        request.executeAsync();
    }


    /**
     * It will return the bitmap of user profile picture
     * @param userID - FB userId
     * @return
     */
    public static Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


}
