package analysis.apk;
import soot.options.Options;

public class Main {
    // Specify root path for APK files
    private static String apkRootPath;
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
    private static String sdkRootPath;
    // Results save path
    private static final String logPath = "output/results.txt";

    public static void main(String[] args) throws Exception {
        parseArgs(args);
        initSootOptions();
        ResultSaver saver = new ResultSaver(logPath);
        Timer timer = new Timer();

        Analyzer analyzer = new Analyzer();
        analyzer.setApks(apks);
        analyzer.setApkRootPath(apkRootPath);
        analyzer.setSdkRootPath(sdkRootPath);
        analyzer.setResultSaver(saver);
        analyzer.setTimer(timer);

        timer.start();
        saver.save(String.format(" ============== BEGIN (%d) ============== ", timer.getStartTime()));
        saver.save("JVM Memory Max: " + Runtime.getRuntime().maxMemory() / (1024 * 1024 * 1024));
        saver.save(" ============== Start Analysis with Timeout 60 ============== ");
        analyzer.run(60);
        saver.save(" ============== Start Analysis with Timeout 300 ============== ");
        analyzer.run(300);
        saver.save(" ============== Start Analysis with Timeout 1200 ============== ");
        analyzer.run(1200);
        saver.save(String.format(" ============== END (duration: %d) ============== ", timer.duration()));
    }

    private static void initSootOptions() {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_ignore_resolution_errors(true);
    }

    private static void parseArgs(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--sdk_root_path=")) {
                sdkRootPath = arg.substring("--sdk_root_path=".length());
            } else if (arg.startsWith("--apk_root_path=")) {
                apkRootPath = arg.substring("--apk_root_path=".length());
            }
        }
    }
}