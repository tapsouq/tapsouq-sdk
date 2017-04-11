package com.tapsouq.sdk.ads;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;

import com.tapsouq.sdk.util.Log;

import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.COUNTRY;
import com.tapsouq.sdk.util.CountryCodeException;
import com.tapsouq.sdk.util.LANGUAGE;
import com.tapsouq.sdk.util.LanguageCodeException;
import com.tapsouq.sdk.util.PreferencesUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by dell on 8/7/2016.
 */
public class Device {

    public static String retrieveDeviceInfo(Context context) {

        String manefacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int osApiNumber = Build.VERSION.SDK_INT;
        String osVersion = android.os.Build.VERSION.RELEASE;

        //if no language put 0 by default
        String language = "0";
        try {
            //language = LANGUAGE.getLangugeNumber(Locale.getDefault().getDisplayLanguage()); big mistake return العربية
            language = LANGUAGE.getLangugeNumber(Locale.getDefault().getLanguage()); // return ar for arabic --- ok
        } catch (LanguageCodeException e) {
            Log.e(AD_CONST.LOG_TAG, e.getMessage());
            //e.printStackTrace();
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        //if no country put 0 by default
        String country = "0";
        try {
            String[] countryInfo = COUNTRY.getCountryInfo(tm.getSimCountryIso());
            country = countryInfo[0];
        } catch (CountryCodeException e) {
            Log.e(AD_CONST.LOG_TAG, e.getMessage());
            //e.printStackTrace();
        }

        String carrier = tm.getNetworkOperatorName();
        String sdkVersion = AD_CONST.TAP_SDK_VERSION;

        String deviceInfo = UrlGenerator.formatDeviceInfo2(manefacturer, model, String.valueOf(osApiNumber), osVersion,
                language, country, carrier, sdkVersion);

        return deviceInfo;

    }

    public static void createDevice(final Context context, final String googleAdvertisingId, final TapSouqAd tapSouqAd) {

        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                String deviceId = null;

                String googleAdvertisingId = params[0];
                //todo generate create device url


                String deviceInfo = Device.retrieveDeviceInfo(context);

                PreferencesUtils.setDeviceInfo(context, deviceInfo);
                PreferencesUtils.updateSdkVersion(context);

                String url = UrlGenerator.createDevice2(googleAdvertisingId, deviceInfo);
                //String url2 = UrlGenerator.updateDevice(Utils.readDeviceIdFromFile(context), Utils.getDeviceInfo(context));

                Log.d(AD_CONST.LOG_TAG, url);

                try {
                    URL u = new URL(url.replaceAll(" ", "%20"));

                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setRequestMethod("GET");

                    conn.connect();
                    InputStream is = conn.getInputStream();

                    BufferedReader r = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        sb.append(line);
                    }

                    Log.d(AD_CONST.LOG_TAG, sb.toString());

                    //{"status":true,"device_id":12}
                    String JSONResp = new String(sb.toString());

                    JSONObject jsonObject = new JSONObject(JSONResp);

                    boolean status = jsonObject.getBoolean("status");
                    if (status) {
                        deviceId = jsonObject.getString("device_id");
                    }

                } catch (Exception e) {
                    Log.d(AD_CONST.LOG_TAG, e.getMessage());
                    Log.e(AD_CONST.LOG_TAG, "Error while creating device.");
                    //e.printStackTrace();

                }
                return deviceId;
            }

