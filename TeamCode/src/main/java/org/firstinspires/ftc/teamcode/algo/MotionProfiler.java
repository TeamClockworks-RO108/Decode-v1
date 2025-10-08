package org.firstinspires.ftc.teamcode.algo;

import java.util.Iterator;
import java.util.LinkedList;

public class MotionProfiler {

    private final long window;

    public void setWindowEnabled(boolean windowEnabled) {
        this.windowEnabled = windowEnabled;
    }

    private boolean windowEnabled = true;

    private final LinkedList<Entry> entries = new LinkedList<>();

    public MotionProfiler(long window) {
        this.window = window;
    }



    public double feed(double value, long time) {
        if(!windowEnabled){
            return value;
        }
        Entry entry = new Entry(time, value);
        entries.addLast(entry);
        long cutoff = time - window;
        Iterator<Entry> iterator = entries.iterator();
        double sum = 0;
        int cnt = 0;
        while (iterator.hasNext()) {
            Entry e = iterator.next();
            if (e.getTime() < cutoff) {
                iterator.remove();
            } else cnt++;
        }
        int sumdiv = 0;
        int i = 0;
        iterator = entries.iterator();
        while (iterator.hasNext()) {
            Entry e = iterator.next();
            if (i < cnt / 2) {
                sum += e.getValue() * 1;
                sumdiv += 1;
            } else {
                sum += e.getValue() * 1;
                sumdiv += 1;
            }
            i++;
        }

        return cnt != 0 ? sum / sumdiv : 0;
    }

    private static class Entry {

        private final long time;

        private final double value;

        private Entry(long time, double value) {
            this.time = time;
            this.value = value;
        }

        public long getTime() {
            return time;
        }

        public double getValue() {
            return value;
        }


    }

}