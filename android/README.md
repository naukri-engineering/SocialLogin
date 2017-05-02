This library facilitates login with G+/Facebook account, fetching user's profile.

**Getting Started**	

First of all, you need to register your application on respective Developer consoles:

For _Facebook_,

Open https://developers.facebook.com/
1. On the top click on Apps and register as developer, then click Create a New App, Select Dashboard in the left menu. You should see your App ID, enter it in your AndroidManifest.xml

    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="**App ID**"
    />
2. Go to the Settings in left menu. Enter your Contact Email Click + Add Platform 
3. Select Android Enter your package name,
* To obtain key hash, use the *'keytool': keytool -exportcert -alias -keystore | openssl sha1 -binary | openssl base64*
More info: https://developers.facebook.com/docs/android/getting-started/

For Google,

1. Open https://console.developers.google.com/project 
2. Create New Project Open APIs in left menu Find Google+ API and enable it 
3. Open Credentials in left menu Click Create New Client ID Select INSTALLED APPLICATION.
4. Android Enter your package name,
* To obtain SHA1 go to your console and enter: 
*keytool -exportcert -alias androiddebugkey -keystore path-to-debug-or-production-keystore -list -v*
More Info: https://developers.google.com/console/help/new/#installedapplications

**Add Dependency**

To include this library, Add the following dependency into your gradle file,

_compile 'com.naukri.android:SocialLogin:1.0.0'_

**Usage**

Facebook:

Implement or instatiate FbInteractor and just call *FacebookLoginHandler.initiateLogin()*, you'll receive the callbacks in respective methods of FbInteractor. 

If the process is successful, FbInteractor.onSuccess is called with LoginResult. You can then parse and use the LoginResult as per your requirements.

To signOut of the currently logged in person, call *FacebookLoginHandler.signOut(*) method.

Google:

Similar to Facebook's method, Implement or instantiate GoogleInteractor.

As this is not based on static model, you'll need to create an object of GoogleLoginHandler and then Call *googleLoginHandlerObject.initiateLogin()*. If the process is successful, *googleinteractor.loginSuccess()* would be called with login GoogleResult.

For fetching a person's profile, you can instantiate GoogleProfileInteractor and exectue FetchGoogleProfile asyncTask.

**Contributing**

Social Login is open source. Help us by submitting merge requests, forking and playing around with the codebase.

**Contact us**

Get in touch with us with your suggestions, thoughts and queries at: engineering@naukri.com

**License**

Please see [LICENSE.md](LICENSE.md) for details.

