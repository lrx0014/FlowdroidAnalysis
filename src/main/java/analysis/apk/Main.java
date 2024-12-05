package analysis.apk;
import soot.options.Options;

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
    // Results save path
    private static final String logPath = "output/results.txt";

    public static void main(String[] args) throws Exception {
        initSootOptions();
        Analyzer analyzer = new Analyzer();
        analyzer.setApks(apks);
        analyzer.setApkRootPath(apkRootPath);
        analyzer.setSdkRootPath(sdkRootPath);
        analyzer.setResultSaver(new ResultSaver(logPath));

        analyzer.run(60);
        analyzer.run(300);
        analyzer.run(1200);
    }

    private static void initSootOptions() {
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_ignore_resolution_errors(true);
    }
}