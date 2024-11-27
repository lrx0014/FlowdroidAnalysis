package analysis.apk;

import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.options.Options;

import java.io.File;
import java.util.Objects;

public class Main {
    // Specify root path for APK files
    private static final String apkRootPath = "/Users/lirenxiang/WorkSpace/cybersecurity/static_project/OneDrive_1_11-27-2024/APKs/";
    // APKs to be analyzed
    private static final String[] apks = {
            "com.delhi.metro.dtc.apk",
            "com.hawaiianairlines.app.apk",
            "com.imo.android.imoim.apk",
            "com.tado.apk",
            "com.walkme.azores.new.apk",
            "com.wooxhome.smart.apk",
            "com.yourdelivery.pyszne.apk",
            "linko.home.apk",
            "mynt.app.apk",
            "nz.co.stuff.android.news.apk"
    };
    // Specify root path for SDK
    private static final String sdkRootPath = "/Users/lirenxiang/WorkSpace/github/Android-platforms/jars/stubs/";

    public static void main(String[] args) throws Exception {
        for (String apk : apks) {
            System.out.println("<====== apk: " + apk + " ======>");
            analyzeApk(apk);
            System.out.println("---------");
        }
    }

    private static String getTargetSdkVersion(String apkPath) throws Exception {
        ApkUtils apkUtils = new ApkUtils();
        return apkUtils.getTargetSdkVersion(apkPath);
    }

    private static String getPlatformJarPath(String sdkVersion) {
        return sdkRootPath + "android-" + sdkVersion + "/android.jar";
    }

    private static String getSourceSinkFilePath() {
        return Objects.requireNonNull(
                Main.class.getClassLoader().getResource("SourcesAndSinks.txt")).getPath();
    }

    private static void analyzeApk(String apk) throws Exception {
        String apkPath = apkRootPath + apk;

        String targetSdkVersion = getTargetSdkVersion(apkPath);
        System.out.println("Target SDK Version: " + targetSdkVersion);

        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.getAnalysisFileConfig().setAndroidPlatformDir(new File(getPlatformJarPath(targetSdkVersion)));
        config.getAnalysisFileConfig().setTargetAPKFile(new File(apkPath));
        config.getAnalysisFileConfig().setSourceSinkFile(new File(getSourceSinkFilePath()));
        // timeout (seconds)
        config.setDataFlowTimeout(300);

        SetupApplication app = new SetupApplication(config);
        InfoflowResults results = app.runInfoflow();

        if (results.getResults() == null || results.getResults().isEmpty()) {
            System.out.println("[No results found.]");
            return;
        }

        results.getResults().forEach((res) -> {
            System.out.println("Source: " + res.getO2().toString());
            System.out.println("Sinks: " + res.getO1().toString());
        });

    }

    private static void initSootOptions() {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_ignore_resolution_errors(true);
    }
}