package com.checkedin.interfaces;


/**
 * Created by Vinay Rathod on 08/04/16.
 */
public interface SocialLoginListener {
    public void onSocialLogin(String id, String accessToken, String firstname, String lastname, String authTokenOREmail, String displayName, String dateOfBirth);
}
