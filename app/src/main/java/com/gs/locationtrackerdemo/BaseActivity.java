package com.gs.locationtrackerdemo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ghanshyam on 11/12/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    String[] permission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission = null;
    }

    public void checkRequiredPermission(String[] permission){
        this.permission = permission;
        checkPermissions(permission);
    }

    /**
     * called when required permission is granted to notify in child class need to override this
     */
    protected void invokedWhenPermissionGranted(){

    }

    /**
     * called when required permission is not or allready granted to notify in child class need to override this
     */
    protected void invokedWhenNoOrAllreadyPermissionGranted(){

    }


    private void checkPermissions(String... permission) {

        if (Build.VERSION.SDK_INT >= 23 && permission != null) {

            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permission) {
                result = ContextCompat.checkSelfPermission(this, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {

                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),111);

            }else{

                invokedWhenNoOrAllreadyPermissionGranted();

            }

        }else{

            invokedWhenNoOrAllreadyPermissionGranted();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111 && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted();
        } else if(requestCode == 111){
            invokedWhenDeniedWithResult(grantResults);
        }
    }

    /**
     * called when all required permission is checked and granted
     */
    private void allPermissionsGranted() {
        invokedWhenPermissionGranted();
    }


    protected void invokedWhenDeniedWithResult(int[] grantResults) {

    }


    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}