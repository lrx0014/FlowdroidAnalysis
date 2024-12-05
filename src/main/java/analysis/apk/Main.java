package analysis.apk;
import soot.options.Options;

public class Main {
    // Specify root path for APK files
    private static final String apkRootPath = "/home/ryan/Downloads/APKs/";
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
    private static final String sdkRootPath = "/home/ryan/WorkSpace/github/Android-platforms/jars/stubs/";
    // Results save path
    private static final String logPath = "output/results.txt";

    public static void main(String[] args) throws Exception {
        initSootOptions();
        ResultSaver saver = new ResultSaver(logPath);

        Analyzer analyzer = new Analyzer();
        analyzer.setApks(apks);
        analyzer.setApkRootPath(apkRootPath);
        analyzer.setSdkRootPath(sdkRootPath);
        analyzer.setResultSaver(saver);

        saver.save(" ============== BEGIN ============== ");
        saver.save("JVM Memory Max: " + Runtime.getRuntime().maxMemory() / (1024 * 1024 * 1024));
        saver.save(" ============== Start Analysis with Timeout 60 ============== ");
        analyzer.run(60);
        saver.save(" ============== Start Analysis with Timeout 300 ============== ");
        analyzer.run(300);
        saver.save(" ============== Start Analysis with Timeout 1200 ============== ");
        analyzer.run(1200);
        saver.save(" ============== END ============== ");
    }

    private static void initSootOptions() {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_ignore_resolution_errors(true);
    }
}