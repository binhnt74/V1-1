package Test;

import java.util.Timer;
import java.util.TimerTask;

public class Tests {
    public static int i = 1;

        public static void main(String[] args) {

            Timer timer = new Timer();

            // Helper class extends TimerTask
            TimerTask task = new Helper();

            /*
             *  Schedule() method calls for timer class.
             *  void schedule(TimerTask task, Date firstTime, long period)
             */

            timer.schedule(task, 100, 100);

        }

    static class Helper extends TimerTask {

        // TimerTask.run() method will be used to perform the action of the task

        public void run() {
            System.out.println("This is called " + i++ + " time");
        }
    }
    }

