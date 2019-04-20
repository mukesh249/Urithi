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

public class Registration extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.first_et)
    EditText first_name;
    @Bind(R.id.second_et)
    EditText last_name;
    @Bind(R.id.email_et)
    EditText sign_email_erg;
    @Bind(R.id.id_pass_et)
    EditText id_passport;
    @Bind(R.id.mobile_et)
    EditText mobile;
    @Bind(R.id.pass_et)
    EditText sign_pass_reg;
    @Bind(R.id.con_pass_et)
    EditText con_pass;

    @Bind(R.id.sigin_btn) Button sigin_btn;
    @Bind(R.id.registration_btn) Button registration_btn;
    @Bind(R.id.triangle_signin) ImageView triangle_signin;
    @Bind(R.id.reg_form_btn) Button reg_form_btn;
    @Bind(R.id.form_rel) RelativeLayout form_rel;
    private InterstitialAd mInterstitialAd;
    Boolean rclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this,this);
        sigin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this,Sigin.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View,String>(sigin_btn,"sin_t");
                pairs[1] = new Pair<View,String>(registration_btn,"reg_btn_ct");
                pairs[2] = new Pair<View,String>(triangle_signin,"trianle_image");
                pairs[3] = new Pair<View,String>(form_rel,"get_btn_tr");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Registration.this,pairs);
                startActivity(intent,activityOptions.toBundle());
                finish();
            }
        });
        rclick = false;
        reg_form_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Registration.this)) {
                    CreateAccount();
                    return;
                }else {
                    rclick = true;
                    showInterstitial();
                }
            }
        });
        //        MobileAds.initialize(this,"ca-app-pub-4969343742134029~1926324990");
        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(Registration.this)) {
            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (rclick){
                    CreateAccount();
                }
//                if(sclick){
//                    SignIn();
//                }
//                if (f_click){
//                    startActivity(new Intent(SignIn_SignUp.this,ForgotPasword.class));
//                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                }
            }
        });
    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            if (rclick){
                CreateAccount();
            }
//            if(sclick){
//                SignIn();
//            }
//            if (f_click){
//                startActivity(new Intent(SignIn_SignUp.this,ForgotPasword.class));
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
        }
    }
    public void CreateAccount(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (TextUtils.isEmpty(first_name.getText().toString())){
            first_name.setError("Please enter the First Name");
            first_name.requestFocus();
        } else if (TextUtils.isEmpty(last_name.getText().toString())){
            last_name.setError("Please enter the Last Name");
            last_name.requestFocus();
        } else if (!SharedPrefManager.getInstance(Registration.this).isValidEmail(sign_email_erg.getText().toString())){
            sign_email_erg.setError("Email not valid");
            sign_email_erg.requestFocus();
        } else if (sign_pass_reg.getText().length()<6){
            sign_pass_reg.setError("Password must have atleast 6 digit");
            sign_pass_reg.requestFocus();
        } else if (TextUtils.isEmpty(con_pass.getText().toString())){
            con_pass.setError("Please enter the confirm Password");
            con_pass.requestFocus();
        }else if (!sign_pass_reg.getText().toString().equals(con_pass.getText().toString())){
            con_pass.setError("Password and confirm Password do not match");
            con_pass.requestFocus();
        }else if (TextUtils.isEmpty(id_passport.getText().toString())){
            id_passport.setError("Please enter your ID number/ Passport NO.");
            id_passport.requestFocus();
        }else if (TextUtils.isEmpty(mobile.getText().toString())){
            mobile.setError("Please Enter your mobile number");
            mobile.requestFocus();
        }else {
            if (!Network.isConnectingToInternet(Registration.this)) {
                SharedPrefManager.showMessage(Registration.this, getString(R.string.network_error_msg));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                return;
            }else {
//            showInterstitial();
                try {
                    HashMap objectNew = new HashMap();
                    objectNew.put("first_name", first_name.getText() + "");
                    objectNew.put("last_name", last_name.getText() + "");
                    objectNew.put("mobile", mobile.getText() + "");
                    objectNew.put("email", sign_email_erg.getText() + "");
                    objectNew.put("password", sign_pass_reg.getText() + "");
                    objectNew.put("confirm_password", con_pass.getText() + "");
                    objectNew.put("id_proof", id_passport.getText() + "");

                    Log.d("data_obj", objectNew.toString());
                    new WebTask(Registration.this, WebUrls.BASE_URL + WebUrls.acc_api, objectNew, Registration.this, RequestCode.CODE_Signup, 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_Signup){
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("create_acc_response",response);
                JSONObject dataObject = jsonObject.optJSONObject("data");

                if (jsonObject.optString("status").compareTo("success")==0){
                    SharedPrefManager.setUserID(Registration.this,WebUrls.User_Id,dataObject.getString("id"));
                    SharedPrefManager.setUserEmail(Registration.this,WebUrls.User_Email,dataObject.getString("email"));
                    SharedPrefManager.setIDPassPort(Registration.this,WebUrls.User_Id_Passport,dataObject.getString("id_proof"));
                    SharedPrefManager.setMobile(Registration.this,WebUrls.User_Mobile,dataObject.getString("phone"));
                    Toast.makeText(Registration.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registration.this,OptVerify.class).putExtra("Activity_id","reg"));
                }else {
                    Toast.makeText(Registration.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
