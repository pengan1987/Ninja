package io.github.mthli.Ninja.Service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by andyzhou on 16-07-25.
 */
public class XWalkDetector {

    public static boolean detect(Context context) {
        String cpuAbi = getPrimaryCpuAbi();


        if (getPackageInfo(context, "org.xwalk.core"))
            return true;
        boolean hasXWalk = false;
        if (cpuAbi.equalsIgnoreCase("arm64-v8a")) {
            hasXWalk = getPackageInfo(context, "org.xwalk.core64");
        } else if (cpuAbi.equalsIgnoreCase("x86")) {
            hasXWalk = getPackageInfo(context, "org.xwalk.core.ia");
        } else if (cpuAbi.equalsIgnoreCase("x86_64") && !getPackageInfo(context, "org.xwalk.core64") && !getPackageInfo(context, "org.xwalk.core.ia")) {
            hasXWalk = getPackageInfo(context, "org.xwalk.core64.ia");
        }
        return hasXWalk;
    }

    private static boolean getPackageInfo(Context context, String packageName) {
        try {
            PackageInfo e = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException ex) {

            return false;
        }
        return true;
    }

    public static String getPrimaryCpuAbi() {
        try {
            return Build.SUPPORTED_ABIS[0];
        } catch (NoSuchFieldError var6) {
            try {
                Process process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
                InputStreamReader ir = new InputStreamReader(process.getInputStream());
                BufferedReader input = new BufferedReader(ir);
                String abi = input.readLine();
                input.close();
                ir.close();
                return abi;
            } catch (IOException var5) {
                return Build.CPU_ABI;
            }
        }
    }

}
