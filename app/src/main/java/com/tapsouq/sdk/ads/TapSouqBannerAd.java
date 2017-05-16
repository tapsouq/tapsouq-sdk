package com.tapsouq.sdk.ads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapsouq.sdk.R;
import com.tapsouq.sdk.task.AdActionAsyncTask;
import com.tapsouq.sdk.task.BannerAdRequestAsyncTask;
import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.Log;
import com.tapsouq.sdk.util.PreferencesUtils;
import com.tapsouq.sdk.util.SdkId;
import com.tapsouq.sdk.util.TrackingInfo;

/**
 * Created by dell on 7/28/2016.
 */
public class TapSouqBannerAd extends LinearLayout implements TapSouqAd {

    private static final int BANNER_WIDTH = 320;
    private static final int BANNER_HEIGHT = 50;

    private static final int ICON_SIZE = 48;

    private boolean testMode = false;

//    private ImageView imageView;
//    private TextView textView;

    private TapSouqListener listener;
    private String adUnitID;
    private AdCreative adCreative;
    private String deviceId;
    private String requestId;
    private String adPlacementId;
    private int refreshInterval = 60; // default value, will be overridded by returned server value
    private boolean adLoaded;

    private String letters;

    public String getAdUnitID() {
        return adUnitID;
    }

    public void setRefreshInterval(int refreshInterval) {
        if (refreshInterval < 20)
            refreshInterval = 20;
        this.refreshInterval = refreshInterval;
    }

    public TapSouqBannerAd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        getLayoutParams().height = (int) (50 * getResources().getDisplayMetrics().density);
        requestLayout();
    }


    //1. set adunit id
    public void setAdUnitID(String id) {
        this.adUnitID = id;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getAdCreative().getClickUrl()));
                getContext().startActivity(intent);
                //call for test
                adClicked();
            }
        });
    }

    public void setListener(TapSouqListener listener) {
        this.listener = listener;
    }

    public TapSouqListener getListener() {
        return listener;
    }

    //2. load ad
    @Override
    public void load() {

        // todo
        //check network connection
        //check adunit set!
        //load device info()
        //load relevant keywords()  !!!

        letters = SdkId.generateRandomLetters();
        Log.d(AD_CONST.LOG_TAG, "Letter: " + letters);

        deviceId = PreferencesUtils.getDeviceId(getContext());
        if (deviceId == null) {
            Log.d(AD_CONST.LOG_TAG, "Device id not created yet, will create device...");
            TapSouqSDK.init(this.getContext(), this);
            return;
        } else { //todo: use interface listener as param to update after status=true
            Device.checkDeviceInfo(getContext());
            TrackingInfo.trackConversions(this.getContext(), this);
        }
        // todo: get refresh interval from json
        if (isTestMode() || PreferencesUtils.isFreqCapPassed(getContext(), adUnitID)) {

            int actionName = AD_CONST.ACTION_AD_REQUEST;
            requestId = "0";
            adPlacementId = adUnitID;
            adCreative = new AdCreative();
            adCreative.setId("0");
            PreferencesUtils.setLastRequestMillis(getContext(), adUnitID);
            sendAction(actionName);
            Log.i(AD_CONST.LOG_TAG, "Loading ad unit " + adUnitID);
        } else if (!PreferencesUtils.isFreqCapPassed(getContext(), adUnitID)){
            if(listener != null)
                listener.adFailed();
        }
    }

    @Override
    public void appInstalled(String installUrl) {
        new AdActionAsyncTask().execute(installUrl);
    }

