package com.tapsouq.sdk.ads;

import android.content.Context;
import android.content.Intent;

import com.tapsouq.sdk.util.Log;

import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.PreferencesUtils;
import com.tapsouq.sdk.util.SdkId;
import com.tapsouq.sdk.util.TrackingInfo;
import com.tapsouq.sdk.task.AdActionAsyncTask;
import com.tapsouq.sdk.task.InterstitialAdRequestAsyncTask;

/**
 * Created by dell on 7/28/2016.
 */
public class TapSouqInterstitialAd implements TapSouqAd {


    private static TapSouqInterstitialAd interstitialAd;
    private boolean testMode;

    // todo: handle the case when developer need to load many interstitial in cache
    public static void setInterstitialAd(TapSouqInterstitialAd ad) {
        interstitialAd = ad;
    }

    public static TapSouqInterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public static final int INTERSTITIAL_WIDTH = 320;
    public static final int INTERSTITIAL_HEIGHT = 480;

    public static final int INTERSTITIAL_APP_ICON = 250;

    private final Context context;

    private TapSouqListener listener;

    private String adUnitID;
    private AdCreative adCreative;
    private String deviceId;
    private String requestId;
    private String adPlacementId;
    private boolean adLoaded;

    private String letters;


    public Context getContext() {
        return context;
    }

    public TapSouqListener getListener() {
        return listener;
    }

    public String getadUnitID() {
        return adUnitID;
    }


    public TapSouqInterstitialAd(Context context) {
        this.context = context;

    }

    public void setListener(TapSouqListener listener) {
        this.listener = listener;
    }


    public void setAdUnitID(String id) {
        this.adUnitID = id;
    }

    //2. load ad
    @Override
    public void load() {

        // todo check network connection
        //check adunit set!
        //load device info()
        //load relevant keywords()  !!!


        letters = SdkId.generateRandomLetters();
        Log.d(AD_CONST.LOG_TAG, "Letter: " + letters);

        deviceId = PreferencesUtils.getDeviceId(context);

        if (deviceId == null) {
            Log.d(AD_CONST.LOG_TAG, "Device id not created yet, will create device...");
            TapSouqSDK.init(getContext(), this);
            return;
        } else {
            Device.checkDeviceInfo(getContext());
            TrackingInfo.trackConversions(getContext(), this);
        }

        if (isTestMode() || PreferencesUtils.isFreqCapPassed(context, adUnitID)) {
            int actionName = AD_CONST.ACTION_AD_REQUEST;
            requestId = "0";
            adPlacementId = adUnitID;
            adCreative = new AdCreative();
            adCreative.setId("0");
            PreferencesUtils.setLastRequestMillis(context, adUnitID);
            sendAction(actionName);
            Log.i(AD_CONST.LOG_TAG, "Loading ad unit " + adUnitID);
        } else if (!PreferencesUtils.isFreqCapPassed(context, adUnitID)){
            if(listener != null)
                listener.adFailed();
        }
    }


    //3. show ad
    public void showAd() {


        if (adCreative.getImageFile() != null && !"".equals(adCreative.getImageFile())) {
            TapSouqInterstitialAd.setInterstitialAd(this);
            Intent intent = new Intent(context, TapSouqInterstitialView.class);
            context.startActivity(intent);
            Log.i(AD_CONST.LOG_TAG, "Showing ad unit " + adUnitID);
            if (listener != null)
                listener.adShown();

            sendAction(AD_CONST.ACTION_AD_SHOWN);

        } else {
            // todo: handle, interstitial not loaded

        }

    }

    //4. ad clicked
    public void adClicked() {
        Log.i(AD_CONST.LOG_TAG, "Clicked ad unit " + adUnitID);
        sendAction(AD_CONST.ACTION_AD_CLICKED);

        sendAction(AD_CONST.ACTION_TRACK_INSTALL);

        if (listener != null)
            listener.adClicked();

    }

    //5. ad closed
    public void adClosed() {
        if (listener != null)
            listener.adClosed();

    }

    ////supporting methods ////////////////////////////////////
    private void sendAction(int actionName) {

        String[] countryInfo = Device.getCountryInfo(getContext());
        String countryId = countryInfo[0];
        String countryTier = countryInfo[3];

        String testValue = "";
        if (testMode) {
            testValue = "interstitial";
            requestId = "1000000";
        }

        String finalUrl = UrlGenerator.getActionUrl2(deviceId, actionName, requestId, adPlacementId,
                adCreative.getId(), getContext().getPackageName(), PreferencesUtils.getShownCreativeParams(getContext()),
                adCreative.getAppId(), adCreative.getAppUserId(), adCreative.getCampId(), adCreative.getCampUserId(),
                countryId, countryTier, AD_CONST.TAP_SDK_VERSION, testValue);


        Log.d(AD_CONST.LOG_TAG, finalUrl);
        if (actionName == AD_CONST.ACTION_AD_REQUEST) {
            new InterstitialAdRequestAsyncTask(this).execute(finalUrl);

        } else if (actionName == AD_CONST.ACTION_TRACK_INSTALL) {

            //TODO STORE CONVERSION INFO
            String[] array = adCreative.getClickUrl().split("=");
            String packageName = "";
            if (array.length == 2)
                packageName = array[1];
            else
                return;
            TrackingInfo trackingInfo = new TrackingInfo(packageName, finalUrl, System.currentTimeMillis());
            TrackingInfo.storeTrackingInfo(getContext(), trackingInfo);

        } else {
            new AdActionAsyncTask().execute(finalUrl);
        }
    }

    ////server requests api/////////////////////////////////

    public AdCreative getAdCreative() {
        return adCreative;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public boolean isAdLoaded() {
        return adLoaded;
    }

    public void setAdLoaded(boolean b) {
        adLoaded = b;
    }

    @Override
    public void appInstalled(String installUrl) {
        new AdActionAsyncTask().execute(installUrl);
    }

    public boolean isTestMode() {
        Log.d(AD_CONST.LOG_TAG, adUnitID + " test mode value: " + testMode);
        if (testMode) {
            Log.i(AD_CONST.LOG_TAG, "Test mode is true for ad unit " + adUnitID);
        }
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

}