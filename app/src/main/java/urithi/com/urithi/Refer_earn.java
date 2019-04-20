package urithi.com.urithi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.Bind;
import butterknife.ButterKnife;
import urithi.com.urithi.Api.Network;

public class Refer_earn extends AppCompatActivity {

    @Bind(R.id.home_btn)
    ImageButton home_btn;
    @Bind(R.id.fag_btn)
    Button faq_btn;

    @Bind(R.id.invite_btn)
    Button invite_btn;
    @Bind(R.id.rate_us_btn)
    Button rate_us_btn;

    private InterstitialAd mInterstitialAd;
    boolean i_click=false;boolean p_click=false;boolean r_click=false;boolean f_click=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ButterKnife.bind(this,this);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Refer_earn.this)) {
                    startActivity(new Intent(Refer_earn.this,PurposeOfLoan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    p_click = true;
                    f_click = false;
                    i_click = false;
                    r_click = false;
                    showInterstitial();
                }
            }
        });
        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Refer_earn.this)) {
                    startActivity(new Intent(Refer_earn.this,Faq.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    p_click = false;
                    f_click = true;
                    i_click = false;
                    r_click = false;
                    showInterstitial();
                }

            }
        });
        invite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Refer_earn.this)) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.stawishaloans.manu.stawishaloans");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    return;
                }else {
                    p_click = false;
                    f_click = false;
                    i_click = true;
                    r_click = false;
                    showInterstitial();
                }
            }
        });

        rate_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Refer_earn.this)) {
                    //Toast.makeText(Refer_earn.this,"Coming soon..",Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    p_click = false;
                    f_click = false;
                    i_click = false;
                    r_click = true;
                    showInterstitial();
                }
            }
        });

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(Refer_earn.this)) {

            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (f_click){
                    startActivity(new Intent(Refer_earn.this,Faq.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (p_click){
                    startActivity(new Intent(Refer_earn.this,PurposeOfLoan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (i_click){
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.stawishaloans.manu.stawishaloans");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                if (r_click){
                   // Toast.makeText(Refer_earn.this,"Coming soon..",Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            if (f_click){
                startActivity(new Intent(Refer_earn.this,Faq.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (p_click){
                startActivity(new Intent(Refer_earn.this,PurposeOfLoan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (i_click){
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.stawishaloans.manu.stawishaloans");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
            if (r_click){
                //Toast.makeText(Refer_earn.this,"Coming soon..",Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }
}
