package com.naukri.sociallogin.facebook;

import android.content.Context;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

/**
 * Created by sudeep on 28/4/17.
 */

public class InstanceFactory {

    public LoginManager getLoginManagerInstance() {
        return LoginManager.getInstance();
    }

    public CallbackManager getCallBackManager() {
        return CallbackManager.Factory.create();
    }

    public void initializeFacebookSdk(Context context) {
        FacebookSdk.sdkInitialize(context);
    }
}
