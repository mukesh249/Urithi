package urithi.com.urithi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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

public class OptVerify extends AppCompatActivity implements WebCompleteTask {

    private InterstitialAd mInterstitialAd;
    @Bind(R.id.verify_btn)
    Button verify_btn;
    @Bind(R.id.opt_et)
    EditText opt_et;
    @Bind(R.id.scroll_view)
    ScrollView scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_verify);

        ButterKnife.bind(this,this);
        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        scroll_view.setSmoothScrollingEnabled(true);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(OptVerify.this)) {
                    VerifyOtp();
                    return;
                }else {
                    showInterstitial();
                }

            }
        });

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(OptVerify.this)) {
            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());

        }
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                VerifyOtp();
            }
        });

    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            VerifyOtp();
        }
    }

    public void VerifyOtp(){
        if (!Network.isConnectingToInternet(OptVerify.this)) {
            SharedPrefManager.showMessage(OptVerify.this, getString(R.string.network_error_msg));
            return;
        }else {
            HashMap objectNew = new HashMap();
            objectNew.put("email", SharedPrefManager.getUserEmail(OptVerify.this, WebUrls.User_Email) + "");
            objectNew.put("otp", opt_et.getText().toString() + "");
            new WebTask(OptVerify.this, WebUrls.BASE_URL + WebUrls.check_otp_api, objectNew, OptVerify.this, RequestCode.CODE_checkResetOtp, 1);
        }

    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_checkResetOtp){
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("create_acc_response",response);
                JSONObject dataObject = jsonObject.optJSONObject("data");

                if (jsonObject.optString("status").compareTo("success")==0){
                    Toast.makeText(OptVerify.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    if (getIntent().getExtras()!=null&&getIntent().getExtras().getString("Activity_id","").compareTo("reg")==0){
                        startActivity(new Intent(OptVerify.this,Sigin.class));
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                    if(getIntent().getExtras()!=null&&getIntent().getExtras().getString("Activity_id","").compareTo("for")==0) {
                        startActivity(new Intent(OptVerify.this,NewPassword.class));
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                }else {
                    Toast.makeText(OptVerify.this,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
