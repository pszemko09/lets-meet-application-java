package com.example.model;

import com.google.common.base.Preconditions;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Class representing time range, starting with {@code from} and ending with {@code to} both inclusively.
 */
public class TimeRange {
    private LocalTime from;
    private LocalTime to;

    public TimeRange(String from, String to) {

        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(from.compareTo(to) <= 0, "second argument cannot be less than first");

        //casting String object to LocalTime
        this.from = LocalTime.of(Integer.parseInt(from.substring(0, 2)), Integer.parseInt(from.substring(3)));
        this.to = LocalTime.of(Integer.parseInt(to.substring(0, 2)), Integer.parseInt(to.substring(3)));

    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    public String calculateDuration(){
        Duration duration = Duration.between(from, to);

        long durationInMinutes = duration.toMinutes();

        int hours = (int) durationInMinutes / 60;
        int minutes = (int) durationInMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public String toString() {
        return "[" + from + "," +  to + "]";
    }
}
