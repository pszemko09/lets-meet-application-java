package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing Calendar object, containing specified working hours and collection of planned meetings.
 */
public class Calendar {
    private WorkingHours workingHours;
    private List<Meeting> plannedMeetings;

    public Calendar(WorkingHours workingHours) {
        this.workingHours = workingHours;
        plannedMeetings = new ArrayList<>();
    }

    public void addMeeting(Meeting meeting){
        plannedMeetings.add(meeting);
    }

    public List<Meeting> getPlannedMeetings() {
        return plannedMeetings;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }
}
