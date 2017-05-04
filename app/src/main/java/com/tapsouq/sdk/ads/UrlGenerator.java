package com.tapsouq.sdk.ads;

import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.DATA;

/**
 * Created by dell on 10/25/2016.
 */
public class UrlGenerator {

    private static final String getBaseUrl(String a, String b, String c, String d, String e, String f, String g) {
        return a + b + c + d + e + f + g;
    }


    private static final String PLATFORM = AD_CONST.PLATFORM_ANDROID; //android

    public static String createDevice2(String advertisingId, String deviceInfo) {

        return
                getBaseUrl(DATA.https, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.net, "", "")
                + DATA.slash1 +
                DATA.CREATE_DEVICE + DATA.qmarq +
                addParam(DATA.a, advertisingId) + DATA.and +
                addParam(DATA.c, PLATFORM) + DATA.and +
                deviceInfo
                ;

    }


    public static String updateDevice2(String deviceId, String deviceInfo) {

        return getBaseUrl(DATA.https, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.net, "", "")
                + DATA.slash1 +
                DATA.UPDATE_DEVICE + DATA.qmarq +
                addParam(DATA.b, deviceId) + DATA.and +
                addParam(DATA.c, PLATFORM) + DATA.and +
                deviceInfo;
    }


    public static String formatDeviceInfo2(String manefacturer, String model, String osApiNumber, String osVersion,
                                           String language, String country, String carrier, String sdkVersion) {

        return
                addParam(DATA.d, manefacturer) + DATA.and +
                addParam(DATA.e, model) + DATA.and +
                addParam(DATA.f, osApiNumber) + DATA.and +
                addParam(DATA.g, osVersion) + DATA.and +
                addParam(DATA.h, language) + DATA.and +
                addParam(DATA.i, country) + DATA.and +
                addParam(DATA.j, carrier) + DATA.and +
                addParam(DATA.k, sdkVersion)
                ;
    }


    public static String getActionUrl2(String deviceId, int actionName, String requestId, String adPlacementId,
                                       String adCreativeId, String packageName, String shownCreativeParams,
                                       int mAppId, int mAppUserId, int mCampId, int mCampUserId, String mCountryId,
                                       String countryTier, String sdk_version, String testMode, String sLetters,  String scode) {
        return getBaseUrl(DATA.https, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.net, "", "")
                + DATA.slash1 +
                DATA.SDK_ACTION + DATA.qmarq
                + addParam(DATA.b, deviceId) + DATA.and
                + addParam(DATA.l, actionName) + DATA.and
                + addParam(DATA.m, requestId) + DATA.and
                + addParam(DATA.u, sLetters) + DATA.and
                + addParam(DATA.n, adCreativeId) + DATA.and
                + addParam(DATA.o, adPlacementId) + DATA.and
                + addParam(DATA.p, mAppId) + DATA.and
                + addParam(DATA.q, mCampId) + DATA.and
                + addParam(DATA.r, mAppUserId) + DATA.and
                + addParam(DATA.s, mCampUserId) + DATA.and
                + addParam(DATA.i, mCountryId) + DATA.and
                + addParam(DATA.t, countryTier) + DATA.and
                + addParam(DATA.v, packageName) + DATA.and
                + addParam(DATA.k, sdk_version) + DATA.and
                + addParam(DATA.x, testMode) + DATA.and
                + addParam(DATA.y, scode) + DATA.and
                + addParam(DATA.w, shownCreativeParams);
    }

    private static String addParam(String key, int value) {
        if(value == 0)
            return key + DATA.equal;
        return key + DATA.equal + String.valueOf(value);
    }

    private static String addParam(String key, boolean value) {
        return key + DATA.equal + String.valueOf(value);
    }

    private static String addParam(String key, String value) {
        return key + DATA.equal + value;
    }

}
