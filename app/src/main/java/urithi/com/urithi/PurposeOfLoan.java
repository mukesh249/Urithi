package urithi.com.urithi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import butterknife.Bind;
import butterknife.ButterKnife;
import urithi.com.urithi.Api.Network;
import urithi.com.urithi.Api.SharedPrefManager;
import urithi.com.urithi.Api.WebUrls;

public class PurposeOfLoan extends AppCompatActivity {

    @Bind(R.id.proceed_btn)
    Button proceed_btn;
    @Bind(R.id.already_tv)
    TextView already_tv;
    @Bind(R.id.refer_earn_btn)
    Button refer_earn_btn;
    @Bind(R.id.fag_btn)
    Button faq_btn;

    @Bind(R.id.loan_spinner)
    Spinner loan_spinner;
    private InterstitialAd mInterstitialAd;
    boolean a_click=false;boolean p_click=false;boolean r_click=false;boolean f_click=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose);

        ButterKnife.bind(this,this);
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(PurposeOfLoan.this)) {
                    SharedPrefManager.showMessage(PurposeOfLoan.this, getString(R.string.network_error_msg));
                    return;
                }else {
                    f_click =false;
                    r_click = false;
                    p_click = true;
                    a_click = false;
                    showInterstitial();
                }

//                startActivity(new Intent(PurposeOfLoan.this,LoanDetail.class));
            }
        });
        already_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(PurposeOfLoan.this)) {
                    SharedPrefManager.showMessage(PurposeOfLoan.this, getString(R.string.network_error_msg));
                    return;
                }else {

                    f_click =false;
                    r_click = false;
                    p_click = false;
                    a_click = true;
                    showInterstitial();
                }

//                startActivity(new Intent(PurposeOfLoan.this,AlreadyAccount.class));
            }
        });

        refer_earn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(PurposeOfLoan.this)) {
                    SharedPrefManager.showMessage(PurposeOfLoan.this, getString(R.string.network_error_msg));
                    return;
                }else {
                    f_click =false;
                    r_click = true;
                    p_click = false;
                    a_click = false;
                    showInterstitial();
                }

//                startActivity(new Intent(PurposeOfLoan.this,Refer_earn.class));
            }
        });

        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(PurposeOfLoan.this)) {
                    SharedPrefManager.showMessage(PurposeOfLoan.this, getString(R.string.network_error_msg));
                    return;
                }else {
                    f_click =true;
                    r_click = false;
                    p_click = false;
                    a_click = false;
                    showInterstitial();
                }

//                startActivity(new Intent(PurposeOfLoan.this,Faq.class));
            }
        });

        loan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                SharedPrefManager.setLoanType(PurposeOfLoan.this, WebUrls.Loan_Type,selectedItem);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
//        MobileAds.initialize(this,"ca-app-pub-4969343742134029~1926324990");
        MobileAds.initialize(this,getResources().getString(R.string.app_banner_testing));

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(PurposeOfLoan.this)) {

            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (r_click){
                    startActivity(new Intent(PurposeOfLoan.this,Refer_earn.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if(f_click){
                    startActivity(new Intent(PurposeOfLoan.this,Faq.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (p_click){
                    startActivity(new Intent(PurposeOfLoan.this,LoanDetail.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (a_click){
                    startActivity(new Intent(PurposeOfLoan.this,AlreadyAccount.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            if (r_click){
                startActivity(new Intent(PurposeOfLoan.this,Refer_earn.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if(f_click){
                startActivity(new Intent(PurposeOfLoan.this,Faq.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (p_click){
                startActivity(new Intent(PurposeOfLoan.this,LoanDetail.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (a_click){
                startActivity(new Intent(PurposeOfLoan.this,AlreadyAccount.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }

}
