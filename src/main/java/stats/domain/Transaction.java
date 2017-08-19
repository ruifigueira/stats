package stats.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Equality and hashCode are the default methods, it will use object identity.
 *
 * @author rui.figueira
 *
 */
public class Transaction implements Comparable<Transaction> {
    private final double amount;
    private final long timestamp;

    @JsonCreator
    public Transaction(@JsonProperty("amount") double amount, @JsonProperty("timestamp") long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}
