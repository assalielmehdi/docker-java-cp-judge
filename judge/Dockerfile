FROM openjdk:11-slim

RUN apt-get update \
    && apt-get install -y build-essential python python3 jq

COPY ./entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

COPY ./examples/light/ /examples/
COPY ./examples/average/ /examples/
COPY ./examples/heavy/ /examples/

COPY ./judge.sh /usr/local/bin/judge

RUN chmod +x /usr/local/bin/judge

ENTRYPOINT ["/entrypoint.sh"]