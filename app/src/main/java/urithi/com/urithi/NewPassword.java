package urithi.com.urithi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

public class NewPassword extends AppCompatActivity implements WebCompleteTask {
    @Bind(R.id.submit_btn)
    Button submit_btn;
    @Bind(R.id.pass_et)
    EditText pass_et;
    @Bind(R.id.con_pass_et)
    EditText con_pass_et;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this,this);

        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(NewPassword.this)) {
                    NewPassword();
                    return;
                }else {
                    showInterstitial();
                }
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(NewPassword.this)) {
            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                NewPassword();
            }
        });

    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            NewPassword();
        }
    }

    public void NewPassword(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (TextUtils.isEmpty(pass_et.getText().toString())){
            pass_et.setError("Please enter the Password");
            pass_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else if (pass_et.getText().length()<6){
            pass_et.setError("Password must have atleast 6 digit");
            pass_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else if (TextUtils.isEmpty(con_pass_et.getText().toString())){
            con_pass_et.setError("Please enter the confirm Password");
            con_pass_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }else if (!pass_et.getText().toString().equals(con_pass_et.getText().toString())){
            con_pass_et.setError("Password and confirm Password do not match");
            con_pass_et.requestFocus();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }else {
            if (!Network.isConnectingToInternet(NewPassword.this)) {
                SharedPrefManager.showMessage(NewPassword.this, getString(R.string.network_error_msg));
                return;
            }else {
                HashMap objectNew = new HashMap();
                objectNew.put("email", SharedPrefManager.getUserEmail(NewPassword.this, WebUrls.User_Email) + "");
                objectNew.put("password", pass_et.getText().toString() + "");
                objectNew.put("confirm_password", con_pass_et.getText().toString() + "");
                new WebTask(NewPassword.this, WebUrls.BASE_URL + WebUrls.reset_password_api, objectNew, NewPassword.this, RequestCode.CODE_forgot_password, 1);
            }
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_forgot_password){
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("create_acc_response",response);
                JSONObject dataObject = jsonObject.optJSONObject("data");

                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(NewPassword.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewPassword.this,Sigin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }else {
                    Toast.makeText(NewPassword.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
