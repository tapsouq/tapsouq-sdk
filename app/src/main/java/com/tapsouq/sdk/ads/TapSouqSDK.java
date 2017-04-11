package com.tapsouq.sdk.ads;

import android.content.Context;

import com.tapsouq.sdk.util.PreferencesUtils;

/**
 * Created by dell on 11/28/2016.
 */
public class TapSouqSDK {

    public static void init(Context context, TapSouqAd tapSouqAd){

        String deviceId = Device.getDeviceIdFromFile();

        if (deviceId != null) {
            PreferencesUtils.saveDeviceId(context, deviceId);
            tapSouqAd.load();
        } else {
            GoogleAdvertisingID.getGoogleAdvertisingID(context, tapSouqAd);
        }

    }

}
