package com.mks.gaidlibtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.advertising_id_service.appclick.googleadvertisingidservice.GoogleAdvertisingIdGetter;
import com.advertising_id_service.appclick.googleadvertisingidservice.IGoogleAdvertisingIdGetter;
import com.advertising_id_service.appclick.googleadvertisingidservice.PublisherID.PublisherIDMask;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.List;

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

        Button btn1  = (Button) findViewById(R.id.btn1);
        final TextView lbl_1 = (TextView)  findViewById(R.id.lbl_1);
        Button btn2  = (Button) findViewById(R.id.btn2);
        final TextView lbl_2 = (TextView)  findViewById(R.id.lbl_2);
        Button btn3  = (Button) findViewById(R.id.btn3);
        final TextView lbl_3 = (TextView)  findViewById(R.id.lbl_3);
        Button btn4  = (Button) findViewById(R.id.btn4);
        final TextView lbl_4 = (TextView)  findViewById(R.id.lbl_4);
        Button btn5  = (Button) findViewById(R.id.btn5);
        final TextView lbl_5 = (TextView)  findViewById(R.id.lbl_5);
        Button btn6  = (Button) findViewById(R.id.btn6);
        final TextView lbl_6 = (TextView)  findViewById(R.id.lbl_6);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        String data = null;
                        try {
                            data = new GoogleAdvertisingIdGetter().getOriginalID(getApplicationContext());
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }
                        setTextInMainGIThred(R.id.lbl_1, "" + data);
                    }
                });
            }
        });



        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run () {
                        String data = null;
                        try {
                            data = new GoogleAdvertisingIdGetter().getFakeGaid(getApplicationContext());
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }
                        setTextInMainGIThred(R.id.lbl_2, "" + data);
                    }
                });
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = null;
                data = new GoogleAdvertisingIdGetter().generateGUID(IGoogleAdvertisingIdGetter.GenerateIDType.DEFAULT, getApplicationContext());
                lbl_3.setText(""+data);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = null;
                data = new GoogleAdvertisingIdGetter().generateGUID(IGoogleAdvertisingIdGetter.GenerateIDType.RANDOM, getApplicationContext());
                lbl_4.setText(""+data);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> data = new GoogleAdvertisingIdGetter().getFilePublisherIDs(getApplicationContext(), new PublisherIDMask("apk,appclick", "_", ".zip,.apk"));
                lbl_5.setText(""+data);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = new GoogleAdvertisingIdGetter().getInnerPublisherIDs(getApplicationContext(), "key_inner");
                lbl_6.setText(""+data);
            }
        });
    }
}
