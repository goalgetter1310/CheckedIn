package com.checkedin.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.checkedin.R;
import com.checkedin.databinding.ActivityWelcomeBinding;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.MyPrefs;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Yudiz on 15/07/16.
 */

public class WelcomeActivity extends BaseActivity implements View.OnClickListener, VolleyStringRequest.AfterResponse {

    private WebServiceCall webServiceCall;
    private String id="",fistname="",lastname="",image="";
    private  CallbackManager callbackManager;
    private String imagePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        imagePath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/profile.png";
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallBack());
        ActivityWelcomeBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        mBinding.btnSignin.setOnClickListener(this);
        mBinding.btnSignup.setOnClickListener(this);
        mBinding.btnSignupFb.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_signup:
                Intent iSignup = new Intent(this, SignupActivity.class);
                startActivityForResult(iSignup, 101);

                break;
            case R.id.btn_signin:
                Intent iLogin = new Intent(this, LoginActivity.class);
                startActivityForResult(iLogin, 101);
                break;
            case R.id.btn_signup_fb:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    PermissionEverywhere.getPermission(WelcomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 105)
                                            .enqueue(new PermissionResultCallback() {
                                                @Override
                                                public void onComplete(PermissionResponse permissionResponse) {
                                                    if (permissionResponse.isGranted()) {
                                                        initFacebook();
                                                    }

                                                }
                                            });

                                }
                else{
                    initFacebook();
                }

                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag","Welcome.onActivityResult " + requestCode + " " + resultCode + " " + data);
        if (resultCode == RESULT_OK && requestCode == 101) {
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show();
            Intent iMain = new Intent(this, MainActivity.class);
            startActivity(iMain);
            finish();
        }
        else{
            try {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();


            }
        }
    }


    private void initFacebook() {

        ArrayList<String> alPermission = new ArrayList<>();
        alPermission.add("email");
        alPermission.add("public_profile");
        alPermission.add("user_friends");
        LoginManager.getInstance().logInWithReadPermissions(this, alPermission);

    }

    public class FacebookCallBack implements FacebookCallback<LoginResult> {


        @Override
        public void onSuccess(final LoginResult result) {
            Utility.logUtils("Base Success Called");
            try {
                    if (result.getAccessToken().getUserId() != null) {
//                        loginListener.onSocialLogin(result.getAccessToken().getUserId(), "", "", "", "", "", "");
                    }
//                    else

                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    if (object != null) {
//                                        loginListener.onSocialLogin("123123", "", "", "", "", "", "");
                                        if (object.has("id")) {
                                            String id = object.getString("id");
                                            //                                String[] name = object.getString("name").split(" ");
                                            String email = "";
                                            if (object.has("email")) email = object.getString("email");

                                            if(object.has("picture")&&object.getJSONObject("picture").has("data")&&
                                            object.getJSONObject("picture").getJSONObject("data").has("url"))
                                                image=object.getJSONObject("picture").getJSONObject("data").getString("url");

                                            Utility.logUtils("Base Success Called"+image);
                                            String dob = "";
//                                        if (object.has("birthday")) dob = object.getString("birthday");
                                            //                                String image = "https://graph.facebook.com/" + post_id + "/picture?type=large";
                                            logoutFacebook();
                                            onSocialLogin(
                                                        id,
                                                        "" + result.getAccessToken().getToken(),
                                                        object.getString("first_name"),
                                                        object.getString("last_name"),
                                                        "" + email,
                                                        "" + object.getString("name"),
                                                        "" + dob);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", " name, email, gender,  first_name, last_name, picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                        Utility.logUtils("Base Success Called 2");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancel() {
            Utility.logUtils("Base cancel Called");
            onSocialLogin("", "", "", "", "", "", "");

        }

        @Override
        public void onError(FacebookException e) {
            Utility.logUtils("Base error Called"+e.getMessage());
            if (e instanceof FacebookAuthorizationException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
            }

            onSocialLogin("", "", "", "", "", "", "");
            }

    }


    public void logoutFacebook() {
        try {
            FacebookSdk.sdkInitialize(this);
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSocialLogin(String id, String accessToken, String firstname, String lastname, String authTokenOREmail, String displayName, String dateOfBirth) {
        Log.d("tag", "facebook login :" + accessToken);
        webServiceCall = new WebServiceCall(this);
        this.id=id;
        this.fistname=firstname;
        this.lastname=lastname;
        webServiceCall.loginFbWsCall(this, id, UserPreferences.getGCMKey(this));
    }



    class DownloadImage extends AsyncTask<String, Integer, String>
    {
        protected String doInBackground(String...arg0) {
            try {
                return DownloadFromUrl(arg0[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return "";

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(s.equals(""))
                    imagePath="";

                webServiceCall.signUpWithSocial(WelcomeActivity.this,imagePath,id,fistname,lastname);
            }
            catch (Exception ae){ae.printStackTrace();}
        }
    }

    public String DownloadFromUrl(String fileName) {
        try {
            URL url = new URL(image); //you can write here any link
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file);
            byte data[] = new byte[1024];
           int   count=0;
            while ((count = input.read(data)) != -1) {

                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }



    @Override
    public void onResponseReceive(int requestCode) {
        if (requestCode == WebServiceCall.REQUEST_CODE_LOGIN) {
            UserDetailModel mLogin = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, "Login Response");
            if (mLogin != null) {
                if (mLogin.getStatus() == BaseModel.STATUS_SUCCESS) {
                    UserPreferences.saveUserDetails(this, mLogin.getUserDetails());
                    onActivityResult(101, RESULT_OK, null);
                } else if(mLogin.getStatus() == BaseModel.STATUS_FALITURE&&mLogin.getMessage().equals("No Social ID connect")){
//                    Utility.showSnackBar(findViewById(android.R.id.content), mLogin.getMessage());
//                    webServiceCall.signUpWithSocial(this,image,id,fistname,lastname);
                    new DownloadImage().execute(imagePath);
                }
                else {
                    Utility.showSnackBar(findViewById(android.R.id.content), mLogin.getMessage());
                }
            } else {
                Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
            }
        }
        else  if (requestCode == WebServiceCall.REQUEST_CODE_SIGNUP) {
            UserDetailModel mSignUp = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, "Signup Response");
            if (mSignUp != null) {
                if (mSignUp.getStatus() == BaseModel.STATUS_SUCCESS) {
                    UserPreferences.saveFirstTime(this);
                    UserPreferences.saveUserDetails(this, mSignUp.getUserDetails());
                    webServiceCall.deviceTokenWsCall(this);
                } else {
                    Utility.showSnackBar(findViewById(android.R.id.content), mSignUp.getMessage());
                }
            } else {
                Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
            }
        }
        else if (requestCode == WebServiceCall.REQUEST_CODE_REGISTER_DEVICE_TOKEN) {
            BaseModel mBase = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
            Utility.logUtils(mBase != null ? mBase.getMessage() : getString(R.string.server_connect_error));
            onActivityResult(101, RESULT_OK, null);
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }
}
