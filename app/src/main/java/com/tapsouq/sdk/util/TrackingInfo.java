package com.tapsouq.sdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.format.DateUtils;

import com.tapsouq.sdk.ads.TapSouqAd;

import java.util.ArrayList;
import java.util.List;

public class TrackingInfo {

    private String packageName;
    private String trackingUrl;
    private long trackingStartTime;

    public TrackingInfo(String packageName, String finalUrl, long trackingStartTime) {
        this.packageName = packageName;
        this.trackingUrl = finalUrl;
        this.trackingStartTime = trackingStartTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public long getTrackingStartTime() {
        return trackingStartTime;
    }

    public void setTrackingStartTime(long trackingStartTime) {
        this.trackingStartTime = trackingStartTime;
    }

    @Override
    public String toString() {
        return packageName + AD_CONST.FIELD_SEPARATOR
                + trackingUrl + AD_CONST.FIELD_SEPARATOR
                + String.valueOf(trackingStartTime);
    }

    public static TrackingInfo parse(String line) {
        if (line == null)
            return null;

        String[] array = line.split(AD_CONST.FIELD_SEPARATOR);

        if (array.length != 3)
            return null;

        return new TrackingInfo(array[0], array[1], Long.parseLong(array[2]));
    }

    @Override
    public boolean equals(Object o) {
        return packageName.equals(((TrackingInfo) o).getPackageName());
    }

    public static List<TrackingInfo> getTrackInfoList(Context context) {
        String lines = PreferencesUtils.getTrackingInfo(context);
        List<TrackingInfo> list = new ArrayList<>();

        if (lines == null || "".equals(lines))
            return list;

        String[] linesArray = lines.split(AD_CONST.LINE_SEPARATOR);
        for (int i = 0; i < linesArray.length; i++) {

            TrackingInfo trackingInfo = TrackingInfo.parse(linesArray[i]);
            if (trackingInfo != null)
                list.add(trackingInfo);
        }

        return list;
    }

    public static String formatTrackingLines(List<TrackingInfo> list) {
        if (list == null || list.size() == 0)
            return "";

        TrackingInfo firstTrackingLine = list.get(0);
        String trackingLines = firstTrackingLine.toString();

        for (int i = 1; i < list.size(); i++) {
            TrackingInfo trackingInfo = list.get(i);
            trackingLines = trackingLines + AD_CONST.LINE_SEPARATOR + trackingInfo.toString();
        }
        return trackingLines;
    }

    public static void storeTrackingInfo(Context context, TrackingInfo trackingInfo) {

        List<TrackingInfo> list = TrackingInfo.getTrackInfoList(context);

        for (int i = 0; i < list.size(); i++) {
            TrackingInfo oldTrackingInfo = list.get(i);
            if (oldTrackingInfo.equals(trackingInfo)) {
                list.remove(i--);
                Log.d(AD_CONST.LOG_TAG, trackingInfo.getPackageName() + " is removed because newer comes.\n" +
                        ", list size: " + list.size() + ", i=" + i + ", " + formatTrackingLines(list));
            }
        }

        list.add(trackingInfo);

        Log.d(AD_CONST.LOG_TAG, "list size: " + list.size() + ", " + formatTrackingLines(list));

        PreferencesUtils.storeTrackingInfo(context, formatTrackingLines(list));
    }


    public static void trackConversions(Context context, TapSouqAd tapSouqAd) {

        if ((System.currentTimeMillis() - PreferencesUtils.getLastTimeOfTrackingConversions(context)) < DateUtils.DAY_IN_MILLIS) {
            Log.d(AD_CONST.LOG_TAG, "Less than a day");
            return;
        }

        PreferencesUtils.setLastTimeOfTrackingConversions(context);

        boolean isTrackingDataChanged = false;

        List<TrackingInfo> trackingInfoList = getTrackInfoList(context);
        Log.d(AD_CONST.LOG_TAG, "start tracking: " + formatTrackingLines(trackingInfoList));

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> installedAppsList = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //assumed: com.app.package@@http://sfasf/sd/asd@@1453453454
        for (int i = 0; i < trackingInfoList.size(); i++) {

            TrackingInfo trackingInfo = trackingInfoList.get(i);

            //remove any tracking info passed 7 days
            if ((System.currentTimeMillis() - trackingInfo.getTrackingStartTime()) > 7 * DateUtils.DAY_IN_MILLIS) {
                trackingInfoList.remove(i--);
                isTrackingDataChanged = true;
                Log.d(AD_CONST.LOG_TAG, trackingInfo.getPackageName() + " is removed because > 7 days.\n" +
                        ", list size: " + trackingInfoList.size() + ", i=" + i + ", " + formatTrackingLines(trackingInfoList));

                continue;
            }

            for (int j = 0; j < installedAppsList.size(); j++) {

                String installedPackageName = installedAppsList.get(j).packageName;

                //inform server that installed is exists and remove it from tracking info
                if (installedPackageName.equals(trackingInfo.getPackageName())) {
                    isTrackingDataChanged = true;
                    //todo: store in preferneces tracked flag, to resend in case internet failure
                    tapSouqAd.appInstalled(trackingInfo.getTrackingUrl());
                    Log.d(AD_CONST.LOG_TAG, "SENDING TRACKED... " + trackingInfo.getTrackingUrl());
                    trackingInfoList.remove(i--);

                    Log.d(AD_CONST.LOG_TAG, trackingInfo.getPackageName() + " is removed because it installed.\n" +
                            ", list size: " + trackingInfoList.size() + ", i=" + i + ", " + formatTrackingLines(trackingInfoList));

                    break;
                }
            }
        }

        if (isTrackingDataChanged) {
            PreferencesUtils.storeTrackingInfo(context, formatTrackingLines(trackingInfoList));
            Log.d(AD_CONST.LOG_TAG, "Tracking changed.\n" +
                    ", list size: " + trackingInfoList.size() +  ", " + formatTrackingLines(trackingInfoList));

        } else {
            Log.d(AD_CONST.LOG_TAG, "No conversion tracked");
        }

        Log.d(AD_CONST.LOG_TAG, "end of trackConversions..." + formatTrackingLines(trackingInfoList));
        return;
    }

    public static String[] trackConversionsForTesting(Context context, TapSouqAd tapSouqAd) {

        if ((System.currentTimeMillis() - PreferencesUtils.getLastTimeOfTrackingConversions(context)) < DateUtils.DAY_IN_MILLIS)
            return new String[]{AD_CONST.CONV_MSG_LESS_DAY, AD_CONST.CONV_MSG_LESS_DAY};

        String[] message = new String[2];

        message[0] = AD_CONST.CONV_MSG_NO_CONVERSION;

        PreferencesUtils.setLastTimeOfTrackingConversions(context);

        boolean isTrackingDataChanged = false;

        List<TrackingInfo> trackingInfoList = getTrackInfoList(context);

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> installedAppsList = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //assumed: http://sfasf/sd/asd@@com.app.package@@1453453454
        for (int i = 0; i < trackingInfoList.size(); i++) {

            TrackingInfo trackingInfo = trackingInfoList.get(i);

            //remove any tracking info passed 7 days
            if ((System.currentTimeMillis() - trackingInfo.getTrackingStartTime()) > 2 * DateUtils.MINUTE_IN_MILLIS) {
                trackingInfoList.remove(i);
                isTrackingDataChanged = true;
                message[0] = AD_CONST.CONV_MSG_REMOVED_AFTER_7_DAYS;
                i--;
                continue;
            }

            for (int j = 0; j < installedAppsList.size(); j++) {

                String installedPackageName = installedAppsList.get(j).packageName;

                //inform server that installed is exists and remove it from tracking info
                if (installedPackageName.equals(trackingInfo.getPackageName())) {
                    isTrackingDataChanged = true;
                    //todo: store in preferneces tracked flag, to resend in case internet failure
                    //tapSouqAd.appInstalled(trackingInfo.getTrackingUrl());
                    Log.d(AD_CONST.LOG_TAG, "SENDING TRACKED... " + trackingInfo.getTrackingUrl());
                    trackingInfoList.remove(i);
                    message[0] = AD_CONST.CONV_MSG_INSTALLED;
                    i--;
                    break;
                }
            }
        }

        if (isTrackingDataChanged) {
            PreferencesUtils.storeTrackingInfo(context, formatTrackingLines(trackingInfoList));
        } else {
            Log.d(AD_CONST.LOG_TAG, "No conversion tracked");
        }

        Log.d(AD_CONST.LOG_TAG, "end of trackConversions..." + formatTrackingLines(trackingInfoList));
        message[1] = formatTrackingLines(trackingInfoList);
        return message;
    }
}
