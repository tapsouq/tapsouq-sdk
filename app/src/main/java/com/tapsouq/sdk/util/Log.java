package com.tapsouq.sdk.util;

/**
 * Created by dell on 2/9/2017.
 */

/**
 * TODO: PUBLISH CHECKLIST
 * 1. change sdk_version
 * 2. change log level
 * 3. comment out //e.printStackTrace()
 */

public class Log {

    private static int ERROR = 5;
    private static int WARN = 4;
    private static int INFO = 3;
    private static int DEBUG = 2;
    private static int VERBOSE = 1;

    private static int LEVEL = DEBUG; // DEBUG USED IN USE IN DEVELOPMENT MODE
//    private static int LEVEL = INFO; // INFO USED IN PRODUCTION MODE

    public static void e(String tag, String message){
        if(LEVEL <= ERROR)
            android.util.Log.e(tag, message);
    }

    public static void w(String tag, String message){
        if(LEVEL <= WARN)
            android.util.Log.w(tag, message);
    }

    public static void i(String tag, String message){
        if(LEVEL <= INFO)
            android.util.Log.i(tag, message);
    }

    public static void d(String tag, String message){
        if(LEVEL <= DEBUG)
            android.util.Log.d(tag, message);
    }

    public static void v(String tag, String message){
        if(LEVEL == VERBOSE)
            android.util.Log.v(tag, message);
    }
}
