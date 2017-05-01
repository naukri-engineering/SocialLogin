package com.naukri.sociallogin.google;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;

/**
 * Created by sudeep on 25/4/17.
 */

public class AuthFactory {

public GoogleSignInApi getGoogleSignInApi(){
        return Auth.GoogleSignInApi;
    }

}
