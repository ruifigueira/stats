package stats.mocks;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import com.google.common.base.Preconditions;

public class MockClock extends Clock {

    private Instant instant;

    public MockClock(Instant instant) {
        this.instant = instant;
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        Preconditions.checkArgument(instant.compareTo(this.instant) >= 0, "We cannot go back to the past, this clock only moves forward");
        this.instant = instant;
    }

    public void setEpochMilli(long epochMilli) {
        setInstant(Instant.ofEpochMilli(epochMilli));
    }

    public void setEpochSecond(long epochSecond) {
        setInstant(Instant.ofEpochSecond(epochSecond));
    }
}
