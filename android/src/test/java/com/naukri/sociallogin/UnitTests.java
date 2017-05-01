package com.naukri.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.naukri.sociallogin.facebook.FacebookLoginHandler;
import com.naukri.sociallogin.facebook.FbInteractor;
import com.naukri.sociallogin.facebook.InstanceFactory;
import com.naukri.sociallogin.google.AuthFactory;
import com.naukri.sociallogin.google.GoogleInteractor;
import com.naukri.sociallogin.google.GoogleLoginHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.meta.When;

/**
 * Created by sudeep on 20/2/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class, GoogleApiClient.class, Auth.class, Status.class, CallbackManager.class, FacebookSdk.class, LoginManager.class})
public class UnitTests {
    GoogleLoginHandler googleLoginHandler;

    public static final List PERMISSIONS_LIST = Arrays.asList("email", "public_profile", "user_birthday", "user_location", "user_work_history", "user_education_history","user_mobile_phone");

    @Mock
    private FragmentActivity fragmentActivity;

    @Mock
    private Intent intent;

    @Mock
    GoogleInteractor googleInteractor;

    @Mock
    GoogleSignInResult googleSignInResult;

    @Mock
    GoogleApiClient googleApiClient;

    @Mock
    GoogleSignInApi googleSignInApi;

    @Mock
    AuthFactory authFactory;

    @Mock
    Status status;

    @Mock
    Activity activity;

    @Mock
    FbInteractor fbInteractor;

    @Mock
    InstanceFactory instanceFactory;

    @Mock
    CallbackManager callbackManager;

    @Mock
    LoginManager loginManager;
    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        Mockito.when(authFactory.getGoogleSignInApi()).thenReturn(googleSignInApi);
    }

    //Google

    @Test
    public void shouldCallOnErrorWhenResultIsNull() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleApiClient.class);
        PowerMockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        intent.putExtra("DummyValue", "Dummy");
        googleLoginHandler = new GoogleLoginHandler(fragmentActivity, googleInteractor, "4259784738-h8vrl9nce4dorj48809p1q6lt848u2ks.apps.googleusercontent.com", authFactory);
        googleLoginHandler.initializeGoogleApiClient(googleApiClient);
        googleLoginHandler.onActivityResult(6006, -1, intent);
        Mockito.verify(googleInteractor).onError(-33);
    }

    @Test
    public void shouldCallOnErrorWhenDataIsNull() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleApiClient.class);
        PowerMockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        googleLoginHandler = new GoogleLoginHandler(fragmentActivity, googleInteractor, "42597834738-h8vrl9nce4dorj48809p1q6lt848u2ks.apps.googleusercontent.com", authFactory);
        googleLoginHandler.initializeGoogleApiClient(googleApiClient);
        googleLoginHandler.onActivityResult(6006, -1, intent);
        Mockito.verify(googleInteractor).onError(-33);
    }

    @Test
    public void shouldCallSuccessWhenResultIsValid() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleApiClient.class);
        PowerMockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        PowerMockito.mockStatic(Auth.class);
        Mockito.when(intent.hasExtra(Mockito.anyString())).thenReturn(true);
        Mockito.when(googleSignInResult.isSuccess()).thenReturn(true);
        PowerMockito.doReturn(googleSignInResult).when(googleSignInApi).getSignInResultFromIntent(intent);
        Mockito.when(authFactory.getGoogleSignInApi().getSignInResultFromIntent(intent)).thenReturn(googleSignInResult);
        googleLoginHandler = new GoogleLoginHandler(fragmentActivity, googleInteractor, "4259783048-h8vrl9nce4dorj48809p1q6lt848u2ks.apps.googleusercontent.com", authFactory);
        googleLoginHandler.initializeGoogleApiClient(googleApiClient);
        googleLoginHandler.onActivityResult(6006, -1, intent);
        Mockito.verify(googleInteractor).loginSuccess(googleSignInResult);
    }

    @Test
    public void shouldCallLoginFailedWhenDataIsNull() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleApiClient.class);
        PowerMockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        googleLoginHandler = new GoogleLoginHandler(fragmentActivity, googleInteractor, "4259784738-h8vrl9nce4dorj48809p1q6lt848u2ks.apps.googleusercontent.com", authFactory);
        googleLoginHandler.initializeGoogleApiClient(googleApiClient);
        googleLoginHandler.onActivityResult(6006, -1, null);
        Mockito.verify(googleInteractor).onError(-33);
    }

    @Test
    public void shouldFailLoginWhenActivityCancelled() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleApiClient.class);
        PowerMockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        googleLoginHandler = new GoogleLoginHandler(fragmentActivity, googleInteractor, "4259784738-h8vrl9nce4dorj48809p1q6lt848u2ks.apps.googleusercontent.com", authFactory);
        googleLoginHandler.initializeGoogleApiClient(googleApiClient);
        googleLoginHandler.onActivityResult(6006, 0, null);
        Mockito.verify(googleInteractor).onError(-33);
    }

    @Test
    public void shouldCallLoginFailedWhenResultIsUnsuccessful() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.mockStatic(GoogleApiClient.class);
        PowerMockito.when(TextUtils.isEmpty(Mockito.anyString())).thenReturn(false);
        PowerMockito.mockStatic(Auth.class);
        Mockito.when(intent.hasExtra(Mockito.anyString())).thenReturn(true);
        Mockito.when(googleSignInResult.isSuccess()).thenReturn(false);
        PowerMockito.when(googleSignInResult.getStatus()).thenReturn(status);
        Mockito.when(status.getStatusCode()).thenReturn(10);
        PowerMockito.doReturn(googleSignInResult).when(googleSignInApi).getSignInResultFromIntent(intent);
        Mockito.when(authFactory.getGoogleSignInApi().getSignInResultFromIntent(intent)).thenReturn(googleSignInResult);
        googleLoginHandler = new GoogleLoginHandler(fragmentActivity, googleInteractor, "4259783048-h8vrl9nce4dorj48809p1q6lt848u2ks.apps.googleusercontent.com", authFactory);
        googleLoginHandler.initializeGoogleApiClient(googleApiClient);
        googleLoginHandler.onActivityResult(6006, -1, intent);
        Mockito.verify(googleInteractor).onError(Mockito.anyInt());
    }

    //Facebook

    @Test
    public void shoudlCallonSuccessWhenSuccessful() {
        PowerMockito.mockStatic(CallbackManager.class);
        PowerMockito.mockStatic(LoginManager.class);
        Mockito.when(instanceFactory.getCallBackManager()).thenReturn(callbackManager);
        Mockito.when(activity.getApplicationContext()).thenReturn(context);
        PowerMockito.when(instanceFactory.getLoginManagerInstance()).thenReturn(loginManager);
        FacebookLoginHandler.initiateLogin(activity,fbInteractor,PERMISSIONS_LIST,instanceFactory);
        Mockito.verify(instanceFactory).initializeFacebookSdk(context);
        ArgumentCaptor<FacebookCallback> captor = ArgumentCaptor.forClass(FacebookCallback.class);
        ArgumentCaptor<CallbackManager> captor1 = ArgumentCaptor.forClass(CallbackManager.class);
        Mockito.verify(loginManager).registerCallback(captor1.capture(),captor.capture());
        FacebookCallback facebookCallback = captor.getValue();
        AccessToken accessToken = new AccessToken("accessToken","applicationId","socialUser",null,null,null,null,null);
        Set<String> strings = new TreeSet<>();
        strings.add("phone");
        LoginResult loginResult = new LoginResult(accessToken,strings,strings);
        facebookCallback.onSuccess(loginResult);
        Mockito.verify(fbInteractor).onSuccess(loginResult);
    }

    @Test
    public void shoudlCallonErrorInCaseOfFailure() {
        PowerMockito.mockStatic(CallbackManager.class);
        PowerMockito.mockStatic(LoginManager.class);
        Mockito.when(instanceFactory.getCallBackManager()).thenReturn(callbackManager);
        Mockito.when(activity.getApplicationContext()).thenReturn(context);
        PowerMockito.when(instanceFactory.getLoginManagerInstance()).thenReturn(loginManager);
        FacebookLoginHandler.initiateLogin(activity,fbInteractor,PERMISSIONS_LIST,instanceFactory);
        Mockito.verify(instanceFactory).initializeFacebookSdk(context);
        ArgumentCaptor<FacebookCallback> captor = ArgumentCaptor.forClass(FacebookCallback.class);
        ArgumentCaptor<CallbackManager> captor1 = ArgumentCaptor.forClass(CallbackManager.class);
        Mockito.verify(loginManager).registerCallback(captor1.capture(),captor.capture());
        FacebookCallback facebookCallback = captor.getValue();
        FacebookException facebookException = new FacebookException("facebookError");
        facebookCallback.onError(facebookException);
        Mockito.verify(fbInteractor).onError(facebookException);
    }


    @Test
    public void shoudlCallOnCancelInCaseOfCancellation() {
        PowerMockito.mockStatic(CallbackManager.class);
        PowerMockito.mockStatic(LoginManager.class);
        Mockito.when(instanceFactory.getCallBackManager()).thenReturn(callbackManager);
        Mockito.when(activity.getApplicationContext()).thenReturn(context);
        PowerMockito.when(instanceFactory.getLoginManagerInstance()).thenReturn(loginManager);
        FacebookLoginHandler.initiateLogin(activity,fbInteractor,PERMISSIONS_LIST,instanceFactory);
        Mockito.verify(instanceFactory).initializeFacebookSdk(context);
        ArgumentCaptor<FacebookCallback> captor = ArgumentCaptor.forClass(FacebookCallback.class);
        ArgumentCaptor<CallbackManager> captor1 = ArgumentCaptor.forClass(CallbackManager.class);
        Mockito.verify(loginManager).registerCallback(captor1.capture(),captor.capture());
        FacebookCallback facebookCallback = captor.getValue();
        facebookCallback.onCancel();
        Mockito.verify(fbInteractor).onCancel();
    }


}
