package urithi.com.urithi;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import urithi.com.urithi.Api.Network;
import urithi.com.urithi.Api.RequestCode;
import urithi.com.urithi.Api.SharedPrefManager;
import urithi.com.urithi.Api.WebCompleteTask;
import urithi.com.urithi.Api.WebTask;
import urithi.com.urithi.Api.WebUrls;

public class Sigin extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.sigin_btn) Button sigin_btn;
    @Bind(R.id.registration_btn) Button registration_btn;
    @Bind(R.id.signin_tv) TextView signin_tv;
    @Bind(R.id.triangle_signin) ImageView triangle_signin;
    @Bind(R.id.signin_form_btn) Button signin_form_btn;
    @Bind(R.id.form_rel) RelativeLayout form_rel;
    @Bind(R.id.email_et) EditText email_et;
    @Bind(R.id.pass_et) EditText pass_et;
    @Bind(R.id.forgot_tv) TextView forgot_tv;

    private InterstitialAd mInterstitialAd;
    Boolean sclick=false,f_click=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this,this);

        signin_form_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Sigin.this)) {
                    SignIn();
                    return;
                }else {
                    sclick = true;
                    showInterstitial();
                }
            }
        });
        forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sclick = false;
                f_click = true;
                if (!Network.isConnectingToInternet(Sigin.this)) {
//                    SharedPrefManager.showMessage(SignIn_SignUp.this, getString(R.string.network_error_msg));
                    startActivity(new Intent(Sigin.this,ForgotPasword.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    showInterstitial();
                }

            }
        });
        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sigin.this,Registration.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(sigin_btn,"reg_btn_ct");
                pairs[1] = new Pair<View,String>(registration_btn,"reg_t");
                pairs[2] = new Pair<View,String>(triangle_signin,"trianle_image");
                pairs[3] = new Pair<View,String>(form_rel,"get_btn_tr");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Sigin.this,pairs);
                startActivity(intent,activityOptions.toBundle());
                finish();
            }
        });
        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));


        if (!Network.isConnectingToInternet(Sigin.this)) {
            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if(sclick){
                    SignIn();
                }
                if (f_click){
                    startActivity(new Intent(Sigin.this,ForgotPasword.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            if(sclick){
                SignIn();
            }
            if (f_click){
                startActivity(new Intent(Sigin.this,ForgotPasword.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }
    public void SignIn(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (TextUtils.isEmpty(email_et.getText().toString())){
            email_et.setError("Please enter email");
            email_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else if (TextUtils.isEmpty(pass_et.getText().toString())){
            pass_et.setError("Please enter your password");
            pass_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }else if (!SharedPrefManager.getInstance(Sigin.this).isValidEmail(email_et.getText().toString())){
            email_et.setError("Email not valid");
            email_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }else {
            if (!Network.isConnectingToInternet(Sigin.this)) {
                SharedPrefManager.showMessage(Sigin.this, getString(R.string.network_error_msg));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                return;
            }else {
                HashMap objectNew = new HashMap();
                objectNew.put("email", email_et.getText() + "");
                objectNew.put("password", pass_et.getText() + "");
                new WebTask(Sigin.this, WebUrls.BASE_URL + WebUrls.login_api, objectNew, Sigin.this, RequestCode.CODE_Login, 1);
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_Login ){
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("login_response",response);
                JSONObject dataObject = jsonObject.optJSONObject("data");
                if (jsonObject.optString("status").compareTo("success")==0){
                    SharedPrefManager.setUserID(Sigin.this,WebUrls.User_Id,dataObject.getString("id"));
                    SharedPrefManager.setUserEmail(Sigin.this,WebUrls.User_Email,dataObject.getString("email"));
                    SharedPrefManager.setIDPassPort(Sigin.this,WebUrls.User_Id_Passport,dataObject.getString("id_proof"));
                    SharedPrefManager.setMobile(Sigin.this,WebUrls.User_Mobile,dataObject.getString("phone"));
                    Toast.makeText(Sigin.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Sigin.this,PurposeOfLoan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }else {
                    Toast.makeText(Sigin.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
