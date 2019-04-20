package urithi.com.urithi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

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

public class LoanDetail extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.request_loan_btn)
    Button request_loan_btn;
    @Bind(R.id.already_tv)
    TextView already_tv;
    @Bind(R.id.refer_earn_btn)
    Button refer_earn_btn;
    @Bind(R.id.reypay)
    EditText repay;
    @Bind(R.id.amount)
    EditText amount;
    @Bind(R.id.fag_btn)
    Button faq_btn;
    private InterstitialAd mInterstitialAd;
    boolean a_click=false;boolean p_click=false;boolean r_click=false;boolean f_click=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        ButterKnife.bind(this,this);

        request_loan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(repay.getText().toString())){
                    repay.setError("Please enter time period to repay");
                    repay.requestFocus();
                } else if (TextUtils.isEmpty(amount.getText().toString())){
                    amount.setError("Please enter your amount");
                    amount.requestFocus();
                }else {
                    int num=Integer.parseInt(amount.getText().toString());
                    if (SharedPrefManager.getLoanType(LoanDetail.this, WebUrls.Loan_Type).compareTo("Business Loan")==0){
                        if(num<=10000){
                            amount.setText(""+num);
                            if (!Network.isConnectingToInternet(LoanDetail.this)) {
//                                SharedPrefManager.showMessage(LoanDetail.this, getString(R.string.network_error_msg));
                                LoanRequestMethod();
                                return;
                            }else {
                                f_click =false;
                                r_click = false;
                                p_click = true;
                                a_click = false;
                                showInterstitial();
                            }

                        }else{
                            amount.setError("you can enter 10000/- or less.");
                        }
                    }else {
                        if(num<=1500){
                            amount.setText(""+num);
                            if (!Network.isConnectingToInternet(LoanDetail.this)) {
                                LoanRequestMethod();
                                return;
                            }else {
                                f_click =false;
                                r_click = false;
                                p_click = true;
                                a_click = false;
                                showInterstitial();
                            }

                        }else{
                            amount.setError("you can enter 1500/- or less only");
                        }
                    }
                }

            }
        });
        already_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(LoanDetail.this)) {
                    startActivity(new Intent(LoanDetail.this,AlreadyAccount.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    return;
                }else {
                    f_click = false;
                    r_click = false;
                    p_click = false;
                    a_click = true;
                    showInterstitial();
                }
            }
        });
        refer_earn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(LoanDetail.this)) {
                    startActivity(new Intent(LoanDetail.this,Refer_earn.class));
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
                if (!Network.isConnectingToInternet(LoanDetail.this)) {
                    startActivity(new Intent(LoanDetail.this,Faq.class));
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
        if (!Network.isConnectingToInternet(LoanDetail.this)) {
            return;
        }else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (r_click){
                    startActivity(new Intent(LoanDetail.this,Refer_earn.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if(f_click){
                    startActivity(new Intent(LoanDetail.this,Faq.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (p_click){
                    LoanRequestMethod();
                }
                if (a_click){
                    startActivity(new Intent(LoanDetail.this,AlreadyAccount.class));
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
                startActivity(new Intent(LoanDetail.this,Refer_earn.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if(f_click){
                startActivity(new Intent(LoanDetail.this,Faq.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (p_click){
                LoanRequestMethod();
            }
            if (a_click){
                startActivity(new Intent(LoanDetail.this,AlreadyAccount.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        }
    }
    public void LoanRequestMethod(){
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (!Network.isConnectingToInternet(LoanDetail.this)) {
            SharedPrefManager.showMessage(LoanDetail.this, getString(R.string.network_error_msg));
            return;
        }else {
            HashMap objectNew = new HashMap();
            objectNew.put("user_id", SharedPrefManager.getUserID(LoanDetail.this, WebUrls.User_Id) + "");
            objectNew.put("loan_type", SharedPrefManager.getLoanType(LoanDetail.this, WebUrls.Loan_Type) + "");
            objectNew.put("repayment", repay.getText() + "");
            objectNew.put("amount", amount.getText() + "");
            new WebTask(LoanDetail.this, WebUrls.BASE_URL + WebUrls.update_loan_api, objectNew, LoanDetail.this, RequestCode.CODE_Loan_Request, 1);
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_Loan_Request){
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("create_acc_response",response);
                JSONObject dataObject = jsonObject.optJSONObject("data");

//                SharedPrefManager.setUserID(SignIn_SignUp.this,WebUrls.User_Id,dataObject.getString("id"));
//                SharedPrefManager.setUserEmail(SignIn_SignUp.this,WebUrls.User_Email,dataObject.getString("email"));
//                SharedPrefManager.setIDPassPort(SignIn_SignUp.this,WebUrls.User_Id_Passport,dataObject.getString("id_proof"));
//                SharedPrefManager.setMobile(SignIn_SignUp.this,WebUrls.User_Mobile,dataObject.getString("phone"));
                if (jsonObject.optString("status").compareTo("success")==0){
                    startActivity(new Intent(LoanDetail.this,LoanRequestReceived.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
