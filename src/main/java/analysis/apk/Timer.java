package analysis.apk;

import java.time.Instant;

public class Timer {

    private Instant startTime;

    public void start() {
        startTime = Instant.now();
    }

    public long getStartTime() {
        return startTime.getEpochSecond();
    }

    public long duration() {
        if (startTime == null) {
            throw new IllegalStateException("Timer has not been started. Call start() first.");
        }
        Instant now = Instant.now();
        return now.getEpochSecond() - startTime.getEpochSecond();
    }
}
