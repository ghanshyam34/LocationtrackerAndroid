package com.gs.locationtrackerdemo

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import java.util.ArrayList

/**
 * Created by Ghanshyam.
 */
abstract class BaseActivityKotlin : AppCompatActivity() {

    internal var permission: Array<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permission = null
    }

    fun checkRequiredPermission(permission: Array<String>) {
        this.permission = permission
        checkPermissions(*permission)
    }

    /**
     * called when required permission is granted to notify in child class need to override this
     */
    protected open fun invokedWhenPermissionGranted() {

    }

    /**
     * called when required permission is not or allready granted to notify in child class need to override this
     */
    protected open fun invokedWhenNoOrAllreadyPermissionGranted() {

    }


    private fun checkPermissions(vararg permission: String) {

        if (Build.VERSION.SDK_INT >= 23 && permission != null) {

            var result: Int
            val listPermissionsNeeded = ArrayList<String>()
            for (p in permission) {
                result = ContextCompat.checkSelfPermission(this, p)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p)
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {

                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 111)

            } else {

                invokedWhenNoOrAllreadyPermissionGranted()

            }

        } else {

            invokedWhenNoOrAllreadyPermissionGranted()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted()
        } else if (requestCode == 111) {
            invokedWhenDeniedWithResult(grantResults)
        }
    }

    /**
     * called when all required permission is checked and granted
     */
    private fun allPermissionsGranted() {
        invokedWhenPermissionGranted()
    }


    protected open fun invokedWhenDeniedWithResult(grantResults: IntArray) {

    }


    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }
}
