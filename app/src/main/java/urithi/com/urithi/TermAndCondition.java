package urithi.com.urithi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TermAndCondition extends AppCompatActivity {
    @Bind(R.id.proceed_to_loan_btn) Button  proceed_to_loan_btn;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);

        ButterKnife.bind(this,this);

        proceed_to_loan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
            }
        });

        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));
//        MobileAds.initialize(this,getResources().getString(R.string.app_banner));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(TermAndCondition.this,Sigin.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
                startActivity(new Intent(TermAndCondition.this,Sigin.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }
}
