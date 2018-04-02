package com.mks.gaidlibtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.advertising_id_service.appclick.googleadvertisingidservice.GoogleAdvertisingIdGetter;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private void setTextInMainGIThred(final int id, final String str)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tmp = (TextView) findViewById(id);
                tmp.setText(str);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_show_original_gaid = (Button)   findViewById(R.id.btn_show_original_gaid);
        Button btn_show_fake_gaid     = (Button)   findViewById(R.id.btn_show_fake_gaid);

        final TextView lbl_original_gaid = (TextView) findViewById(R.id.lbl_original_gaid);
        final TextView lbl_fake_gaid     = (TextView) findViewById(R.id.lbl_fake_gaid);

        btn_show_original_gaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        String realGAID = "";
                        AdvertisingIdClient.Info adInfo = null;
                        try {
                            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                        } catch (IOException e) {
                            setTextInMainGIThred(R.id.lbl_original_gaid, "Unrecoverable error connecting to Google Play services");
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            setTextInMainGIThred(R.id.lbl_original_gaid, "Google Play services is not available entirely");
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            setTextInMainGIThred(R.id.lbl_original_gaid, e.getMessage());
                            e.printStackTrace();
                        }
                        if(adInfo != null) {
                            String adInfoId = adInfo.getId();
                            if(adInfoId != null) {
                                realGAID = adInfoId;
                                setTextInMainGIThred(R.id.lbl_original_gaid, realGAID);
                            }
                            else
                            {
                                setTextInMainGIThred(R.id.lbl_original_gaid, "original gaid is NULL");
                            }
                        }
                    }
                });
            }
        });

        btn_show_fake_gaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            final String id = new GoogleAdvertisingIdGetter().getFakeGaid( getApplicationContext(), getApplicationContext().getPackageName(), "");
                            setTextInMainGIThred(R.id.lbl_fake_gaid, id);
                        } catch (IOException e) {
                            // Unrecoverable error connecting to Google Play services (e.g.,
                            // the old version of the service doesn't support getting AdvertisingId).
                            setTextInMainGIThred(R.id.lbl_fake_gaid, "Unrecoverable error connecting to Google Play services");
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // Google Play services is not available entirely.
                            setTextInMainGIThred(R.id.lbl_fake_gaid, "Google Play services is not available entirely");
                        } catch (IllegalStateException e) {
                            setTextInMainGIThred(R.id.lbl_fake_gaid, e.getMessage());
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            setTextInMainGIThred(R.id.lbl_fake_gaid, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
}
