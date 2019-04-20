package urithi.com.urithi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.Bind;
import butterknife.ButterKnife;
import urithi.com.urithi.Api.Network;
import urithi.com.urithi.Api.SharedPrefManager;

public class AlreadyAccount extends AppCompatActivity {

    @Bind(R.id.gotit_btn)
    Button gotit_btn;
    @Bind(R.id.content)
    TextView content;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_account);

        ButterKnife.bind(this,this);

        final ProgressBar pbar = (ProgressBar) findViewById(R.id.bar); // Final so we can access it from the other thread
        pbar.setVisibility(View.VISIBLE);
        gotit_btn.setVisibility(View.GONE);
        content.setVisibility(View.GONE);

// Create a Handler instance on the main thread
        final Handler handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(5000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        if (!Network.isConnectingToInternet(AlreadyAccount.this)) {
                            SharedPrefManager.showMessage(AlreadyAccount.this, getString(R.string.network_error_msg));
                            return;
                        }else {
                            pbar.setVisibility(View.INVISIBLE);
                            gotit_btn.setVisibility(View.VISIBLE);
                            content.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        }).start();

        //        MobileAds.initialize(this,"ca-app-pub-4969343742134029~1926324990");
        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                startActivity(new Intent(AlreadyAccount.this,Sigin.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        gotit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(AlreadyAccount.this);
                progressDoalog.setMessage("logout please wait....");
                progressDoalog.show();
                // Create and start a new Thread
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            Thread.sleep(5000);
                        }
                        catch (Exception e) { } // Just catch the InterruptedException

                        // Now we use the Handler to post back to the main thread
                        handler.post(new Runnable() {
                            public void run() {
                                // Set the View's visibility back on the main UI Thread
                                if (!Network.isConnectingToInternet(AlreadyAccount.this)) {
                                    SharedPrefManager.showMessage(AlreadyAccount.this, getString(R.string.network_error_msg));
                                    return;
                                }else {
                                    progressDoalog.dismiss();
                                    showInterstitial();
                                }

                            }
                        });
                    }
                }).start();
            }
        });
    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            startActivity(new Intent(AlreadyAccount.this,Sigin.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();

        }
    }
}
