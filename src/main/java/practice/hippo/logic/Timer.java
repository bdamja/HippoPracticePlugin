package practice.hippo.logic;

public class Timer {

    private long startTime;

    public void setStartTime() {
        startTime = System.currentTimeMillis();
    }

    public long computeTime() {
        return (System.currentTimeMillis() - startTime);
    }

}
