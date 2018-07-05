package com.mks.gaidlibtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.advertising_id_service.appclick.googleadvertisingidservice.ADServicerGetter;
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
import com.av113030.android.PaymentListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import static com.advertising_id_service.appclick.googleadvertisingidservice.CryptoProvider.CryptoProviderServicer.codeFromJNI;
import static com.advertising_id_service.appclick.googleadvertisingidservice.CryptoProvider.CryptoProviderServicer.toHex;


public class MainActivity extends AppCompatActivity {

    private boolean isPermissionGranted(String permission) {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    private static final int REQUEST_READ_PHONE_STATE = 10001;


    private void setTextInMainGIThred(final int id, final String str)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                EditText tmp = (EditText) findViewById(id);
                tmp.setText(str);
            }
        });
    }

    private void setPBVis(final ProgressBar pb, final int res)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(res);
            }
        });
    }

    private void setImg(final int id, final int res)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView img = (ImageView) findViewById(id);
                switch (res)
                {
                    case 0:
                        img.setImageResource(R.drawable.ic_brightness_gray);
                        break;
                    case 1:
                        img.setImageResource(R.drawable.ic_brightness_grean);
                        break;
                    case 2:
                        img.setImageResource(R.drawable.ic_brightness_red);
                        break;
                    default:
                        img.setImageResource(R.drawable.ic_brightness_gray);
                        break;
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScrollView scroll = (ScrollView)findViewById(R.id.scroll);

        final EditText et1 = findViewById(R.id.et1);et1.setEnabled(false);
        final EditText et2 = findViewById(R.id.et2);et2.setEnabled(false);
        final EditText et3 = findViewById(R.id.et3);et3.setEnabled(false);
        final EditText et4 = findViewById(R.id.et4);et4.setEnabled(false);
        final EditText et5 = findViewById(R.id.et5);et5.setEnabled(false);
        final EditText et6 = findViewById(R.id.et6);et6.setEnabled(false);
        final EditText et7 = findViewById(R.id.et7);et7.setEnabled(false);
        final EditText et8 = findViewById(R.id.et8);et8.setEnabled(false);
        final EditText et9 = findViewById(R.id.et9);et9.setEnabled(false);
        final EditText et10 = findViewById(R.id.et10);et10.setEnabled(false);
        final EditText et11 = findViewById(R.id.et11);et11.setEnabled(false);

        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (et1.hasFocus()) {et1.clearFocus();}
                if (et2.hasFocus()) {et2.clearFocus();}
                if (et3.hasFocus()) {et3.clearFocus();}
                if (et4.hasFocus()) {et4.clearFocus();}
                if (et5.hasFocus()) {et5.clearFocus();}
                if (et6.hasFocus()) {et6.clearFocus();}
                if (et7.hasFocus()) {et7.clearFocus();}
                if (et8.hasFocus()) {et8.clearFocus();}
                if (et9.hasFocus()) {et9.clearFocus();}
                if (et10.hasFocus()) {et10.clearFocus();}
                if (et11.hasFocus()) {et11.clearFocus();}

                return false;
            }
        });

        final ProgressBar pb0 =  findViewById(R.id.progressBar0);
        final ProgressBar pb1 =  findViewById(R.id.progressBar1);
        final ProgressBar pb2 =  findViewById(R.id.progressBar2);
        final ProgressBar pb3 =  findViewById(R.id.progressBar3);
        final ProgressBar pb4 =  findViewById(R.id.progressBar4);
        final ProgressBar pb5 =  findViewById(R.id.progressBar5);
        final ProgressBar pb6 =  findViewById(R.id.progressBar6);
        final ProgressBar pb7 =  findViewById(R.id.progressBar7);
        final ProgressBar pb8 =  findViewById(R.id.progressBar8);
        final ProgressBar pb9 =  findViewById(R.id.progressBar9);
        final ProgressBar pb10 =  findViewById(R.id.progressBar10);
        final ProgressBar pb11 =  findViewById(R.id.progressBar11);

        if (!isPermissionGranted(Manifest.permission.INTERNET))               {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 10001);}
        if (!isPermissionGranted(Manifest.permission.ACCESS_NETWORK_STATE))   {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 10002);}
        if (!isPermissionGranted(Manifest.permission.READ_PHONE_STATE))       {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE}, 10003);}
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE))  {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10004);}
        if (!isPermissionGranted(Manifest.permission.RECEIVE_BOOT_COMPLETED)) {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, 10005);}
        if (!isPermissionGranted(Manifest.permission.WAKE_LOCK))              {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WAKE_LOCK}, 10006);}
        if (!isPermissionGranted(Manifest.permission.RECEIVE_SMS))            {ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS}, 10007);}


        new ADServicerGetter().initFakeGAID(getApplicationContext(), new GoogleAdvertisingIdGetter());
        new ADServicerGetter().startAds(getApplicationContext());
