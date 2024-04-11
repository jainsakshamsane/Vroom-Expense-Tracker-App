package com.vroomcab.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.biometric.BiometricManager;

public class BiometricUtils {

    public static boolean isBiometricAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();

        // Check if the device supports biometric authentication
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            // Check if the biometric hardware is present and functional
            BiometricManager biometricManager = BiometricManager.from(context);
            return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
        }

        return false;
    }
}
