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
