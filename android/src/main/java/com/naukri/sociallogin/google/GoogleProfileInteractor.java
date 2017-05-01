package com.naukri.sociallogin.google;

import com.google.api.services.people.v1.model.Person;

public interface GoogleProfileInteractor {
    void fetchCompleted(Person person);
    void fetchFailed();
}
