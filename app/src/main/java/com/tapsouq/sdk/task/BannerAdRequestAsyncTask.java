package com.tapsouq.sdk.task;

import android.os.AsyncTask;

import com.tapsouq.sdk.ads.AdCreative;
import com.tapsouq.sdk.ads.TapSouqBannerAd;
import com.tapsouq.sdk.ads.TapSouqListener;
import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.Log;
import com.tapsouq.sdk.util.PreferencesUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BannerAdRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

    private TapSouqBannerAd banner;

    public BannerAdRequestAsyncTask(TapSouqBannerAd bannerAd) {
        banner = bannerAd;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean loaded = downloadAd(params[0]);
        return loaded;
    }

    private boolean downloadAd(String url) {


        try {
            URL u = new URL(url.replaceAll(" ", "%20"));

            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");


            conn.connect();
            InputStream is = conn.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }

            Log.d(AD_CONST.LOG_TAG, sb.toString());

            String JSONResp = new String(sb.toString());

            JSONObject jsonObject = new JSONObject(JSONResp);

            boolean status = jsonObject.getBoolean("status");
            if (status) {

                int generalFreqCap = jsonObject.getInt("general_frequency_capping");
                PreferencesUtils.setGeneralFreqCap(banner.getContext(), generalFreqCap);

                if(!banner.isTestMode()) {
                    banner.setRequestId(jsonObject.getString("requestId"));
                }

                JSONObject adObject = jsonObject.getJSONObject("adsObject");

                String creativeId = adObject.getString("id");

                String clickUrl = adObject.getString("click_url");
                AdCreative adCreative = banner.getAdCreative();
                adCreative.setId(creativeId);
                adCreative.setClickUrl(clickUrl);

                if(!banner.isTestMode()) {
                    int refreshInterval = adObject.getInt("refreshInterval");
                    banner.setRefreshInterval(refreshInterval);
                    adCreative.setAppId(adObject.getInt("app_id"));
                    adCreative.setAppUserId(adObject.getInt("app_user"));
                    adCreative.setCampUserId(adObject.getInt("camp_user"));
                }

                adCreative.setCampId(adObject.getInt("camp_id"));
                adCreative.setImagesPath(jsonObject.getString("imagesPath"));
                adCreative.setImageFile(adObject.getString("image_file"));
                int format = adObject.getInt("format");
                int type = adObject.getInt("type");
                adCreative.setType(type);

                if (type == AdCreative.AD_TYPE_TEXT) {
                    //text ad
                    adCreative.setTitle(adObject.getString("title"));
                    adCreative.setDescription(adObject.getString("description"));
                }

            }
            return status;
        } catch (Exception e) {
            Log.d(AD_CONST.LOG_TAG, e.getMessage());
            Log.e(AD_CONST.LOG_TAG, "Error while connecting to server. ad unit " + banner.getAdUnitID());
            //e.printStackTrace();
        } finally {

        }
        return false;

    }

    @Override
    protected void onPostExecute(Boolean adLoaded) {

        banner.setAdLoaded(adLoaded);

        if (adLoaded) {
            //in this case (of banner), show ad immediately because no need to notify user of loaded
            Log.i(AD_CONST.LOG_TAG, "Loaded successfully, ad unit " + banner.getAdUnitID());
            banner.showAd();
        } else {
            //reasons: no inventory, no network, etc
            Log.i(AD_CONST.LOG_TAG, "Failed to load ad unit " + banner.getAdUnitID());
            TapSouqListener listener = banner.getListener();
            if (listener != null) {
                listener.adFailed();
            }
        }
    }
}