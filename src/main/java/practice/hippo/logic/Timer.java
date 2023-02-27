package practice.hippo.logic;

public class Timer {

    private long startTime;

    public void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public long computeTime() {
        return (System.currentTimeMillis() - startTime);
    }

    public static String computeTimeFormatted(long time) {
        long ms = time % 1000;
        long seconds = (time - ms) / 1000;
        String msStr = "" + ms;
        if (ms == 0) {
            msStr = "000";
        } else if (ms < 10) {
            msStr = "00" + ms;
        } else if (ms < 100) {
            msStr = "0" + ms;
        }
        return "" + seconds + "." + msStr;
    }

}
