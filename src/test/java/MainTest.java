import com.example.model.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {
    private Main main = new Main();

    @Test
    public void shouldCheckFilterTimeRangesMethod(){
        List<TimeRange> timeRanges = new ArrayList<>();
        timeRanges.add(new TimeRange("16:00", "16:30"));
        timeRanges.add(new TimeRange("17:00", "18:00"));
        timeRanges.add(new TimeRange("18:00", "21:00"));

        assertThat(main.filterTimeRanges(timeRanges, "01:00")).hasSize(2);
    }

    @Test
    public void shouldThrowException(){
        Calendar calendarFirst = new Calendar(new WorkingHours("09:00", "12:00"));
        Assertions.assertThrows(NullPointerException.class, () -> {
            main.getPossibleMeetingDates(calendarFirst, null, "01:00");
        });
    }

    @Test
    public void shouldThrowExceptionWhenInvalidMeetingDurationFormat(){
        Calendar first = new Calendar(new WorkingHours("09:00", "12:00"));
        Calendar second = new Calendar(new WorkingHours("09:00", "12:00"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            main.getPossibleMeetingDates(first, second, "2:00");
        });
    }

    @Test
    public void shouldNotFindPossibleDates(){
        Calendar calendarFirst = new Calendar(new WorkingHours("09:00", "12:00"));
        calendarFirst.addMeeting(new Meeting("09:00", "10:00"));
        calendarFirst.addMeeting(new Meeting("11:00", "12:00"));

        Calendar calendarSecond = new Calendar(new WorkingHours("10:00", "13:00"));
        calendarSecond.addMeeting(new Meeting("10:00", "12:00"));

        assertThat(main.getPossibleMeetingDates(calendarFirst, calendarSecond, "01:00")).hasSize(0);
    }

    @Test
    public void shouldFindOnePossibleDates(){
        Calendar calendarFirst = new Calendar(new WorkingHours("09:00", "13:00"));
        calendarFirst.addMeeting(new Meeting("10:00", "11:30"));
        calendarFirst.addMeeting(new Meeting("12:00", "13:00"));

        Calendar calendarSecond = new Calendar(new WorkingHours("10:00", "13:00"));
        calendarSecond.addMeeting(new Meeting("10:00", "11:00"));
        calendarSecond.addMeeting(new Meeting("12:00", "12:30"));

        assertThat(main.getPossibleMeetingDates(calendarFirst, calendarSecond, "00:30")).hasSize(1);
    }

    @Test
    public void shouldFinPossibleDates(){
        Calendar calendarFirst = new Calendar(new WorkingHours("09:00", "18:00"));
        calendarFirst.addMeeting(new Meeting("10:00", "12:00"));
        calendarFirst.addMeeting(new Meeting("12:30", "13:30"));
        calendarFirst.addMeeting(new Meeting("17:00", "17:30"));

        Calendar calendarSecond = new Calendar(new WorkingHours("08:00", "17:30"));
        calendarSecond.addMeeting(new Meeting("08:30", "09:00"));
        calendarSecond.addMeeting(new Meeting("10:00", "12:30"));
        calendarSecond.addMeeting(new Meeting("14:30", "17:00"));

        assertThat(main.getPossibleMeetingDates(calendarFirst, calendarSecond, "00:30")).hasSize(2);
    }
}
