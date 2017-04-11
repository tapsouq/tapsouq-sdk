package sdk.tapsouq.com;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.List;

import com.tapsouq.sdk.ads.Device;
import com.tapsouq.sdk.ads.GoogleAdvertisingID;
import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.PreferencesUtils;
import com.tapsouq.sdk.util.TrackingInfo;

import static com.tapsouq.sdk.util.TrackingInfo.formatTrackingLines;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test1() throws Exception {
        assertEquals(1 + 1, 2);
    }

    public void test_toString() throws Exception {
        String packageName = "com.app";
        String url = "http://tapsouq.com";
        long startTime = System.currentTimeMillis();
        TrackingInfo trackingInfo = new TrackingInfo(packageName, url, startTime);

        assertEquals("com.app@@http://tapsouq.com@@" + String.valueOf(startTime), trackingInfo.toString());

    }

    public void test_toString2() throws Exception {
        String packageName = "com.app";
        String url = "http://tapsouq.com";
        long startTime = System.currentTimeMillis();
        TrackingInfo trackingInfo = new TrackingInfo(packageName, url, startTime);

        assertFalse(("@@com.app@@http://tapsouq.com@@" + String.valueOf(startTime)).equals(trackingInfo.toString()));

    }

    public void test_parse() throws Exception {

        String line = "@@com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        TrackingInfo trackingInfo = TrackingInfo.parse(line);

        assertNull(trackingInfo);

    }

    public void test_parse3() throws Exception {

        String line = "";
        TrackingInfo trackingInfo = TrackingInfo.parse(line);

        assertNull(trackingInfo);

    }

    public void test_parse4() throws Exception {

        String line = null;
        TrackingInfo trackingInfo = TrackingInfo.parse(line);

        assertNull(trackingInfo);

    }

    public void test_parse2() throws Exception {

        String line = "com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        TrackingInfo trackingInfo = TrackingInfo.parse(line);

        assertNotNull(trackingInfo);

    }

    public void test_equal_samePackage() throws Exception {
        String packageName = "com.app";
        String url = "http://tapsouq.com";
        long startTime = System.currentTimeMillis();
        TrackingInfo trackingInfo1 = new TrackingInfo(packageName, url, startTime);

        String packageName2 = "com.app";
        String url2 = "http://tapsouq.com";
        long startTime2 = System.currentTimeMillis() + 1000;
        TrackingInfo trackingInfo2 = new TrackingInfo(packageName2, url2, startTime2);

        assertTrue(trackingInfo1.equals(trackingInfo2));

    }

    public void test_equal_diffPackage() throws Exception {
        String packageName = "com.app";
        String url = "http://tapsouq.com";
        long startTime = System.currentTimeMillis();
        TrackingInfo trackingInfo1 = new TrackingInfo(packageName, url, startTime);

        String packageName2 = "com.app1";
        String url2 = "http://tapsouq.com";
        long startTime2 = System.currentTimeMillis() + 1000;
        TrackingInfo trackingInfo2 = new TrackingInfo(packageName2, url2, startTime2);

        assertFalse(trackingInfo1.equals(trackingInfo2));

    }

    public static List<TrackingInfo> getTrackInfoList(String lines) {
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

    public void test_getTrackInfoList_null_case() throws Exception {
        String lines = null;
        List<TrackingInfo> list = getTrackInfoList(lines);
        assertEquals(list.size(), 0);
    }

    public void test_getTrackInfoList_empty_case() throws Exception {
        String lines = "";
        List<TrackingInfo> list = getTrackInfoList(lines);
        assertEquals(list.size(), 0);
    }

    public void test_getTrackInfoList_one_line_case() throws Exception {
        String lines = "com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        List<TrackingInfo> list = getTrackInfoList(lines);
        assertEquals(list.size(), 1);
    }

    public void test_getTrackInfoList_two_line_case() throws Exception {
        String lines = "com.app@@http://tapsouq.com/0@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app2@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis());
        List<TrackingInfo> list = getTrackInfoList(lines);
        assertEquals(list.size(), 2);
    }

    public void test_getTrackInfoList_11_line_case() throws Exception {
        String lines = "com.app@@http://tapsouq.com/0@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app2@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app3@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app4@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app5@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app6@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app7@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app8@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app9@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app10@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis())
                + "##" + "com.app11@@http://tapsouq.com/1@@" + String.valueOf(System.currentTimeMillis());
        List<TrackingInfo> list = getTrackInfoList(lines);
        assertEquals(list.size(), 11);
    }


    public void test_format_trackingLines_empty() throws Exception {
        List<TrackingInfo> list = new ArrayList<>();

        String result = formatTrackingLines(list);

        assertEquals(result, "");
    }

    public void test_format_trackingLines_null() throws Exception {
        List<TrackingInfo> list = null;

        String result = formatTrackingLines(list);

        assertEquals(result, "");
    }

    public void test_format_trackingLines_one_obj() throws Exception {
        List<TrackingInfo> list = new ArrayList<>();

        long time = System.currentTimeMillis();
        list.add(new TrackingInfo("com.app1", "http://tapsouq.com/1", time));

        String result = formatTrackingLines(list);

        assertEquals(result, "com.app1@@http://tapsouq.com/1@@" + String.valueOf(time));
    }

    public void test_format_trackingLines_2_obj() throws Exception {
        List<TrackingInfo> list = new ArrayList<>();

        long time = System.currentTimeMillis();
        list.add(new TrackingInfo("com.app1", "http://tapsouq.com/1", time));
        list.add(new TrackingInfo("com.app2", "http://tapsouq.com/2", time));

        String result = formatTrackingLines(list);


        String expected = "com.app1@@http://tapsouq.com/1@@" + String.valueOf(time)
                + "##com.app2@@http://tapsouq.com/2@@" + String.valueOf(time);
        System.out.println(result);
        System.out.println(expected);

        assertEquals(result, expected);
    }

    public void test_format_trackingLines_10_obj() throws Exception {
        List<TrackingInfo> list = new ArrayList<>();

        long time = System.currentTimeMillis();
        list.add(new TrackingInfo("com.app1", "http://tapsouq.com/1", time));
        list.add(new TrackingInfo("com.app2", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app3", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app4", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app5", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app6", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app7", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app8", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app9", "http://tapsouq.com/2", time));
        list.add(new TrackingInfo("com.app10", "http://tapsouq.com/2", time));

        String result = formatTrackingLines(list);


        String expected = "com.app1@@http://tapsouq.com/1@@" + String.valueOf(time)
                + "##com.app2@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app3@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app4@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app5@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app6@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app7@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app8@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app9@@http://tapsouq.com/2@@" + String.valueOf(time)
                + "##com.app10@@http://tapsouq.com/2@@" + String.valueOf(time);

        assertEquals(result, expected);
    }

    public List<TrackingInfo> storeTrackingInfo(TrackingInfo trackingInfo, String lines) {

        List<TrackingInfo> list = getTrackInfoList(lines);

        for (int i = 0; i < list.size(); i++) {
            TrackingInfo oldTrackingInfo = list.get(i);
            if (oldTrackingInfo.equals(trackingInfo)) {
                list.remove(oldTrackingInfo);
            }
        }

        list.add(trackingInfo);

        return list;
    }

    public void test_storeTrackingInfo_null_line() throws Exception {
        TrackingInfo obj = new TrackingInfo("com.app1", "http://tapsouq.com/1", System.currentTimeMillis());
        List<TrackingInfo> list = storeTrackingInfo(obj, null);

        assertEquals(list.size(), 1);
    }

    public void test_storeTrackingInfo_empty_line() throws Exception {
        TrackingInfo obj = new TrackingInfo("com.app1", "http://tapsouq.com/1", System.currentTimeMillis());
        List<TrackingInfo> list = storeTrackingInfo(obj, "");

        assertEquals(list.size(), 1);
    }

    public void test_storeTrackingInfo_one_line() throws Exception {
        String lines = "com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        TrackingInfo obj = new TrackingInfo("com.app1", "http://tapsouq.com/1", System.currentTimeMillis());
        List<TrackingInfo> list = storeTrackingInfo(obj, lines);

        assertEquals(list.size(), 2);
    }

    public void test_storeTrackingInfo_duplicate_package() throws Exception {
        String lines = "com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        TrackingInfo obj = new TrackingInfo("com.app", "http://tapsouq.com/1", System.currentTimeMillis());
        List<TrackingInfo> list = storeTrackingInfo(obj, lines);

        assertEquals(list.size(), 1);
    }

    public void test_storeTrackingInfo_duplicate_many_package() throws Exception {
        String lines = "com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app2@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app3@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app4@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app5@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app6@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app7@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app8@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app9@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app10@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app11@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        TrackingInfo obj = new TrackingInfo("com.app", "http://tapsouq.com/1", System.currentTimeMillis());
        List<TrackingInfo> list = storeTrackingInfo(obj, lines);

        assertEquals(list.size(), 11);
    }

    public void test_storeTrackingInfo_many_package() throws Exception {
        String lines = "com.app@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app2@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app3@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app4@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app5@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app6@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app7@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app8@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app9@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app10@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis()) +
                "##com.app11@@http://tapsouq.com@@" + String.valueOf(System.currentTimeMillis());
        TrackingInfo obj = new TrackingInfo("com.app12", "http://tapsouq.com/1", System.currentTimeMillis());
        List<TrackingInfo> list = storeTrackingInfo(obj, lines);

        assertEquals(list.size(), 12);
    }

    public void testPreferencesUtil() throws Exception {


        //PreferencesUtils.setGeneralFreqCap(this.getContext(), 5);
        int cap = PreferencesUtils.getGeneralFreqCap(this.getContext());

        assertEquals(5, cap);
    }

    public void test_storeTrackingInfo() throws Exception{
        String packageName = "com.rashadandhamid.designs1";
        String url = "http://tapsouq.com/sdk-action/24812/1/0/5/0/answers.com.testactivity?ads=12";
        long time = 1485242620826L;
        TrackingInfo trackingInfo = new TrackingInfo(packageName, url, time);
        TrackingInfo.storeTrackingInfo(this.getContext(), trackingInfo);


    }

    public void test_trackConversion() throws Exception {
        String[] packageName = {"com.rashadandhamid.designs1",
                "com.smartapps.videodownloaderforfacebook",
                "com.smartapps.videodownloaderforfacebookpro.free.android.fb",
                "com.rashadandhamid.designs1",
                "com.rashadandhamid.designs"};

        String[] url = {"http://tapsouq.com/sdk-action/24812/1/0/5/0/answers.com.testactivity?ads=12",
                "http://tapsouq.com/sdk-action/24812/1/0/5/0/answers.com.testactivity?ads=12",
                "http://tapsouq.com/sdk-action/24812/1/0/5/0/answers.com.testactivity?ads=12",
                "http://tapsouq.com/sdk-action/24812/1/0/5/0/answers.com.testactivity?ads=12",
                "http://tapsouq.com/sdk-action/24812/1/0/5/0/answers.com.testactivity?ads=12"};

        long now = 1485249999328L + 19 * DateUtils.MINUTE_IN_MILLIS;
        long[] time = new long[] {now,
                now + 1 * DateUtils.MINUTE_IN_MILLIS,
                now + 2 * DateUtils.MINUTE_IN_MILLIS,
                now + 3 * DateUtils.MINUTE_IN_MILLIS,
                now + 4 * DateUtils.MINUTE_IN_MILLIS};

        for(int i=0; i<packageName.length; i++) {
            TrackingInfo trackingInfo = new TrackingInfo(packageName[i], url[i], time[i]);
            TrackingInfo.storeTrackingInfo(this.getContext(), trackingInfo);
        }

//        String[] result = TrackingInfo.trackConversions(getContext(), null);
//
//        String expect = packageName + "@@" + url + "@@a" + time;
//
//        assertEquals(result[0], result[1], expect);

    }

    public void test_getDeviceIdFromPrefs() throws Exception{

        String deviceId = PreferencesUtils.getDeviceId(this.getContext());

        assertNotNull(deviceId);
    }

    public void test_readFile() throws Exception{
        String info = Device.readDeviceIdFromFile();

        assertNotNull(info);
    }

    public void test_readFile2() throws Exception{
        String info = Device.readDeviceIdFromFile();

        assertEquals(info, "DID:123456");
    }

    public void test_create_device_Id_in_prefs() throws Exception{
        GoogleAdvertisingID.getGoogleAdvertisingID(this.getContext(), null);
    }

    public void test_save_deviceid_infile() throws  Exception{
        boolean b = Device.storeDeviceIdInFile("123456");

        assertTrue(b);
    }
}