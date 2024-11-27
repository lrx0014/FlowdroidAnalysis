package analysis.apk;

import net.dongliu.apk.parser.ApkFile;

public class ApkUtils {
    public String getTargetSdkVersion(String apkPath) throws Exception {
        try (ApkFile apkFile = new ApkFile(apkPath)) {
            return apkFile.getApkMeta().getTargetSdkVersion();
        }
    }

    public int getMinSdkVersion(String apkPath) throws Exception {
        try (ApkFile apkFile = new ApkFile(apkPath)) {
            String minSdk = apkFile.getApkMeta().getMinSdkVersion();
            return minSdk != null ? Integer.parseInt(minSdk) : -1; // 返回 -1 表示未知
        }
    }
}
