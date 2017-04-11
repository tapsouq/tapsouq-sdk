package sdk.tapsouq.com;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addition_inValid() throws Exception {
        assertEquals(5, 2+2);
    }

//    @Test
//    public void conversionTrack() throws Exception{
//
//
//        if((System.currentTimeMillis() - getLastTimeOfTrackingConversions()) < DateUtils.MINUTE_IN_MILLIS)
//            return;
//
//        boolean isTrackingDataChanged = false;
//        String trackingInfo = "a.b.c@@http://url";
//        Log.i(AD_CONST.LOG_TAG, "start of trackConversions..." + trackingInfo);
//
//        String[] trackingLinesArray = trackingInfo.split(AD_CONST.LINE_SEPARATOR);
//        List<String> trackingLinesList = new ArrayList<>();
//        for(int i=0; i<trackingLinesArray.length; i++){
//            trackingLinesList.add(trackingLinesArray[i]);
//        }
//
//        PackageManager pm = context.getPackageManager();
//        List<ApplicationInfo> installedAppsList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//        //assumed: http://sfasf/sd/asd@@com.app.package@@1453453454
//        for (int i=0; i<trackingLinesList.size(); i++){
//
//            String[] trackingFields = trackingLinesList.get(i).split(AD_CONST.FIELD_SEPARATOR);
//
//            String clickedPackageName = trackingFields[0];
//            String trackingUrl = trackingFields[1];
//            long storedTimeMillis = Long.parseLong(trackingFields[2]);
//
//            if ((System.currentTimeMillis() - storedTimeMillis) > 7 * DateUtils.DAY_IN_MILLIS) {
//                trackingLinesList.remove(i);
//                isTrackingDataChanged = true;
//                continue;
//            }
//
//            for (int j=0; j<installedAppsList.size(); j++){
//
//                String installedPackageName = installedAppsList.get(j).packageName;
//
//                if(installedPackageName.equals(clickedPackageName)){
//                    isTrackingDataChanged = true;
//                    tapSouqAd.appInstalled(trackingUrl);
//                    Log.i(AD_CONST.LOG_TAG, "SENDING TRACKED... " + trackingUrl);
//                    trackingLinesList.remove(i);
//                    break;
//                }
//            }
//        }
//
//        if (isTrackingDataChanged){
//            String newTrackingLines = "";
//            for (int i=0; i<trackingLinesList.size(); i++){
//                newTrackingLines = newTrackingLines + AD_CONST.LINE_SEPARATOR +  trackingLinesList.get(i);
//            }
//            PreferencesUtils.storeTrackingInfo(context, newTrackingLines);
//        } else {
//            Log.i(AD_CONST.LOG_TAG, "No conversion tracked");
//        }
//
//        Log.i(AD_CONST.LOG_TAG, "end of trackConversions..." + trackingInfo);
//    }

    private long getLastTimeOfTrackingConversions() {
        return 0;
    }

}