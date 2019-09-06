package com.llj.ormsqlite.log;

import android.util.Log;

/**
 * @author: lilinjie
 * @date: 2019-09-06 10:16
 * @description:
 */
public class OrmSqliteLog {

    private static final String TAG = "OrmSqliteLog";
    private static boolean isDebug = false;

    public static void info(String message) {
        if (isDebug) {
            Log.i(TAG, message);
        }
    }

    public static void error(String message) {
        if (isDebug) {
            Log.e(TAG, message);
        }
    }

    public static void enableLog() {
        isDebug = true;
    }

    public static void disableLog() {
        isDebug = false;
    }
}
