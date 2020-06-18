import com.example.model.TimeRange;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for TimeRange.
 */
public class TimeRangeTest {
    @Test
    public void shouldCreateValidTimeRange(){
        TimeRange range = new TimeRange("09:00", "18:00");
        assertNotNull(range);
        assertThat(range.getFrom()).isEqualTo("09:00");
        assertThat(range.getTo()).isEqualTo("18:00");
    }

    @Test
    public void shouldThrowExceptionWhenCreatingInvalidTimeRange(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new TimeRange("9:00", "18:00");
        });
    }

    @Test
    public void shouldThrowExceptionWhenFromGreaterThanToInTimeRange(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new TimeRange("10:00", "09:00");
        });
        assertThat(exception.getMessage()).isEqualTo("second argument cannot be less than first");
    }

    @Test
    public void shouldThrowExceptionWhenCreatingTimeRangeWithNull(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            new TimeRange(null, "18:00");
        });
    }

    @Test
    public void shouldVerifyCalculateDurationMethod(){
        TimeRange range = new TimeRange("09:00", "18:00");
        assertThat(range.calculateDuration()).isEqualTo("09:00");
    }
}
