FROM openjdk:8-alpine

RUN apk add --no-cache maven alpine-sdk && \
    mkdir -p /usr/app

COPY . /usr/app/
WORKDIR /usr/app

CMD ["mvn", "spring-boot:run"]

