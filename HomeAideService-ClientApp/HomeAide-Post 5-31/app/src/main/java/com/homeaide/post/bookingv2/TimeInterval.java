package com.homeaide.post.bookingv2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TimeInterval {
    public long start;
    public long end;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public TimeInterval() {
    }

    public TimeInterval(long start, long end) {
        this.start = start;
        this.end = end;
    }


    public boolean contains (long datetime){
        if ((start <= datetime) && (datetime < end)){
            return true;
        } else {
            return false;
        }

    }

    @Override
    public String toString(){
        return String.format("(%s, %s)", new Date(start).toString(), new Date(end).toString());
    }

    public String toShortString(){
        return String.format("%s %c%s to %s", dateFormat.format(new Date(start)), '\t', timeFormat.format(new Date(start)), timeFormat.format(new Date(end)));
    }

    public TimeInterval intersection(TimeInterval ti){
        long s1 = ti.start;
        long e1 = ti.end;

        return new TimeInterval(Math.max(this.start,s1), Math.min(this.end, e1));
    }

    public long length(){
        return end - start;
    }


    public List<TimeInterval> union(List<TimeInterval> timeIntervalList){
        List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();
        long start = this.start;
        long end = this.end;

        for (TimeInterval ti: timeIntervalList){
            if (((ti.start <= this.start) && (ti.end >= this.start))
                    || ((ti.start <= this.end) && (ti.end >= this.end))){
                start = Math.min(start, ti.start);
                end = Math.max(end, ti.end);
            } else if ((ti.end < this.start) || (ti.start > this.end)){
                timeIntervals.add(ti);
            }
        }

        timeIntervals.add(new TimeInterval(start,end));

        Collections.sort(timeIntervals, (TimeInterval ti1, TimeInterval ti2) -> (Long.compare(ti1.start, ti2.start)));

        return timeIntervals;
    }

    public List<TimeInterval> difference(List<TimeInterval> timeIntervalList){
        List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();

        for (TimeInterval ti: timeIntervalList){
            if ((ti.start <= this.start) && (ti.end >= this.start) && (ti.start != this.start)){
                timeIntervals.add(new TimeInterval(ti.start,this.start));
            }
            if ((ti.start <= this.end) && (ti.end >= this.end) && (ti.end != this.end)) {
                timeIntervals.add(new TimeInterval(this.end,ti.end));
            }
            if ((ti.start > this.end) || (ti.end < this.start)){
                timeIntervals.add(ti);
            }
        }

        Collections.sort(timeIntervals, (TimeInterval ti1, TimeInterval ti2) -> (Long.compare(ti1.start, ti2.start)));

        return timeIntervals;

    }

}

