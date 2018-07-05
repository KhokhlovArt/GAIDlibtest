package com.mks.gaidlibtest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.advertising_id_service.appclick.googleadvertisingidservice.CryptoProvider.CryptoProviderServicer;
import com.advertising_id_service.appclick.googleadvertisingidservice.DeviceInfo;
import com.advertising_id_service.appclick.googleadvertisingidservice.GoogleAdvertisingIdGetter;
import com.advertising_id_service.appclick.googleadvertisingidservice.InstallationInfo;
import com.advertising_id_service.appclick.googleadvertisingidservice.PublisherID.PublisherIDMask;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.IApi;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.InputParameters.CreateParameters;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.InputParameters.DeleteParameters;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.InputParameters.InstallParameters;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.InputParameters.ReadParameters;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.InputParameters.UpdateParameters;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.RESTResultListener.IRESTResultListener;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.Results.ResultCreate;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.Results.ResultDelete;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.Results.ResultInstall;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.Results.ResultRead;
import com.advertising_id_service.appclick.googleadvertisingidservice.REST.Results.ResultUpdate;
import com.advertising_id_service.appclick.googleadvertisingidservice.SharedPreferencesServicer.SharedPreferencesServicer;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.mks.gaidlibtest.Result.ResultTestCrypt;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;


public class unit_test extends AppCompatActivity {
    TextView et_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_test);

        et_main = findViewById(R.id.et_test);
        et_main.setEnabled(false);
//        final String text = "<font color=#cc0029>First Color</font> <font color=#ffcc00>Second Color</font>";
//        et_main.setText(Html.fromHtml(text));

        Button btnUnitTest = findViewById(R.id.start_test);
        btnUnitTest.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    et_main.setText(Html.fromHtml(""));
