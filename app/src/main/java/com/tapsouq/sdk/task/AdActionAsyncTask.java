package com.tapsouq.sdk.task;

import android.os.AsyncTask;

import com.tapsouq.sdk.util.AD_CONST;
import com.tapsouq.sdk.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class handles the actions: ad shown, ad clicked, app installed
 */
public class AdActionAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                URL u = new URL(params[0].replaceAll(" ", "%20"));//replace white space with its html code

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

                boolean success = jsonObject.getBoolean("status");

                return success;
            } catch (Exception e) {
                Log.d(AD_CONST.LOG_TAG, e.getMessage());
                Log.e(AD_CONST.LOG_TAG, "error while connecting to server.");
                //e.printStackTrace();
            } finally {

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Log.d(AD_CONST.LOG_TAG, "send action success.");
            } else {
                Log.d(AD_CONST.LOG_TAG, "send action failed.");
            }
        }
    }