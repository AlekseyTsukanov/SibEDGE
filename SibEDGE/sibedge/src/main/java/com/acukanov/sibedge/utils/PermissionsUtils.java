package com.acukanov.sibedge.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionsUtils {
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissionsSafely(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(Activity activity, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
