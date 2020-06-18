package com.example.model;



import com.google.common.base.Preconditions;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main application class.
 */
public class Main {
    private static final String TIME24HOURS_PATTERN = "(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]";

    public static void main(String[] args) {
        Main main = new Main();

        Calendar calendarFirst = new Calendar(new WorkingHours("09:00", "20:00"));
        calendarFirst.addMeeting(new Meeting("09:00", "10:30"));
        calendarFirst.addMeeting(new Meeting("12:00", "13:00"));
        calendarFirst.addMeeting(new Meeting("16:00", "18:30"));

        Calendar calendarSecond = new Calendar(new WorkingHours("10:00", "18:30"));
        calendarSecond.addMeeting(new Meeting("10:00", "11:30"));
        calendarSecond.addMeeting(new Meeting("12:30", "14:30"));
        calendarSecond.addMeeting(new Meeting("14:30", "15:00"));
        calendarSecond.addMeeting(new Meeting("16:00", "17:00"));

        System.out.println(main.getPossibleMeetingDates(calendarFirst, calendarSecond, "00:30"));

    }

    /**
     * Gets all possible free meeting dates based on provided to calendars and meeting duration provided in format "00:00"
     * @param firstCalendar the calender to be processed
     * @param secondCalendar the calender to be processed
     * @param expectedMeetingDuration the expected meeting duration
     * @return list of all possible time ranges
     */
    public List<TimeRange> getPossibleMeetingDates(Calendar firstCalendar, Calendar secondCalendar, String expectedMeetingDuration){

        //simple method parameters (calendars) validation
        Preconditions.checkNotNull(firstCalendar);
        Preconditions.checkNotNull(secondCalendar);

        //validation for meeting duration format
        Preconditions.checkNotNull(expectedMeetingDuration);
        Preconditions.checkArgument(expectedMeetingDuration.matches(TIME24HOURS_PATTERN));

        List<TimeRange> result = new ArrayList<>();

        List<TimeRange> possibleMeetingDatesInFirstCalendar = getAvailableTimeRanges(firstCalendar, expectedMeetingDuration);
        List<TimeRange> possibleMeetingDatesInSecondCalendar = getAvailableTimeRanges(secondCalendar, expectedMeetingDuration);

        for(TimeRange first : possibleMeetingDatesInFirstCalendar){
            for(TimeRange second : possibleMeetingDatesInSecondCalendar){
                TimeRange timeRangeIntersection = getTimeRangesIntersection(first, second);
                if(timeRangeIntersection.calculateDuration().compareTo(expectedMeetingDuration) >= 0){
                    result.add(timeRangeIntersection);
                }
            }
        }
        return result;
    }

    /**
     * Gets all available time ranges withing working hours based on calendar
     * @param calendar the calender to be processed
     * @return list of all available time ranges
     */
    private List<TimeRange> getAvailableTimeRanges(Calendar calendar, String expectedMeetingDuration){
        List<TimeRange> availableTimeRanges = new ArrayList<>();

        List<Meeting> currentPlannedMeetings = calendar.getPlannedMeetings();
        String minRange = calendar.getWorkingHours().getStart();
        String maxRange = calendar.getWorkingHours().getEnd();

        for(int i = 0; i < currentPlannedMeetings.size(); i++){
            Meeting meeting = currentPlannedMeetings.get(i);
            String start = meeting.getStart();
            String end = meeting.getEnd();

            if((start.compareTo(minRange) > 0)){
                availableTimeRanges.add(new TimeRange(minRange, start));
            }
            if((maxRange.compareTo(end) > 0) && (currentPlannedMeetings.size() - 1) == i){
                availableTimeRanges.add(new TimeRange(end, maxRange));
            }
            minRange = end;
        }
        return filterTimeRanges(availableTimeRanges, expectedMeetingDuration);
    }

    /**
     * Filters provided time ranges based on parameter specifying meeting duration. Accepts data if
     * time range duration is not less than expected duration
     * @param timeRanges list of time ranges to be processed
     * @param expectedMeetingDuration the duration for filtering data
     * @return list containing result of filtering
     */
    public List<TimeRange> filterTimeRanges(List<TimeRange> timeRanges, String expectedMeetingDuration){
        return timeRanges.stream()
                .filter(element -> element.calculateDuration().compareTo(expectedMeetingDuration) >= 0)
                .collect(Collectors.toList());
    }

    /**
     * Gets intersection of two specified time ranges.
     * @param firstRange time range to be processed
     * @param secondRange time range to be processed
     * @return time range representing the intersection of sets
     */
    private TimeRange getTimeRangesIntersection(TimeRange firstRange, TimeRange secondRange){

        //first range
        final LocalTime firstStart = firstRange.getFrom();
        final LocalTime firstEnd = firstRange.getTo();

        //second range
        final LocalTime secondStart = secondRange.getFrom();
        final LocalTime secondEnd = secondRange.getTo();

        if(!firstStart.isAfter(secondStart) && !firstEnd.isBefore(secondEnd)){
            return secondRange;
        }
        else if(!firstStart.isBefore(secondStart) && !firstEnd.isAfter(secondEnd)){
            return firstRange;
        }
        else if(firstStart.isBefore(secondStart) && firstEnd.isAfter(secondStart)){
            return new TimeRange(secondStart.toString(), firstEnd.toString());
        }
        else if(firstStart.isAfter(secondStart) && firstStart.isBefore(secondEnd) && firstEnd.isAfter(secondEnd)){
            return new TimeRange(firstStart.toString(), secondEnd.toString());
        }

        return new TimeRange("00:00", "00:00");
    }

}