//                   CharSequence text_2 = et_main.getText();
//                   et_main.setText(Html.fromHtml(text_2 + "<br>" + text));
                    setMsg(mTestCrypt(), "Проверка шифрования");
                    setMsg(mTestDeCrypt(), "Проверка дешифрования");
                    setMsg(mTestSetSharedPref(), "Проверка setSharedPref");
                    setMsg(mTestGetSharedPref(), "Проверка getSharedPref");

                    setMsg(mGetVersion(), "Проверка getVersion");
                    setMsg(mGetGAID(), "Проверка getGAID");
                    setMsg(mGenerateGAID(), "Проверка generateGAID");

                    AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {
                           setMsg(mGetOriginalGAID(), "Проверка getOriginalGAID");
                       }
                    });
                    setMsg(mTestInstallationInfo(), "Проверка InstallationInfo");
                    setMsg(mTestDeviceInfo(), "Проверка DeviceInfo");


                    mCreate();
                    mInstall();
                    mUpdate();
                    mRead();
                    mDelete();





                    AsyncTask.execute(new Runnable() {
                       @Override
                       public void run() {
                           setMsg(mSetGAID(), "Проверка setGAID");
                       }
                    });

                    mRestorGAIDFromServer();
               }
           }
        );
    }


    void setMsg(final boolean res, final String msg)
    {
        runOnUiThread(new Runnable() {
        @Override
        public void run() {
//            String text_2 = et_main.getText().toString();
//            String text = "";
//            text += res ? "+ | " : "- | ";
//            et_main.setText(text_2 + "\n" + text + msg);


//        final String text = "<font color=#cc0029>First Color</font> <font color=#ffcc00>Second Color</font>";
//        et_main.setText(Html.fromHtml(text));
            String text = "";
            text = res ? "+ | " : "- | ";
            String color = res ? "#00FF00" : "#FF0000";

            et_main.append(Html.fromHtml("<br><font color="+ color +">" + text + msg + "</font>"));

            }
        });
    }


    /*****************************************************************
     *****************  Тестирование основных функций ****************
     ****************************************************************/
    boolean mGetVersion()
    {
        String res = null;
        res = new GoogleAdvertisingIdGetter().getVersion(getApplicationContext());
        return Pattern.matches("\\d\\.\\d\\.\\d",res);
    }

    boolean mGetGAID()
    {
        String res = "";
        try {
            res = new GoogleAdvertisingIdGetter().getGAID(getApplicationContext(), "");
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        return Pattern.matches("[a-z,0-9,A-Z]{8}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{12}",res);
    }

    boolean mSetGAID()
    {
        String newGAID = "Its11111-new1-test-GAID-1234567890Ad";
        boolean result;
        String gaid = "";
        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
        try {
            gaid = g.getGAID(getApplicationContext(), "");
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        result = Pattern.matches("[a-z,0-9,A-Z]{8}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{12}", gaid);
        if (!result) {return result;}

        g.setGAID(getApplicationContext(),newGAID);

        try {
            gaid = g.getGAID(getApplicationContext(), "");
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }

        if (gaid.equals(newGAID)) {return result;}

        g.setGAID(getApplicationContext(),null);
        return true;
    }

    void mRestorGAIDFromServer()
    {
        final GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();

        final String[] server_gaid = {""};
        ReadParameters bp = new ReadParameters().setCnt(getApplicationContext())
                .setLm(getSupportLoaderManager())
                .setCallDestination("")
                .setPass("1111")
                .setLogin("test")
                .setAsincStart(true)
                .setForceStart(false)
                .setReadType(IApi.RestReadType.IMEI1)
                .setOnResultListener(new IRESTResultListener<ResultRead>() {
                    @Override
                    public void onResult(int i_code, ResultRead r) {
                        if (r != null) {
                            if(r.guids != null) {
                                for (Iterator<ResultRead.ItemParams> elem = r.guids.iterator(); elem.hasNext(); ) {
                                    ResultRead.ItemParams i = elem.next();
                                    server_gaid[0] = i.guid;
                                }
                            }
                        }else{
                            setMsg(false, "Востановления GAID с сервера");
                            return;
                        }

                        if ((server_gaid[0] != null) || (!server_gaid[0].equals(""))) {
                            g.setGAID(getApplicationContext(), server_gaid[0]);
                            setMsg(mGetGAID(), "Востановления GAID с сервера");
                            return;
                        }
                        else
                        {
                            setMsg(false, "Востановления GAID с сервера");
                        }
                        setMsg(false, "Востановления GAID с сервера");
                    }
                });
        g.rest_read(bp);
    }

    boolean mGetOriginalGAID()
    {
        String res = "";
        try {
            res = new GoogleAdvertisingIdGetter().getOriginalID(getApplicationContext());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        return ((Pattern.matches("[a-z,0-9,A-Z]{8}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{12}",res)) || (res == null));
    }

    boolean mGenerateGAID()
    {
        String res = "";
        res = new GoogleAdvertisingIdGetter().generateGUID(getApplicationContext());
        return Pattern.matches("[a-z,0-9,A-Z]{8}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{4}-[a-z,0-9,A-Z]{12}",res);
    }

    /*****************************************************************
     *****************  Тестирование функций REST-а ******************
     ****************************************************************/
    void mCreate()
    {
        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
        CreateParameters bp = new CreateParameters().setCnt(getApplicationContext())
                .setLm(getSupportLoaderManager())
                .setCallDestination("")
                .setPass("1111")
                .setLogin("test")
                .setAsincStart(true)
                .setForceStart(false)
                .setOnResultListener(new IRESTResultListener<ResultCreate>() {
                    @Override
                    public void onResult(int i, ResultCreate  r) {
                        if (r != null) {
                            setMsg(true, "проверка Create");
                        }else {
                            setMsg(false, "проверка Create");
                        }
                    }
                });
        g.rest_create(bp);
    }
    void mUpdate()
    {
        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
        UpdateParameters bp = new UpdateParameters().setCnt(getApplicationContext())
                .setLm(getSupportLoaderManager())
                .setCallDestination("")
                .setPass("1111")
                .setLogin("test")
                .setAsincStart(true)
                .setForceStart(false)
                .setOnResultListener(new IRESTResultListener<ResultUpdate>() {

                    @Override
                    public void onResult(int i, ResultUpdate  r) {
                        if (r != null) {
                            setMsg(true, "проверка Update");
                        }else {
                            setMsg(false, "проверка Update");
                        }
                    }
                });
        g.rest_update(bp);
    }
    void mInstall()
    {
        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
        PublisherIDMask m = new PublisherIDMask("GooGames,AppLandGames,AppClickGames,gameclub","_", ".zip,.apk");
        InstallationInfo ii = new InstallationInfo(getApplicationContext(), "", m);
        InstallParameters bp = new InstallParameters().setCnt(getApplicationContext())
                .setLm(getSupportLoaderManager())
                .setCallDestination("")
                .setPass("1111")
                .setLogin("test")
                .setAsincStart(true)
                .setForceStart(false)
                .setInstallInfo(ii)
                .setOnResultListener(new IRESTResultListener<ResultInstall>() {
                    @Override
                    public void onResult(int i, ResultInstall r) {
                        if (r != null) {
                            setMsg(true, "проверка Install");
                        }else {
                            setMsg(false, "проверка Install");
                        }
                    }
                });

        g.rest_install(bp);
    }
    void mRead()
    {
        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();

        ReadParameters bp = new ReadParameters().setCnt(getApplicationContext())
                .setLm(getSupportLoaderManager())
                .setCallDestination("")
                .setPass("1111")
                .setLogin("test")
                .setAsincStart(true)
                .setForceStart(false)
                .setReadType(IApi.RestReadType.GUID)
                .setOnResultListener(new IRESTResultListener<ResultRead>() {
                    @Override
                    public void onResult(int i_code, ResultRead r) {
                        if (r != null) {
                            String tmp = "";
                            if(r.guids != null) {
                                for (Iterator<ResultRead.ItemParams> elem = r.guids.iterator(); elem.hasNext(); ) {
                                    ResultRead.ItemParams i = elem.next();
                                    tmp += " " + i.guid + " [" + i.display_hight + "x" + i.display_width + "] " + " " + i.brand;
                                }
                            }
                            setMsg(true, "проверка read");
                        }else{
                            setMsg(false, "проверка read");
                        }
                    }
                });
        g.rest_read(bp);
    }
    void mDelete()
    {
        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
        //ResultDelete r = g.rest_delete(getApplicationContext(), getSupportLoaderManager(), "", "test", "1111");
        DeleteParameters bp = new DeleteParameters().setCnt(getApplicationContext())
                .setLm(getSupportLoaderManager())
                .setCallDestination("")
                .setPass("1111")
                .setLogin("test")
                .setAsincStart(true)
                .setForceStart(false)
                .setOnResultListener(new IRESTResultListener<ResultDelete>() {

                    @Override
                    public void onResult(int i, ResultDelete r) {
                        if (r != null) {
                            setMsg(true, "проверка delete");
                        }else {
                            setMsg(false, "проверка delete");
                        } }
                });

        ResultDelete r = g.rest_delete(bp);
    }
    /*****************************************************************
     ********* сбора информации обустройстве/инсталяции **************
     *****************************************************************/
    boolean mTestInstallationInfo()
    {
        PublisherIDMask m = new PublisherIDMask("GooGames,AppLandGames,AppClickGames,gameclub","_", ".zip,.apk");
        InstallationInfo ii = new InstallationInfo(getApplicationContext(), "", m);
        boolean res = ((ii.apk_guid != null) &&
                (ii.app_id != null) &&
                (ii.date != null) &&
                (ii.guid != null) &&
                (ii.installation_guid != null));
        return res;
    }

    boolean mTestDeviceInfo()
    {
        DeviceInfo.getDeviceInfo(getApplicationContext(), "");
        boolean res = true;
        res = ((res) && (DeviceInfo.android_id != null));
        res = ((res) && (DeviceInfo.brand != null));
        res = ((res) && (DeviceInfo.device != null));
        res = ((res) && (DeviceInfo.display_hight != null));
        res = ((res) && (DeviceInfo.display_width != null));
        res = ((res) && (DeviceInfo.guid != null));
        res = ((res) && (DeviceInfo.manufactor != null));
        res = ((res) && (DeviceInfo.model != null));
        res = ((res) && (DeviceInfo.product_id != null));
        res = ((res) && (DeviceInfo.version_os != null));
        res = ((res) && (DeviceInfo.versionName != null));
        return res;
    }
    /*****************************************************************
     *******************  Тестирование шифрования ********************
    *****************************************************************/
    boolean mTestCrypt()
    {
        String res = null;
        res = CryptoProviderServicer.cript(ResultTestCrypt.resDeCript);
        return res.equals(ResultTestCrypt.resCript);
    }

    boolean mTestDeCrypt()
    {
        String res = null;
        res = CryptoProviderServicer.deCript(ResultTestCrypt.resCript);
        return res.equals(ResultTestCrypt.resDeCript);
    }

    boolean mTestSetSharedPref()
    {
        String session = "test_session";
        String key = "test_key";
        SharedPreferencesServicer.setPreferences(getApplicationContext(),session, key, ResultTestCrypt.resDeCript);
        String res = SharedPreferencesServicer.getSimplePreferences(getApplicationContext(),session, key, null);
        return res.equals(ResultTestCrypt.resCript);
    }

    boolean mTestGetSharedPref()
    {
        String session = "test_session";
        String key = "test_key";
        SharedPreferencesServicer.setSimplePreferences(getApplicationContext(),session, key, ResultTestCrypt.resCript);
        String res = SharedPreferencesServicer.getPreferences(getApplicationContext(),session, key, "");
        return res.equals(ResultTestCrypt.resDeCript);
    }

}
