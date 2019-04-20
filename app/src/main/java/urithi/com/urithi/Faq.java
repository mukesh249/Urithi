package urithi.com.urithi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import urithi.com.urithi.Adapter.FaqAdapter;
import urithi.com.urithi.Api.Network;
import urithi.com.urithi.Api.SharedPrefManager;
import urithi.com.urithi.Api.WebUrls;
import urithi.com.urithi.Model.FaqModel;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class Faq extends AppCompatActivity {

    @Bind(R.id.faq_recycler_view)
    RecyclerView faq_recycler_view;
    FaqAdapter faqAdapter;
    ArrayList<FaqModel> faqModelArrayList;
    @Bind(R.id.refer_earn_btn)
    Button refer_earn_btn;
    @Bind(R.id.go_back_btn)
    Button go_back_btn;
    private InterstitialAd mInterstitialAd;
    boolean a_click=false;boolean p_click=false;boolean r_click=false;boolean f_click=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq2);
        ButterKnife.bind(this,this);

        refer_earn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Faq.this)) {
                    startActivity(new Intent(Faq.this,Refer_earn.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }else {
                    f_click = false;
                    r_click = true;
                    p_click = false;
                    a_click = false;
                    showInterstitial();
                }
            }
        });
        go_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Network.isConnectingToInternet(Faq.this)) {
                    finish();
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

        faqModelArrayList =  new ArrayList<>();
        faq_recycler_view.setHasFixedSize(true);
        faq_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        if (!Network.isConnectingToInternet(Faq.this)) {
            SharedPrefManager.showMessage(Faq.this, getString(R.string.network_error_msg));
            return;
        }else {
            FaqMethod();
        }

        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_interstitial_testing));
        if (!Network.isConnectingToInternet(Faq.this)) {
            SharedPrefManager.showMessage(Faq.this, getString(R.string.network_error_msg));
            return;
        }else{
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (r_click){
                    startActivity(new Intent(Faq.this,Refer_earn.class));
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                if (p_click){
                    finish();
                }
            }
        });
    }
    public void showInterstitial(){
        if (mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            if (r_click){
                startActivity(new Intent(Faq.this,Refer_earn.class));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
            if (p_click){
                finish();
            }
        }
    }

    private void FaqMethod(){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.api_hiting));
        progressDialog.show();

        RequestQueue requestQueue= newRequestQueue(Faq.this);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, WebUrls.BASE_URL+WebUrls.faq_api, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("Faq_response",response);
                    JSONArray array = jsonObject.optJSONArray("data");
                    faqModelArrayList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            FaqModel item=new FaqModel();
                            item.setFaq_id(o.optString("faq_id"));
                            item.setFaq_ques(o.optString("faq"));
                            item.setFaq_ans(o.optString("answer"));
                            faqModelArrayList.add(item);
                        }
                    faqAdapter = new FaqAdapter(faqModelArrayList,Faq.this);
                    faq_recycler_view.setAdapter(faqAdapter);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(Faq.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
