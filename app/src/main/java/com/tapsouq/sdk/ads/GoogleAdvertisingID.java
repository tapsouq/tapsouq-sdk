package com.tapsouq.sdk.ads;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

import com.tapsouq.sdk.util.PreferencesUtils;

/**
 * Created by dell on 11/20/2016.
 */
public class GoogleAdvertisingID {

    public static void getGoogleAdvertisingID(final Context context, final TapSouqAd tapSouqAd) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                } catch (GooglePlayServicesNotAvailableException e) {
                    //e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    //e.printStackTrace();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                }

                return advertId;
            }

            @Override
            protected void onPostExecute(String googleAdvertisingId) {
                String oldGoogleAdvertisingId = PreferencesUtils.getGoogleAdvertisingIdFromPreferences(context);
                if (googleAdvertisingId != null && !googleAdvertisingId.equals(oldGoogleAdvertisingId)) {
                    Device.createDevice(context, googleAdvertisingId, tapSouqAd);

                }
            }
        };

        task.execute();
    }
}