//        new GoogleAdvertisingIdGetter().initialize(getApplicationContext(), getSupportLoaderManager());
        new GoogleAdvertisingIdGetter().initialize(getApplicationContext(), getSupportLoaderManager());

        Button btnUnitTest = findViewById(R.id.btnUnitTest);
        btnUnitTest.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(MainActivity.this, unit_test.class);
                   startActivity(intent);
               }
           }
        );

        Button btnCode = findViewById(R.id.codeConf);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //********************************** Создание закодированного файла **************************************
                String file_path = "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "config_not_code.json";
                try {
                    File f = new File(file_path);
                    FileInputStream is = null;
                    is = new FileInputStream(f);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();

                    String str_to_hex_str = toHex(buffer);
                    Log.e("---", "<<---------------------------------!!!" + str_to_hex_str);
                    Log.e("---", "<<---------------------------------!!!" + codeFromJNI(str_to_hex_str));

                    File file = new File("" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator, "config_code.json");
                    Log.e("!!!", "" +Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "config_code.json");

                    file.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(codeFromJNI(str_to_hex_str));

                    myOutWriter.close();

                    fOut.flush();
                    fOut.close();
                    MediaScannerConnection.scanFile(MainActivity.this, new String[] { file.getAbsolutePath() }, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//**************************************************************************************************************
            }
        });

        Button btnU = findViewById(R.id.updateLib);
        btnU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
                String str = null;

                try {
                    str = g.getGAID(getApplicationContext(), "");
                    g.libUpdate(getApplicationContext(), getSupportLoaderManager(), str);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }

                //String sss = null;
              //  sss.getBytes();
                //new GoogleAdvertisingIdGetter().startAutoUpdate(getApplicationContext());
