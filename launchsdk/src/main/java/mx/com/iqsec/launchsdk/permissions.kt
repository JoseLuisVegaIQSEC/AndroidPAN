package mx.com.iqsec.test_sdk

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.ArrayList


fun allRuntimePermissionsGranted(activity: Activity, LIST_PERMISSIONS: Array<String>): Boolean {
    for (permission in LIST_PERMISSIONS) {
        permission.let {
            if (!isPermissionGranted(activity, it)) {
                return false
            }
        }
    }
    return true
}

private fun isPermissionGranted(context: Context, permission: String): Boolean {
    if (ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

fun getRuntimePermissions(
    activity: Activity,
    LIST_PERMISSIONS: Array<String>,
    PERMISSION_REQUESTS: Int
) {
    val permissionsToRequest = ArrayList<String>()
    for (permission in LIST_PERMISSIONS) {
        permission.let {
            if (!isPermissionGranted(activity, it)) {
                permissionsToRequest.add(permission)
            }
        }
    }

    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            activity, permissionsToRequest.toTypedArray(), PERMISSION_REQUESTS
        )
    }
}