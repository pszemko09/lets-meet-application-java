package com.example.model;

/**
 * Class representing single meeting, starting with {@code start} and ending with {@code end} both inclusively.
 */
public class Meeting {
    private String start;
    private String end;

    public Meeting(String start, String end){
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
