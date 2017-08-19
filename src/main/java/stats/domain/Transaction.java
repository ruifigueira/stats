package stats.domain;


/**
 * Equality and hashCode are the default methods, it will use object identity.
 *
 * @author rui.figueira
 *
 */
public class Transaction implements Comparable<Transaction> {
    private double amount;
    private long timestamp;

    public Transaction() {
    }

    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}
