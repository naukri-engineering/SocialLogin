package com.naukri.sociallogin.google;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sudeep on 8/2/17.
 */

public class FetchGoogleProfile extends AsyncTask<Void, Void, Person> {

    private GoogleProfileInteractor googleProfileInteractor;
    private GoogleSignInResult googleSignInResult;
    private Context context;
    private static HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public FetchGoogleProfile(GoogleProfileInteractor googleProfileInteractor, GoogleSignInResult result, Context context) {
        this.googleProfileInteractor = googleProfileInteractor;
        this.googleSignInResult = result;
        this.context = context;
    }

    @Override
    protected Person doInBackground(Void... params) {

        return getPersonData(googleSignInResult);
    }

    @Override
    protected void onPostExecute(Person meProfile) {
        super.onPostExecute(meProfile);
        googleProfileInteractor.fetchCompleted(meProfile);

    }

    private Person getPersonData(final GoogleSignInResult result) {
        {
            final List<String> list = Arrays.asList(Scopes.PROFILE);
            GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(context, list);
            credential.setSelectedAccount(
                    new Account(result.getSignInAccount().getEmail(), "com.google"));
            People service = new People.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Naukri")
                    .build();
            try {
                Person meProfile = service.people().get("people/me").execute();
                return meProfile;
            } catch (IOException e) {
                e.printStackTrace();
                googleProfileInteractor.fetchFailed();
                return null;
            }

        }
    }
}