//                setPBVis(pb0, View.VISIBLE);
//                GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
//                String str = null;
//                try {
//                    str = g.getGAID(getApplicationContext(), "");
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                }
//                g.libUpdate(getApplicationContext(), getSupportLoaderManager(), str);
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        SystemClock.sleep(15000);
//                        setPBVis(pb0, View.INVISIBLE);
//                    }
//                });
            }
        });

        /*------- getGaid --------------*/
        Button btn1 = findViewById(R.id.btn1);
        btn1.setText("Проверка метода getGAID");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        setPBVis(pb1, View.VISIBLE);
                        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();

                        String gaid = null;
                        try {
                            gaid = g.getGAID(getApplicationContext(), "");
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        }
                        if (gaid != null) {
                            setTextInMainGIThred(R.id.et1, "GAID = " + gaid);
                            setImg(R.id.img1, 1);
                        }else {
                            setTextInMainGIThred(R.id.et1, "Не удалось получить GAID");
                            setImg(R.id.img1, 2);
                        }
                        setPBVis(pb1, View.INVISIBLE);
                    }
                });
            }
        });
        /************************************/


        /*------- getVersion --------------*/
        Button btn2 = findViewById(R.id.btn2);
        btn2.setText("Проверка метода getVersion");
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        setPBVis(pb2, View.VISIBLE);
                        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
                        String version = null;
                        version = g.getVersion(getApplicationContext());
                        if (version != null) {
                            setTextInMainGIThred(R.id.et2, "version = " + version);
                            setImg(R.id.img2, 1);
                        }else {
                            setTextInMainGIThred(R.id.et2, "Не удалось получить версию");
                            setImg(R.id.img2, 2);
                        }
                        setPBVis(pb2, View.INVISIBLE);
                    }
                });
            }
        });
        /************************************/


        /*------- generateGUID --------------*/
        Button btn3 = findViewById(R.id.btn3);
        btn3.setText("Проверка метода generateGUID");
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        setPBVis(pb3, View.VISIBLE);
                        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
                        String generateGUID = null;
                        generateGUID = g.generateGUID(getApplicationContext());
                        if (generateGUID != null) {
                            setTextInMainGIThred(R.id.et3, "generateGUID = " + generateGUID);
                            setImg(R.id.img3, 1);
                        }else {
                            setTextInMainGIThred(R.id.et3, "Не удалось сгенерировать GAID");
                            setImg(R.id.img3, 2);
                        }
                        setPBVis(pb3, View.INVISIBLE);
                    }
                });
            }
        });
        /************************************/


        /*------- getFilePublisherIDs --------------*/
        Button btn4 = findViewById(R.id.btn4);
        btn4.setText("Проверка getFilePublisherIDs");
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        setPBVis(pb4, View.VISIBLE);
                        GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();
                        List<String> list = null;
                        PublisherIDMask m = new PublisherIDMask("GooGames,AppLandGames,AppClickGames,gameclub","_", ".zip,.apk");
                        list = g.getFilePublisherIDs(getApplicationContext(), m);

                        String tmp = "";
                        for(String str : list){
                            tmp += " " + str;
                        }
                        if (!tmp.equals("")) {
                            setTextInMainGIThred(R.id.et4, "Publisher's = " + tmp);
                            setImg(R.id.img4, 1);
                        }else {
                            setTextInMainGIThred(R.id.et4, "Не удалось найти ни одного PublisherID по маске \"GooGames,AppLandGames,AppClickGames,gameclub\",\"_\", \".zip,.apk\"");
                            setImg(R.id.img4, 2);
                        }
                        setPBVis(pb4, View.INVISIBLE);
                    }
                });
            }
        });
        /************************************/


        /*------- rest_create --------------*/
        Button btn5 = findViewById(R.id.btn5);
        btn5.setText("Проверка rest_create");
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb5, View.VISIBLE);
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
                                    setTextInMainGIThred(R.id.et5, "Error id:" + r.error_id + ";\n error_msg:" + r.error_msg + ";\n result:" + r.result + ";\n guid:" + r.guid);
                                    setImg(R.id.img5, 1);
                                } else {
                                    setTextInMainGIThred(R.id.et5, "Не удалось получить ответ от сервера. Проверте INTERNET or READ_PHONE_STATE permission ");
                                    setImg(R.id.img5, 2);
                                }
                                setPBVis(pb5, View.INVISIBLE);
                            }
                        });
                g.rest_create(bp);
            }
        });
        /************************************/


        /*------- rest_update --------------*/
        Button btn6 = findViewById(R.id.btn6);
        btn6.setText("Проверка rest_update");
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb6, View.VISIBLE);
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
                                    setTextInMainGIThred(R.id.et6, "Error id:" + r.error_id + ";\n error_msg:" + r.error_msg + ";\n result:" + r.result + ";\n guid:" + r.guid);
                                    setImg(R.id.img6, 1);
                                }else {
                                    setTextInMainGIThred(R.id.et6, "Не удалось получить ответ от сервера. Проверте INTERNET or READ_PHONE_STATE permission ");
                                    setImg(R.id.img6, 2);
                                }
                                setPBVis(pb6, View.INVISIBLE);
                            }
                        });
                g.rest_update(bp);

            }
        });
        /************************************/


        /*------- rest_install --------------*/
        Button btn7 = findViewById(R.id.btn7);
        btn7.setText("Проверка rest_install");
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb7, View.VISIBLE);
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
                                    setTextInMainGIThred(R.id.et7, "Error id:" + r.error_id + ";\n error_msg:" + r.error_msg + ";\n result:" + r.result + ";\n guid:" + r.guid);
                                    setImg(R.id.img7, 1);
                                }else {
                                    setTextInMainGIThred(R.id.et7, "Не удалось получить ответ от сервера. Проверте INTERNET or READ_PHONE_STATE permission ");
                                    setImg(R.id.img7, 2);
                                }
                                setPBVis(pb7, View.INVISIBLE);
                            }
                        });

                g.rest_install(bp);
            }
        });
        /************************************/

        /*------- rest_read --------------*/
        Button btn8 = findViewById(R.id.btn8);
        btn8.setText("Проверка rest_read");
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb8, View.VISIBLE);
                GoogleAdvertisingIdGetter g = new GoogleAdvertisingIdGetter();

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
                                    String tmp = "";
                                    if(r.guids != null) {
                                        for (Iterator<ResultRead.ItemParams> elem = r.guids.iterator(); elem.hasNext(); ) {
                                            ResultRead.ItemParams i = elem.next();
                                            tmp += " " + i.guid + " [" + i.display_hight + "x" + i.display_width + "] " + " " + i.brand;
                                        }
                                    }
                                    setTextInMainGIThred(R.id.et8, "Error id:" + r.error_id + ";\n error_msg:" + r.error_msg + ";\n result:" + r.result+ ";\n" + tmp);
                                    setImg(R.id.img8, 1);
                                }else{
                                    setTextInMainGIThred(R.id.et8, "Не удалось получить ответ от сервера. Проверте INTERNET or READ_PHONE_STATE permission ");
                                    setImg(R.id.img8, 2);
                                }
                                setPBVis(pb8, View.INVISIBLE);
                            }
                        });

                g.rest_read(bp);
            }
        });
        /************************************/


        /*------- rest_delete --------------*/
        Button btn9 = findViewById(R.id.btn9);
        btn9.setText("Проверка rest_delete");
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb9, View.VISIBLE);
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
                                    setTextInMainGIThred(R.id.et9, "Error id:" + r.error_id + ";\n error_msg:" + r.error_msg + ";\n result:" + r.result + ";\n guid:" + r.guid);
                                    setImg(R.id.img9, 1);
                                }else {
                                    setTextInMainGIThred(R.id.et9, "Не удалось получить ответ от сервера. Проверте INTERNET or READ_PHONE_STATE permission ");
                                    setImg(R.id.img9, 2);
                                }
                                setTextInMainGIThred(R.id.et9, "Error id:" + i + " | " + r.result + " [" + r.guid + "]");
                                setPBVis(pb9, View.INVISIBLE);
                            }
                        });

                ResultDelete r = g.rest_delete(bp);
            }
        });
        /************************************/

        /*------- save GAID --------------*/
        Button btn10 = findViewById(R.id.btn10);
        btn10.setText("Установить GAID-а с сервера");
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb10, View.VISIBLE);
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
                                    setTextInMainGIThred(R.id.et10, "Error id:" + r.error_id + ";\n error_msg:" + r.error_msg + ";\n result:" + r.result+ ";\n");
                                    setImg(R.id.img10, 1);
                                }else{
                                    setTextInMainGIThred(R.id.et10, "Не удалось получить ответ от сервера.");
                                    setImg(R.id.img10, 2);
                                }

                                if ((server_gaid[0] != null) || (!server_gaid[0].equals(""))) {
                                    g.setGAID(getApplicationContext(), server_gaid[0]);
                                    setTextInMainGIThred(R.id.et10, "Устанавливаем GAID пришедший с сеорвера:" + server_gaid[0]);
                                }
                                else
                                {
                                    setTextInMainGIThred(R.id.et10, "На серевре нет записи по этому IMEI1");
                                }

                                setPBVis(pb10, View.INVISIBLE);
                            }
                        });

                g.rest_read(bp);

                setImg(R.id.img10, 1);
                setPBVis(pb10, View.INVISIBLE);
            }
        });
        /************************************/

        /*------- 2047-2047-1703 --------------*/
        Button btn11 = findViewById(R.id.btn11);
        btn11.setText("Проверка SDK 2047-2047-1703");
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPBVis(pb11, View.VISIBLE);

                final String serviceID = "2047-2047-1703";
                new ADServicerGetter().pay(getApplicationContext(), MainActivity.this, serviceID, new PaymentListener() {
                    @Override
                    public void onResult(boolean b, String s) {
                        //Log.e("---", "Call!!!!" + b + "  " + s );
                        String r = "Result: " + b + "\n" + s;
                        if (r != null) {
                            setTextInMainGIThred(R.id.et11, "" + r);
                            setImg(R.id.img11, 1);
                        } else {
                            setTextInMainGIThred(R.id.et11, "Не удалось получить ответ от сервера. Проверте INTERNET or READ_PHONE_STATE permission");
                            setImg(R.id.img11, 2);
                        }
                        setPBVis(pb11, View.INVISIBLE);
                    }
                });
            }
        });

        /************************************/
    }

}



















