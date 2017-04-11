//package com.tapsouq.sdk.ads;
//
//import com.tapsouq.sdk.util.AD_CONST;
//import com.tapsouq.sdk.util.DATA;
//
//import static com.tapsouq.sdk.util.DATA.tier;
//
///**
// * Created by dell on 10/25/2016.
// */
//public class UrlGenerator2 {
//
//    private static final String getBaseUrl(String a, String b, String c, String d, String e, String f, String g) {
//        return a + b + c + d + e + f + g;
//    }
//
//
//    private static final String PLATFORM = AD_CONST.PLATFORM_ANDROID; //android
//
//    public static String createDevice2(String advertisingId, String deviceInfo) {
//
//
//
//        return getBaseUrl(DATA.http, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.com, "", "")
//                + DATA.slash1 +
//                DATA.CREATE_DEVICE + DATA.slash2 +
//                PLATFORM + DATA.slash3 +
//                advertisingId + DATA.slash4 +
//                deviceInfo;
//    }
//
//
//    public static String updateDevice2(String deviceId, String deviceInfo) {
//
//        return getBaseUrl(DATA.http, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.com, "", "")
//                + DATA.slash1 +
//                DATA.UPDATE_DEVICE + DATA.slash2 +
//                deviceId + DATA.slash3 +
//                deviceInfo;
//    }
//
//
//    public static String formatDeviceInfo2(String manefacturer, String model, String osApiNumber, String osVersion,
//                                           String language, String country, String carrier, String sdkVersion) {
//        return
//                manefacturer + DATA.slash1 +
//                        model + DATA.slash2 +
//                        osApiNumber + DATA.slash3 +
//                        osVersion + DATA.slash4 +
//                        language + DATA.slash5 +
//                        country + DATA.slash6 +
//                        carrier + DATA.slash7 +
//                        sdkVersion;
//    }
//
//
//    public static String getActionUrl2(String deviceId, String actionName, String requestId, String adPlacementId,
//                                       String adCreativeId, String packageName, String shownCreativeParams,
//                                       int mAppId, int mAppUserId, int mCampId, int mCampUserId, String mCountryId,
//                                       String countryTier, String sdk_version, boolean testMode, String sId) {
//        return getBaseUrl(DATA.http, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.com, "", "")
//                + DATA.slash1 +
//                DATA.SDK_ACTION + DATA.slash2 +
//                deviceId + DATA.slash3 +
//                actionName + DATA.slash4 +
//                requestId + DATA.slash5 +
//                adPlacementId + DATA.slash6 +
//                adCreativeId + DATA.slash7 +
//                packageName + DATA.qmarq
//                + addParam(DATA.appId, mAppId) + DATA.and
//                + addParam(DATA.appUserId, mAppUserId) + DATA.and
//                + addParam(DATA.campId, mCampId) + DATA.and
//                + addParam(DATA.campUserId, mCampUserId) + DATA.and
//                + addParam(DATA.countryId, mCountryId) + DATA.and
//                + addParam(tier, countryTier) + DATA.and
//                + addParam(DATA.sdkv, sdk_version) + DATA.and
//                + addParam(DATA.test, testMode) + DATA.and
//                + addParam(DATA.sdk_id, sId) + DATA.and
//                + addParam(DATA.ads, shownCreativeParams);
//    }
//
//    private static String addParam(String key, int value) {
//        if(value == 0)
//            return key + DATA.equal;
//        return key + DATA.equal + String.valueOf(value);
//    }
//
//    private static String addParam(String key, boolean value) {
//        return key + DATA.equal + String.valueOf(value);
//    }
//
//    private static String addParam(String key, String value) {
//        return key + DATA.equal + value;
//    }
//
//}
