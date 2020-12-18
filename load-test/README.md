# Load testing

Refer to https://github.com/rakyll/hey for more details.

```
hey -n <N> -x <C> "http://<H>/test?load=<L>"

    N:  Number of requests to run. Default is 200.
    C:  Number of workers to run concurrently. Total number of requests cannot
        be smaller than the concurrency level. Default is 50.
    H:  IP @ of the host. Don't forget to add port if not default 80.
    L:  One of LIGHT, AVERAGE, HEAVY.
```

Example of execution:

```
hey -n 1 -c 1 "http://localhost:8080/test?load=AVERAGE"
```