            @Override
            protected void onPostExecute(String deviceId) {
                if (deviceId != null) {
                    PreferencesUtils.setGoogleAdvertisingID(context, googleAdvertisingId);
                    PreferencesUtils.saveDeviceId(context, deviceId);
                    boolean success = Device.storeDeviceIdInFile(deviceId);
                    if (!success)
                        Log.d(AD_CONST.LOG_TAG, "Error while saving device id to file");
                    tapSouqAd.load();
                }
            }
        };
        task.execute(googleAdvertisingId);
    }


    public static void updateDevice(final Context context, String deviceId) {

        AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {

                String deviceId = params[0];

                String deviceInfo = PreferencesUtils.getDeviceInfo(context);

                String url = UrlGenerator.updateDevice2(deviceId, deviceInfo);

                Log.d(AD_CONST.LOG_TAG, url);

                try {
                    URL u = new URL(url.replaceAll(" ", "%20"));

                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setRequestMethod("GET");

                    conn.connect();
                    InputStream is = conn.getInputStream();

                    BufferedReader r = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        sb.append(line);
                    }

                    Log.d(AD_CONST.LOG_TAG, sb.toString());

                    String JSONResp = new String(sb.toString());

                    JSONObject jsonObject = new JSONObject(JSONResp);

                    boolean status = jsonObject.getBoolean("status");

                } catch (Exception e) {
                    Log.d(AD_CONST.LOG_TAG, e.getMessage());
                    Log.e(AD_CONST.LOG_TAG, "Error while updating device.");
                    //e.printStackTrace();

                }
                return null;
            }
        };
        task.execute(deviceId);
    }

    public static void checkDeviceInfo(Context context) {

        if (!AD_CONST.TAP_SDK_VERSION.equals(PreferencesUtils.getSdkVersion(context))) {

            String deviceInfo = Device.retrieveDeviceInfo(context);
            PreferencesUtils.setDeviceInfo(context, deviceInfo);
            PreferencesUtils.updateSdkVersion(context);
            PreferencesUtils.updateDeviceInfoUpdateTime(context);
            Device.updateDevice(context, PreferencesUtils.getDeviceId(context));

            // todo : need to make sure sdk update to sever is successful
        }
        if (System.currentTimeMillis() - PreferencesUtils.getDeviceInfoUpdateTime(context) > DateUtils.DAY_IN_MILLIS * 7) {

            PreferencesUtils.updateDeviceInfoUpdateTime(context);

            String deviceInfo = Device.retrieveDeviceInfo(context);

            if (!deviceInfo.equals(PreferencesUtils.getDeviceInfo(context))) {

                PreferencesUtils.setDeviceInfo(context, deviceInfo);
                Device.updateDevice(context, PreferencesUtils.getDeviceId(context));
            }
        }
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //todo: sync device id in preferences with device id in file each day or week
    public static String readDeviceIdFromFile() {
        try {

            File sdcard = Environment.getExternalStorageDirectory();
            File appFolder = new File(sdcard, AD_CONST.APP_EXTERNAL_FOLDER);

            if (!appFolder.exists()) {
                return null;
            }

            File idFile = new File(appFolder, AD_CONST.ID_FILE);
            if (!idFile.exists()) {
                return null;
            } else {
                //open file and read id
                StringBuilder text = new StringBuilder();

                BufferedReader br = new BufferedReader(new FileReader(idFile));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
                br.close();
                Log.d(AD_CONST.LOG_TAG, "read from file: " + text.toString());
                return text.toString();
            }
        } catch (IOException e) {
            Log.d(AD_CONST.LOG_TAG, e.getMessage());
            Log.e(AD_CONST.LOG_TAG, "Error while reading file");
            //e.printStackTrace();
        }
        return null;
    }

    public static boolean storeDeviceIdInFile(String deviceId) {
        try {

            File sdcard = Environment.getExternalStorageDirectory();
            File appFolder = new File(sdcard, AD_CONST.APP_EXTERNAL_FOLDER);

            if (!appFolder.exists()) {
                appFolder.mkdir();
            }

            File idFile = new File(appFolder, AD_CONST.ID_FILE);
            BufferedWriter bw = new BufferedWriter(new FileWriter(idFile));
            bw.write(String.format("DID:%s", deviceId));
            bw.close();
            Log.d(AD_CONST.LOG_TAG, "write to file.");

            return true;
        } catch (IOException e) {
            Log.d(AD_CONST.LOG_TAG, e.getMessage());
            Log.e(AD_CONST.LOG_TAG, "Error while writing to file.");
            //e.printStackTrace();
        }
        return false;
    }

    public static String getDeviceIdFromFile() {
        String info = Device.readDeviceIdFromFile();
        String deviceId = "";
        if (info != null && info.startsWith("DID")) {
            String[] array = info.split(":");
            if (array.length == 2) {
                deviceId = array[1];
                return deviceId;
            }
        }
        return null;
    }

    public static String[] getCountryInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String[] countryInfo = null;
        try {
            countryInfo = COUNTRY.getCountryInfo(tm.getSimCountryIso());
            return countryInfo;
        } catch (CountryCodeException e) {
            Log.e(AD_CONST.LOG_TAG, e.getMessage());
            //e.printStackTrace();
        }

        return COUNTRY.getDefaultCountry();
    }


}
