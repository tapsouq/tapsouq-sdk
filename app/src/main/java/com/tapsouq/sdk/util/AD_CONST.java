package com.tapsouq.sdk.util;

/**
 * Created by dell on 8/6/2016.
 */
public class AD_CONST {

    public static final String LOG_TAG = "TAP_SOUQ";
    public static final String CONV_MSG_LESS_DAY = "Less than a day";
    public static final String CONV_MSG_REMOVED_AFTER_7_DAYS = "App removed from tracking info after 7 days";
    public static final String CONV_MSG_INSTALLED = "App removed from tracking info after it installed";
    public static final String CONV_MSG_NO_CONVERSION = "No conversion found yet";
    public static final String LAST_TIME_DEVICE_ID_SYNC = "LAST_TIME_DEVICE_ID_SYNC";
    public static String TAP_SDK_VERSION = "1.01";

    public static final int ACTION_AD_REQUEST = 1;
    public static final int ACTION_AD_SHOWN = 2;
    public static final int ACTION_AD_CLICKED = 3;
    public static final int ACTION_TRACK_INSTALL = 4;

    public static final String PLATFORM_ANDROID = "1";
    public static final String PLATFORM_IOS = "2";

    public static final String AD_CACHE_FOLDER = "tapsouq";
    public static final String APP_EXTERNAL_FOLDER = "tapsouq";
    public static final String ID_FILE = "tap-souq-id.txt";

    public static final String SERVER_URL_OF_IMAGES = "http://www.tapsouq.com/public/uploads/ad-images/";

    //public static final String SHARED_PREFERENCES_NAME = "tapsouq_preferences"; //future
    public static final String SHARED_PREFERENCES_NAME = "tapsouq";
    public static final String GOOGLE_ADVERTISING_ID_KEY = "GOOGLE_ADVERTISING_ID_KEY";
    public static final String SHOWN_CREATIVE_IDS_KEY = "SHOWN_CREATIVE_IDS_KEY";
    public static final String LAST_24_MILLIS = "LAST_24_MILLIS";//MILLIS USED TO COUNT 24 HOURS
    public static final String DEVICE_INFO = "DEVICE_INFO";
    public static final String DEVICE_INFO_UPDATE_TIME = "DEVICE_INFO_UPDATE_TIME";
    public static final String SDK_VERSION_KEY = "SDK_VERSION_KEY";
    public static final String LAST_REQUEST_MILLIS = "LAST_REQUEST_MILLIS";
    public static final String TRACKING_INFO = "TRACKING_INFO";
    public static final String FIELD_SEPARATOR = "@@";
    public static final String LINE_SEPARATOR = "##";
    public static final String LAST_TRACKING_TIME = "LAST_TRACKING_TIME";
    public static final String GENERAL_FREQ_CAP = "GENERAL_FREQ_CAP";

    public static final String DEVICE_ID_STORED = "DEVICE_ID_STORED";
    public static final String DEVICE_ID_FILE_ERROR = "DEVICE_ID_FILE_ERROR";

}
