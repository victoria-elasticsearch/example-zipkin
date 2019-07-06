package main

import (
    "net/http"
    "log"
    "strings"
    "os"
    zipkin "github.com/openzipkin/zipkin-go"
	zipkinhttp "github.com/openzipkin/zipkin-go/middleware/http"
    //logreporter "github.com/openzipkin/zipkin-go/reporter/log"
    reporterhttp "github.com/openzipkin/zipkin-go/reporter/http"
)


func main() {

    // set up a span reporter
	// reporter := logreporter.NewReporter(log.New(os.Stderr, "", log.LstdFlags))
	// defer reporter.Close()
    reporter := reporterhttp.NewReporter(getZipkinEndpoint())

	// // create our local service endpoint
    endpoint, err := zipkin.NewEndpoint("serviceGo", "localhost:8080")
	if err != nil {
	    log.Fatalf("unable to create local endpoint: %+v\n", err)
	}

	// // initialize our tracer
	tracer, err := zipkin.NewTracer(reporter, zipkin.WithLocalEndpoint(endpoint))
	if err != nil {
		log.Fatalf("unable to create tracer: %+v\n", err)
    }
    
    // create global zipkin http server middleware
    serverMiddleware := zipkinhttp.NewServerMiddleware(
        tracer, zipkinhttp.TagResponseSize(true),
    )

    // create global zipkin traced http client
	client, err := zipkinhttp.NewClient(tracer, zipkinhttp.ClientTrace(true))
	if err != nil {
		log.Fatalf("unable to create client: %+v\n", err)
	}
    
    // Create a new Router instance
    router := makeRouter(client)

    // set the router to use the servier middleware
    router.Use(serverMiddleware)

    // Pass our router to net/http
    http.Handle("/", router)

    // Listen and serve on localhost:8080
    http.ListenAndServe(":8080", nil)
}

func getZipkinEndpoint() string {
    var str strings.Builder
    str.WriteString("http://")
    str.WriteString(getZipkinHost())
    str.WriteString(":")
    str.WriteString(getZipkinPort())
    str.WriteString("/api/v2/spans")
    return str.String();
}

func getZipkinHost() string {
    if(os.Getenv("GO-ZIPKIN-HOST") == "") {
        return "localhost"
    }
    return os.Getenv("GO-ZIPKIN-HOST")
}

func getZipkinPort() string {
    if(os.Getenv("GO-ZIPKIN_PORT") == "") {
        return "9411"
    }
    return os.Getenv("GO-ZIPKIN_PORT")
}

