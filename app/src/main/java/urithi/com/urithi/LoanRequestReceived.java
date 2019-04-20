package urithi.com.urithi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.Bind;
import butterknife.ButterKnife;
import urithi.com.urithi.Api.Network;
import urithi.com.urithi.Api.SharedPrefManager;

public class LoanRequestReceived extends AppCompatActivity {

    @Bind(R.id.go_back_btn)
    Button go_back_btn;
    @Bind(R.id.refer_earn_btn)
    Button refer_earn_btn;
    @Bind(R.id.fag_btn)
    Button faq_btn;

    private InterstitialAd mInterstitialAd;
    boolean a_click=false;boolean p_click=false;boolean r_click=false;boolean f_click=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_request_received);

        ButterKnife.bind(this,this);

        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(LoanRequestReceived.this)) {
                    SharedPrefManager.showMessage(LoanRequestReceived.this, getString(R.string.network_error_msg));
                    return;
                }else {
                    f_click = false;
                    r_click = false;
                    p_click = true;
                    a_click = false;
                    showInterstitial();
                }
            }
        });
        refer_earn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(LoanRequestReceived.this)) {
                    startActivity(new Intent(LoanRequestReceived.this,Refer_earn.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    f_click = false;
                    r_click = true;
                    p_click = false;
                    a_click = false;
                    showInterstitial();
                }
            }
        });
        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(LoanRequestReceived.this)) {
                    startActivity(new Intent(LoanRequestReceived.this,Faq.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    f_click = true;
                    r_click = false;
                    p_click = false;
                    a_click = false;
                    showInterstitial();
                }
            }
        });
        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(LoanRequestReceived.this)) {

            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (r_click){
                    startActivity(new Intent(LoanRequestReceived.this,Refer_earn.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if(f_click){
                    startActivity(new Intent(LoanRequestReceived.this,Faq.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (p_click){
                    startActivity(new Intent(LoanRequestReceived.this,PurposeOfLoan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        });
    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            if (r_click){
                startActivity(new Intent(LoanRequestReceived.this,Refer_earn.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if(f_click){
                startActivity(new Intent(LoanRequestReceived.this,Faq.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (p_click){
                startActivity(new Intent(LoanRequestReceived.this,PurposeOfLoan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
    }
}
