package com.tapsouq.sdk.ads;

import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.DATA;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                                       String countryTier, String sdk_version, String testMode) {

        String params = addParam(DATA.b, deviceId) + DATA.and
                + addParam(DATA.i, mCountryId) + DATA.and
                + addParam(DATA.k, sdk_version) + DATA.and
                + addParam(DATA.l, actionName) + DATA.and
                + addParam(DATA.m, requestId) + DATA.and
                + addParam(DATA.n, adCreativeId) + DATA.and
                + addParam(DATA.o, adPlacementId) + DATA.and
                + addParam(DATA.p, mAppId) + DATA.and
                + addParam(DATA.q, mCampId) + DATA.and
                + addParam(DATA.r, mAppUserId) + DATA.and
                + addParam(DATA.s, mCampUserId) + DATA.and
                + addParam(DATA.t, countryTier) + DATA.and
                + addParam(DATA.v, packageName) + DATA.and
                + addParam(DATA.x, testMode);

        String query = params.replaceAll(DATA.and, "");
        String pk = "HB5FTxbt59j7wSnQm5UY34";
        String h = en(pk + query);
        params += DATA.and + addParam(DATA.z, h);

        return getBaseUrl(DATA.https, DATA.dslash, DATA.tapsouq, DATA.dot, DATA.net, "", "")
                + DATA.slash1 +
                DATA.SDK_ACTION + DATA.qmarq
                + params;
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

    private static String en(String query){
        StringBuffer hashString = new StringBuffer();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(query.getBytes("UTF-8"));
            for(int i=0; i<32; i++){
//                hashString += String.format("%02x", hash[i]);
                hashString.append(String.format("%02x", hash[i]));

            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
