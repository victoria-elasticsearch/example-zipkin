# iron/go:dev is the alpine image with the go tools added
FROM golang:latest 


ARG ZIPKIN_HOST
ARG ZIPKIN_PORT

ENV GO-ZIPKIN_HOST=$ZIPKIN_HOST
ENV GO-ZIPKIN_PORT=$ZIPKIN_PORT

RUN mkdir /go/src/app

ADD ./src/main /go/src/app

WORKDIR /go/src/app

RUN go get

RUN go build -o main

EXPOSE 8080

ENTRYPOINT [ "./main" ]