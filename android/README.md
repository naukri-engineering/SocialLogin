

_**SocialLogin**_ is a library which makes fetching and parsing user data from social network easier. If you sometime have tried to work with social networks on android you would remember that this is quite cumbersome. You have to read documentation for every social platform, download their SDK or use some libraries for OAuth and make http calls by yourself. This library should make your work a lot easier for integrating ```Google+ and Facebook Login```.

This library can - Facilitate login into G+/Facebook account, Get person's information and parse it.

**Getting Started**	

First of all, you need to register you application on Developer consoles:

For _Facebook_,

Open https://developers.facebook.com/
1. On the top click on Apps and register as developer Then click Create a New App Select Dashboard in the left menu You should see your App ID, enter it in your AndroidManifest.xml

    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="**App ID**"
    />
2. Go to the Settings in left menu Enter your Contact Email Click + Add Platform 
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

Usage -

Facebook :

Implement or instatiate FbInteractor and just call *FacebookLoginHandler.initiateLogin()*, 
you'll get the callbacks in respective methods of FbInteractor. 
If the process is successful, FbInteractor.onSuccess is called with LoginResult. We can then parse and use the LoginResult as per our requirements.
To signOut of the currently logged in person, call *FacebookLoginHandler.signOut(*) method.

Google -

Similar to Facebook's method, Implement or instatiate GoogleInteractor.
As this is not based on static model, you'll need to create and object of GoogleLoginHandler and then Call *googleLoginHandlerObject.initiateLogin()*. If the process is successful, *googleinteractor.loginSuccess()* would be called with login GoogleResult. 
For fetching a person's profile, we can instantiate GoogleProfileInteractor and exectue FetchGoogleProfile asyncTask.
.

