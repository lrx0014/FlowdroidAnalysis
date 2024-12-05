package analysis.apk;

import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.results.InfoflowResults;

import java.io.File;
import java.util.Objects;

public class Analyzer {
    // APKs to be analyzed
    private String[] apks;
    // root path for apk files
    private String apkRootPath;
    // Specify root path for SDK
    private String sdkRootPath;

    private ResultSaver resultSaver;
    private ApkUtils apkUtils;

    public Analyzer() {
        this.apkUtils = new ApkUtils();
    }

    private String getTargetSdkVersion(String apkPath) throws Exception {
        return this.apkUtils.getTargetSdkVersion(apkPath);
    }

    private String getPlatformJarPath(String sdkVersion) {
        return sdkRootPath + "android-" + sdkVersion + "/android.jar";
    }

    private String getSourceSinkFilePath() {
        return Objects.requireNonNull(
                Main.class.getClassLoader().getResource("SourcesAndSinks.txt")).getPath();
    }

    public void setSdkRootPath(String sdkRootPath) {
        this.sdkRootPath = sdkRootPath;
    }

    public void setApkRootPath(String apkRootPath) {
        this.apkRootPath = apkRootPath;
    }

    public void setApks(String[] apks) {
        this.apks = apks;
    }

    public void setResultSaver(ResultSaver resultSaver) {
        this.resultSaver = resultSaver;
    }

    public void run(long timeout_sec) throws Exception {
        for (String apk : this.apks) {
            this.runOne(apk, timeout_sec);
        }
    }

    private void runOne(String apk, long timeout_sec) throws Exception {

        System.out.println("[Analyzer] Analyzing apk: " + apk + " in " + this.apkRootPath);
        String apkPath = this.apkRootPath + apk;
        String targetSdkVersion = getTargetSdkVersion(apkPath);
        System.out.println("Target SDK Version: " + targetSdkVersion);

        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.getAnalysisFileConfig().setAndroidPlatformDir(new File(getPlatformJarPath(targetSdkVersion)));
        config.getAnalysisFileConfig().setTargetAPKFile(new File(apkPath));
        config.getAnalysisFileConfig().setSourceSinkFile(new File(getSourceSinkFilePath()));
        // timeout (seconds)
        config.setDataFlowTimeout(timeout_sec);

        SetupApplication app = new SetupApplication(config);
        InfoflowResults results = app.runInfoflow();

        if (results.getResults() == null || results.getResults().isEmpty()) {
            System.out.println("[No results found.]");
            return;
        }

        results.getResults().forEach((res) -> {
            this.save("Source: " + res.getO2().toString());
            this.save("Sinks: " + res.getO1().toString());
        });
    }

    private void save(String row) {
        if (this.resultSaver == null) {
            return;
        }

        String logT = "[Analyzer] " + row;
        System.out.println(logT);
        this.resultSaver.save(row);
    }
}