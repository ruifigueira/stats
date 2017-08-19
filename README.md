# Stats application

Very simple stats collecting application for transactions.

## Build

To build and run Unit tests:

```
mvn clean package
```

## Run

To run the application:

```
java -jar target/stats.jar
```

After that, you can test registering transactions:

```
curl -X POST -H "Content-Type: application/json" -d '{"amount":12.3,"timestamp":1478192204000}' http://localhost:8080/transactions
```

and to get current statistics:

```
curl -X GET -H "Content-Type: application/json" http://localhost:8080/statistics
```

or just open http://localhost:8080/statistics in your browser.

## Decisions

### O(1) Complexity

To achieve both memory and time `O(1)` complexity, the idea is that statistics are aggregated per second.
When we want to get statistics for the latest 60 seconds, we combine the latest 60 aggregated statistics, each one providing one second.

To keep these 60 statistics per second, we only need a ring buffer, and every time a second passes, we shift it accordingly, resetting the structures corresponding to periods older than 60 seconds (those same structures
will now become the most recent second structures).

There are some compromises, however, with this solution: The 60 second interval for statistics is not exact, because it starts at millisecond 0 of each second.
That means that, if we are requesting statistics when time is 65.5 sec., instead of getting results from transactions from 5.5sec. to 65.5sec., we'll get from transactions since 6sec. to 66sec.

## Possible Improvements

* `ReadWriteLock` for thread-safety instead of `synchronized` methods, which don't differenciate read and write operations.