//    public void load1() {
//
//        //check network connection
//        //check adunit set!
//        //load device info()
//        //load relevant keywords()  !!!
//
//        deviceId = PreferencesUtils.getDeviceId(getContext());
//        if (deviceId == null) {
//            Log.i("TAP_SOUQ", "device id not created yet, will create device...");
//            TapSouqSDK.init(this.getContext(), this);
//            return;
//        } else { //todo: use interface listener as param to update after status=true
//            Device.checkDeviceInfo(getContext());
//        }
//        // todo: get refresh interval from json
//        if (refreshInterval > 0) {
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //if this activity is top
//                    boolean activityShown = (((Activity) getContext()).getWindow().getDecorView().getRootView().isShown());
//
//                    if (activityShown) {
//
//                        String actionName = AD_CONST.ACTION_AD_REQUEST;
//                        requestId = "0";
//                        adPlacementId = adUnitID;
//                        adCreative = new AdCreative();
//                        adCreative.setId("0");
//                        sendAction(actionName);
//
//
//                        Log.d(AD_CONST.LOG_TAG, adUnitID + ", activity shown, refresh banner ");
//                    } else {
//                        Log.d(AD_CONST.LOG_TAG, adUnitID + ", activity not shown");
//                    }
//
//                    //todo: we may enable the handler in the future to enable banner refreshing frequently
//                    //handler.postDelayed(this, refreshInterval * DateUtils.SECOND_IN_MILLIS);
//
//                }
//            }, 0);
//        }
//
//    }

    //3. show ad
    public void showAd() {

        if (adCreative.getType() == AdCreative.AD_TYPE_IMAGE) {

            if (adCreative.getImageFile() != null && !"".equals(adCreative.getImageFile())) {

                ImageView view = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.image_banner, null);
                this.removeAllViews();
                this.addView(view);

                Glide.with(getContext()).load(adCreative.getImageFileUrl()).into(view);
                Log.i(AD_CONST.LOG_TAG, "Showing ad unit " + adUnitID);

                if (listener != null)
                    listener.adShown();

                PreferencesUtils.setShownCreative(getContext(), adCreative.getId());
                sendAction(AD_CONST.ACTION_AD_SHOWN);

            }
        } else if (adCreative.getType() == AdCreative.AD_TYPE_TEXT) {
            // banner text
            View view = LayoutInflater.from(getContext()).inflate(R.layout.text_banner, null);
            this.removeAllViews();
            this.addView(view);
            TextView titleView = (TextView) view.findViewById(R.id.text_title);
            titleView.setText(adCreative.getTitle());

            TextView descriptionView = (TextView) view.findViewById(R.id.text_description);
            descriptionView.setText(adCreative.getDescription());

            ImageView adIcon = (ImageView) view.findViewById(R.id.ad_icon);
            Glide.with(getContext()).load(adCreative.getImageFileUrl()).into(adIcon);

            Log.i(AD_CONST.LOG_TAG, "Showing ad unit " + adUnitID);

            if (listener != null)
                listener.adShown();

            PreferencesUtils.setShownCreative(getContext(), adCreative.getId());
            sendAction(AD_CONST.ACTION_AD_SHOWN);

        }

    }

    //4. ad clicked
    private void adClicked() {
        sendAction(AD_CONST.ACTION_AD_CLICKED);
        Log.i(AD_CONST.LOG_TAG, "Clicked ad unit " + adUnitID);


        sendAction(AD_CONST.ACTION_TRACK_INSTALL);

        if (listener != null)
            listener.adClicked();

    }

    ////supporting methods ////////////////////////////////////
    private void sendAction(int actionName) {

        String[] countryInfo = Device.getCountryInfo(getContext());
        String countryId = countryInfo[0];
        String countryTier = countryInfo[3];
        String testValue = "";

        if(testMode) {
            testValue = "banner";
            requestId = "1000000";

        }


        String finalUrl = UrlGenerator.getActionUrl2(deviceId, actionName, requestId, adPlacementId,
                adCreative.getId(), getContext().getPackageName(), PreferencesUtils.getShownCreativeParams(getContext()),
                adCreative.getAppId(), adCreative.getAppUserId(), adCreative.getCampId(), adCreative.getCampUserId(),
                countryId, countryTier, AD_CONST.TAP_SDK_VERSION, testValue);

        Log.d(AD_CONST.LOG_TAG, finalUrl);
        if (actionName == AD_CONST.ACTION_AD_REQUEST)
            new BannerAdRequestAsyncTask(this).execute(finalUrl);

        else if (actionName == AD_CONST.ACTION_TRACK_INSTALL && !testMode) {
            //TODO STORE CONVERSION INFO
            String[] array = adCreative.getClickUrl().split("=");
            String packageName = "";
            if (array.length == 2)
                packageName = array[1];
            else
                return;
            TrackingInfo trackingInfo = new TrackingInfo(packageName, finalUrl, System.currentTimeMillis());
            TrackingInfo.storeTrackingInfo(getContext(), trackingInfo);

        } else if(!testMode)
            new AdActionAsyncTask().execute(finalUrl);
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


    public boolean isTestMode() {
        Log.d(AD_CONST.LOG_TAG, adUnitID + " test mode value: " + testMode);
        if(testMode){
            Log.i(AD_CONST.LOG_TAG, "Test mode is true for ad unit " + adUnitID);
        }
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }
}