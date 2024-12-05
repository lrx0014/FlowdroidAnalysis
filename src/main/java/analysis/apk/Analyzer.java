package analysis.apk;

import soot.jimple.infoflow.android.InfoflowAndroidConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.results.InfoflowResults;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Analyzer {
    // APKs to be analyzed
    private String[] apks;
    // root path for apk files
    private String apkRootPath;
    // Specify root path for SDK
    private String sdkRootPath;

    private ResultSaver resultSaver;
    private ApkUtils apkUtils;
    private Timer timer;

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

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void run(long timeout_sec) throws Exception {
        for (String apk : this.apks) {
            this.runOne(apk, timeout_sec);
        }
    }

    private void runOne(String apk, long timeout_sec) throws Exception {

        this.save("---------------------------------------------------------" + timer.duration());
        this.save("Analyzing apk: " + apk + " in " + this.apkRootPath);
        String apkPath = this.apkRootPath + apk;
        String targetSdkVersion = getTargetSdkVersion(apkPath);
        this.save("Target SDK Version: " + targetSdkVersion);
        this.save("");

        InfoflowAndroidConfiguration config = new InfoflowAndroidConfiguration();
        config.getAnalysisFileConfig().setAndroidPlatformDir(new File(getPlatformJarPath(targetSdkVersion)));
        config.getAnalysisFileConfig().setTargetAPKFile(new File(apkPath));
        config.getAnalysisFileConfig().setSourceSinkFile(getSourcesAndSinksConfig());
        // timeout (seconds)
        config.setDataFlowTimeout(timeout_sec);

        SetupApplication app = new SetupApplication(config);
        InfoflowResults results = app.runInfoflow();

        if (results.getResults() == null || results.getResults().isEmpty()) {
            this.save("[No results found.]");
            return;
        }

        AtomicInteger i = new AtomicInteger(1);
        results.getResults().forEach((res) -> {
            this.save(String.format("[%d] Source: %s", i.get(), res.getO2().toString()));
            this.save(String.format("[%d] Sink: %s", i.get(), res.getO1().toString()));
            i.getAndIncrement();
        });

        this.save("---------------------------------------------------------" + timer.duration());
        this.save("");
    }

    private void save(String row) {
        if (this.resultSaver == null) {
            return;
        }

        String logT = "[Analyzer] " + row;
        System.out.println(logT);
        this.resultSaver.save(row);
    }

    private File getSourcesAndSinksConfig() throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("SourcesAndSinks.txt");
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + "SourcesAndSinks.txt");
        }

        File tempFile = File.createTempFile("SourcesAndSinks", ".txt");
        tempFile.deleteOnExit();

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }
}