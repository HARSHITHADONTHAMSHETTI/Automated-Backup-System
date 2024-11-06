import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private final Timer timer = new Timer();

    public Scheduler(Runnable task, long intervalMillis) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, 0, intervalMillis);
    }

    public void stop() {
        timer.cancel();
    }
}
