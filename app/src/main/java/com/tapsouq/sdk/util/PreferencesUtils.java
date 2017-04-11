package com.tapsouq.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

/**
 * Created by dell on 10/26/2016.
 */
public class PreferencesUtils {

    public static void saveDeviceId(Context context, String deviceId) {
        getSdkPrefs(context).edit().putString(DEVICE_ID_KEY(context), deviceId).commit();
    }

    public static String getDeviceId(Context context) {
        return getSdkPrefs(context).getString(DEVICE_ID_KEY(context), null);
    }

    public static void setGoogleAdvertisingID(Context context, String advertId) {
        SharedPreferences prefs = getSdkPrefs(context);
        prefs.edit().putString(AD_CONST.GOOGLE_ADVERTISING_ID_KEY, advertId).commit();
    }

    public static String getGoogleAdvertisingIdFromPreferences(Context context) {
        return getSdkPrefs(context).getString(AD_CONST.GOOGLE_ADVERTISING_ID_KEY, "NONE");
    }

    private static SharedPreferences getSdkPrefs(Context context) {
        return context.getSharedPreferences(AD_CONST.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private static String DEVICE_ID_KEY(Context context) {
        return getGoogleAdvertisingIdFromPreferences(context) + "_KEY";
    }

    public static String getShownCreative(Context context) {
        return getSdkPrefs(context).getString(AD_CONST.SHOWN_CREATIVE_IDS_KEY, "");
    }


    public static String getShownCreativeParams(Context context) {
        long millis = getLast24Millis(context);
        if (System.currentTimeMillis() - millis > DateUtils.DAY_IN_MILLIS) {
            return "";
        }

        String shown = getShownCreative(context);
        if ("".equals(shown))
            return "";

        return shown;
    }

    public static void setShownCreative(Context context, String id) {

        String shown = getShownCreative(context);

        long millis = getLast24Millis(context);
        if (System.currentTimeMillis() - millis > DateUtils.DAY_IN_MILLIS) {
            updateLast24Millis(context);
            shown = "";
        }

        if (shown.equals(""))
            shown = id;
        //todo: complicated if may cause fraud, better to convert this logit to list and loop
        else if (!(shown.equals(id) || shown.startsWith(id + ",") || shown.endsWith("," + id) || shown.contains("," + id + ","))) {
            shown += "," + id;
        }

        getSdkPrefs(context).edit().putString(AD_CONST.SHOWN_CREATIVE_IDS_KEY, shown).commit();
    }

    private static long getLast24Millis(Context context) {
        return getSdkPrefs(context).getLong(AD_CONST.LAST_24_MILLIS, 0);
    }

    private static void updateLast24Millis(Context context) {
        getSdkPrefs(context).edit().putLong(AD_CONST.LAST_24_MILLIS, System.currentTimeMillis()).commit();
    }

    public static void setDeviceInfo(Context context, String deviceInfo) {

        getSdkPrefs(context).edit().putString(AD_CONST.DEVICE_INFO, deviceInfo).commit();
    }

    public static String getDeviceInfo(Context context) {
        return getSdkPrefs(context).getString(AD_CONST.DEVICE_INFO, "NONE");
    }

    public static void updateDeviceInfoUpdateTime(Context context) {
        getSdkPrefs(context).edit().putLong(AD_CONST.DEVICE_INFO_UPDATE_TIME, System.currentTimeMillis()).commit();
    }

    public static long getDeviceInfoUpdateTime(Context context) {
        return getSdkPrefs(context).getLong(AD_CONST.DEVICE_INFO_UPDATE_TIME, 0);
    }

    public static void updateSdkVersion(Context context) {
        getSdkPrefs(context).edit().putString(AD_CONST.SDK_VERSION_KEY, AD_CONST.TAP_SDK_VERSION).commit();
    }

    public static String getSdkVersion(Context context) {
        return getSdkPrefs(context).getString(AD_CONST.SDK_VERSION_KEY, "");
    }

    public static long getLastRequestMillis(Context context, String adUnitID) {
        return getSdkPrefs(context).getLong(AD_CONST.LAST_REQUEST_MILLIS + adUnitID, 0);
    }

    public static void setLastRequestMillis(Context context, String adUnitID) {
        getSdkPrefs(context).edit().putLong(AD_CONST.LAST_REQUEST_MILLIS + adUnitID, System.currentTimeMillis()).commit();
    }

    public static boolean isFreqCapPassed(Context context, String adUnitID) {
        long lastTimePeriod = (System.currentTimeMillis() - PreferencesUtils.getLastRequestMillis(context, adUnitID));

        long generalFreqCap = getGeneralFreqCap(context) * DateUtils.MINUTE_IN_MILLIS;

        if (lastTimePeriod > generalFreqCap) {
            return true;
        }

        return false;
    }

    public static int getGeneralFreqCap(Context context) {
        return getSdkPrefs(context).getInt(AD_CONST.GENERAL_FREQ_CAP, 0);
    }

    public static void setGeneralFreqCap(Context context, int generalFreqCap) {
        getSdkPrefs(context).edit().putInt(AD_CONST.GENERAL_FREQ_CAP, generalFreqCap).commit();
    }

    public static void storeTrackingInfo(Context context, String trackingLines) {
        getSdkPrefs(context).edit().putString(AD_CONST.TRACKING_INFO, trackingLines).commit();
    }

    public static String getTrackingInfo(Context context) {
        return getSdkPrefs(context).getString(AD_CONST.TRACKING_INFO, "");
    }

    public static long getLastTimeOfTrackingConversions(Context context) {
        return getSdkPrefs(context).getLong(AD_CONST.LAST_TRACKING_TIME, 0);
    }

    public static void setLastTimeOfTrackingConversions(Context context) {
        getSdkPrefs(context).edit().putLong(AD_CONST.LAST_TRACKING_TIME, System.currentTimeMillis()).commit();
    }

    public static long getLastTimeOfDeviceIDSync(Context context){
        return getSdkPrefs(context).getLong(AD_CONST.LAST_TIME_DEVICE_ID_SYNC, 0);
    }

    public static void setLastTimeOfDeviceIDSync(Context context){
        getSdkPrefs(context).edit().putLong(AD_CONST.LAST_TIME_DEVICE_ID_SYNC, System.currentTimeMillis());
    }
}
